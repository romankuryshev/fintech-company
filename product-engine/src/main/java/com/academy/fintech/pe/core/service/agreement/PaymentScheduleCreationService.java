package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentService;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.exception.AgreementDoesNotExists;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.AdvancedPaymentRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс управляет созданием расписаний {@link PaymentSchedule} и платежей {@link LoanPayment}.
 * <p>
 * {@link #DIVIDE_SCALE} - Количество цифр после запятой при округлении для вычислений.
 * {@link #AMOUNT_SCALE} - Количество цифр после запятой при округлении для итоговых сумм.
 */
@Service
@Slf4j
@AllArgsConstructor
public class PaymentScheduleCreationService {

    private static final int COUNT_PAYMENTS_PER_YEAR = 12;

    private static final int DIVIDE_SCALE = 8;

    private static final int AMOUNT_SCALE = 2;

    private final PaymentScheduleService paymentScheduleService;

    private final LoanPaymentService loanPaymentService;

    private final AgreementService agreementService;

    /**
     * Метод создает новое расписание платежей
     * @param scheduleDto
     * @return Optional {@code null} если создание невозможно, {@link PaymentSchedule} иначе
     */
    @Transactional
    public PaymentSchedule createSchedule(PaymentScheduleRequestDto scheduleDto) {
        Agreement agreement = agreementService.getById(scheduleDto.agreementId());
        if (agreement == null) {
            log.error("Agreement does not exists. Id - " + scheduleDto.agreementId());
            throw new AgreementDoesNotExists("Agreement with id - " + scheduleDto.agreementId() + " does not exists");
        }

        PaymentSchedule schedule = paymentScheduleService.createNewSchedule(agreement);
        List<LoanPayment> payments = getCalculatedPayments(agreement, schedule, scheduleDto.disbursementDate());

        schedule.setPayments(payments);
        agreement.setStatus(AgreementStatus.ACTIVE);
        agreement.setDisbursementDate(scheduleDto.disbursementDate());
        agreement.setNextPaymentDate(scheduleDto.disbursementDate().plusMonths(1));

        loanPaymentService.saveAll(payments);
        paymentScheduleService.save(schedule);
        agreementService.saveAndSendToDwh(agreement);
        return schedule;
    }

    public BigDecimal calculateAdvancedPayment(AdvancedPaymentRequestDto requestDto) {
        return PMT(requestDto.interest(), requestDto.termInMonths(), requestDto.disbursementAmount());
    }

    /**
     * Метод создает список сумм, которые пойдут на погашение процента от кредита.
     * @param agreement  кредитный договор
     * @param periodPayment  ежемесячный платеж
     * @return List<BigDecimal>  платежи по процентам для данного договора
     */
    private List<BigDecimal> getCalculatedInterestPayments(Agreement agreement, BigDecimal periodPayment) {
        List<BigDecimal> interestPayments = new ArrayList<>();

        if (agreement.getInterest().equals(BigDecimal.ZERO)) {
            for (int i = 0; i < agreement.getTermInMonths(); i++) {
                interestPayments.add(BigDecimal.ZERO);
            }

        } else {
            BigDecimal periodicInterest = agreement.getInterest()
                    .divide(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(COUNT_PAYMENTS_PER_YEAR), DIVIDE_SCALE, RoundingMode.HALF_UP);

            for (int i = 0; i < agreement.getTermInMonths(); i++) {
                interestPayments.add(IPMT(periodicInterest, periodPayment, agreement.getPrincipalAmount(), i));
            }
        }

        return interestPayments;
    }

    /**
     * Метод создает список сумм, которые пойдут на погашение основной суммы кредита.
     * @param interestPayments  платежи для погашения процентов
     * @param periodPayment  ежемесячный платеж
     * @return List<BigDecimal>  платежи для погашения основной суммы
     */
    private List<BigDecimal> getCalculatedPrincipalPayments(List<BigDecimal> interestPayments, BigDecimal periodPayment) {
        List<BigDecimal> principalPayments = new ArrayList<>();
        for (BigDecimal interestPayment : interestPayments) {
            principalPayments.add(PPMT(periodPayment, interestPayment));
        }
        return principalPayments;
    }

    private List<LoanPayment> getCalculatedPayments(Agreement agreement, PaymentSchedule schedule, LocalDate disbursementDate) {
        BigDecimal periodPayment = PMT(agreement.getInterest(), agreement.getTermInMonths(), agreement.getPrincipalAmount());
        List<BigDecimal> interestPayments = getCalculatedInterestPayments(agreement, periodPayment);
        List<BigDecimal> principalPayments = getCalculatedPrincipalPayments(interestPayments, periodPayment);

        List<LoanPayment> payments = new ArrayList<>();
        LocalDate nextPaymentDate = disbursementDate.plusMonths(1);
        for (int i = 0; i < agreement.getTermInMonths(); i++) {
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
     * @param interest  процентная ставка по кредиту
     * @param countPayments  количество платежей
     * @param principalAmount  сумма кредита
     * @return  часть ежемесячного платежа на погашение основной суммы кредита
     */
    private BigDecimal PMT(BigDecimal interest, int countPayments, BigDecimal principalAmount) {

        if (interest.equals(BigDecimal.ZERO)) {
            return principalAmount
                    .divide(BigDecimal.valueOf(countPayments), DIVIDE_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal periodicInterest = interest
                .divide(BigDecimal.valueOf(100), DIVIDE_SCALE, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(COUNT_PAYMENTS_PER_YEAR), DIVIDE_SCALE, RoundingMode.HALF_UP);

        BigDecimal coefficient = BigDecimal.ONE.add(periodicInterest)
                .pow(countPayments);

        BigDecimal periodPayment = principalAmount
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
