package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentService;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementStatus;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Класс управляет созданием расписаний {@link PaymentSchedule} и платежей {@link LoanPayment}.
 * <p>
 * {@link #DIVIDE_SCALE} - Количество цифр после запятой при округлении для вычислений.
 * {@link #AMOUNT_SCALE} - Количество цифр после запятой при округлении для итоговых сумм.
 */
@Service
public class PaymentScheduleCreationService {

    private static final int COUNT_PAYMENTS_PER_YEAR = 12;

    private static final int DIVIDE_SCALE = 8;

    private static final int AMOUNT_SCALE = 2;

    private final PaymentScheduleService paymentScheduleService;

    private final LoanPaymentService loanPaymentService;

    private final AgreementService agreementService;

    @Autowired
    public PaymentScheduleCreationService(PaymentScheduleService paymentScheduleService, LoanPaymentService loanPaymentService, AgreementService agreementService) {
        this.paymentScheduleService = paymentScheduleService;
        this.loanPaymentService = loanPaymentService;
        this.agreementService = agreementService;
    }

    /**
     * Метод новое расписание платежей
     * @param scheduleDto
     * @return Optional {@code null} если создание невозможно, {@link PaymentSchedule} иначе
     */
    @Transactional
    public Optional<PaymentSchedule> createSchedule(PaymentScheduleRequestDto scheduleDto) {
        Agreement agreement = agreementService.getById(scheduleDto.agreementId());
        if (agreement == null) {
            return Optional.empty();
        }
        PaymentSchedule schedule = paymentScheduleService.createNewSchedule(agreement);
        List<LoanPayment> payments = createPayments(agreement, schedule, scheduleDto.disbursementDate());

        schedule.setPayments(payments);
        agreement.setStatus(AgreementStatus.ACTIVE);
        agreement.setDisbursementDate(scheduleDto.disbursementDate().plusMonths(1));

        loanPaymentService.saveAll(payments);
        paymentScheduleService.save(schedule);
        agreementService.save(agreement);
        return Optional.of(schedule);
    }

    /**
     * Метод создает список сумм, которые пойдут на погашение процента от кредита.
     * @param agreement  кредитный договор
     * @param periodPayment  ежемесячный платеж
     * @return List<BigDecimal>  платежи по процентам для данного договора
     */
    private List<BigDecimal> createInterestPayments(Agreement agreement, BigDecimal periodPayment) {
        List<BigDecimal> interestPayments = new ArrayList<>();
        BigDecimal periodicInterest = agreement.getInterest()
                .divide(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(COUNT_PAYMENTS_PER_YEAR), DIVIDE_SCALE, RoundingMode.HALF_UP);

        for (int i = 0; i < agreement.getTerm(); i++) {
            interestPayments.add(IPMT(periodicInterest, periodPayment, agreement.getPrincipalAmount(), i));
        }
        return interestPayments;
    }

    /**
     * Метод создает список сумм, которые пойдут на погашение основной суммы кредита.
     * @param interestPayments  платежи для погашения процентов
     * @param periodPayment  ежемесячный платеж
     * @return List<BigDecimal>  платежи для погашения основной суммы
     */
    private List<BigDecimal> createPrincipalPayments(List<BigDecimal> interestPayments, BigDecimal periodPayment) {
        List<BigDecimal> principalPayments = new ArrayList<>();
        for (BigDecimal interestPayment : interestPayments) {
            principalPayments.add(PPMT(periodPayment, interestPayment));
        }
        return principalPayments;
    }

    private List<LoanPayment> createPayments(Agreement agreement, PaymentSchedule schedule, LocalDate disbursementDate) {
        BigDecimal periodPayment = PMT(agreement);
        List<BigDecimal> interestPayments = createInterestPayments(agreement, periodPayment);
        List<BigDecimal> principalPayments = createPrincipalPayments(interestPayments, periodPayment);

        List<LoanPayment> payments = new ArrayList<>();
        LocalDate nextPaymentDate = disbursementDate.plusMonths(1);
        for (int i = 0; i < agreement.getTerm(); i++) {
            var payment = LoanPayment.builder()
                    .paymentSchedule(schedule)
                    .status(LoanPaymentStatus.FUTURE)
                    .paymentDate(nextPaymentDate)
                    .periodPayment(periodPayment)
                    .interestPayment(interestPayments.get(i))
                    .principalPayment(principalPayments.get(i))
                    .periodNumber(i)
                    .build();

            nextPaymentDate = nextPaymentDate.plusMonths(1);
            payments.add(payment);
        }
        return payments;
    }

    /**
     * Метод рассчитывает ежемесячный платеж по кредиту
     * @param agreement  кредитный договор
     * @return ежемесячный платеж
     */
    private BigDecimal PMT(Agreement agreement) {
        BigDecimal periodicInterest = agreement.getInterest()
                .divide(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(COUNT_PAYMENTS_PER_YEAR), DIVIDE_SCALE, RoundingMode.HALF_UP);

        BigDecimal coefficient = BigDecimal.ONE.add(periodicInterest)
                .pow(agreement.getTerm());

        BigDecimal periodPayment = agreement.getPrincipalAmount()
                .multiply(periodicInterest)
                .multiply(coefficient)
                .divide(coefficient
                        .subtract(BigDecimal.ONE), RoundingMode.HALF_UP);

        return periodPayment.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Метод рассчитывает сумму платежа по погашению процентов для платежа под номером {@code period}.
     * @param interest  процентная ставка за один месяц
     * @param periodPayment  сумма ежемесячного платежа
     * @param principalAmount  сумма кредита
     * @param period  порядковый номер платежа
     * @return сумма платежа по погашению процентов
     */
    private BigDecimal IPMT(BigDecimal interest, BigDecimal periodPayment, BigDecimal principalAmount, int period) {
        BigDecimal coefficient = BigDecimal.ONE
                .add(interest)
                .pow(period);
        BigDecimal remainder = principalAmount
                .multiply(interest)
                .subtract(periodPayment);
        BigDecimal amount = periodPayment.add(coefficient.multiply(remainder));
        return amount.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Метод рассчитывает платеж для погашения основной суммы кредита
     * @param periodPayment  порядковый номер платежа
     * @param interestPayment  платеж по процентам
     * @return  платеж для погашения основной суммы кредита
     */
    private BigDecimal PPMT(BigDecimal periodPayment, BigDecimal interestPayment) {
        return periodPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
    }
}
