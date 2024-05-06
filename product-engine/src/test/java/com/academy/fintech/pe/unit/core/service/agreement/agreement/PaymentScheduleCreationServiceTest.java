package com.academy.fintech.pe.unit.core.service.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.PaymentScheduleCreationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentService;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.exception.AgreementDoesNotExists;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentScheduleCreationServiceTest {

    @Mock
    private PaymentScheduleService paymentScheduleService;

    @Mock
    private LoanPaymentService loanPaymentService;

    @Mock
    private AgreementService agreementService;

    @InjectMocks
    private PaymentScheduleCreationService paymentScheduleCreationService;

    private final LocalDate disbursementDate = LocalDate.parse("2023-11-30");

    @Test
    void givenValidAgreementId_whenCreateSchedule_thenReturnCorrectSchedule() {
        // given
        var dto = new PaymentScheduleRequestDto(UUID.fromString("00000000-0000-0000-0000-000000000001"), disbursementDate);
        Agreement agreement = createAgreement();
        PaymentSchedule schedule = new PaymentSchedule(agreement, 0);
        List<LoanPayment> expectedPayments = createLoanPayments(schedule);
        when(agreementService.getById(dto.agreementId())).thenReturn(agreement);
        when(paymentScheduleService.createNewSchedule(agreement)).thenReturn(schedule);
        when(loanPaymentService.saveAll(anyList())).thenReturn(null);

        // when
        PaymentSchedule actualSchedule = paymentScheduleCreationService.createSchedule(dto);

        // then
        assert actualSchedule != null;
        assertIterableEquals(actualSchedule.getPayments(), expectedPayments);
    }

    @Test
    void givenInvalidAgreementId_whenCreateSchedule_thenReturnNull() {
        var dto = new PaymentScheduleRequestDto(UUID.fromString("00000000-0000-0000-0000-000000000001"), LocalDate.parse("2023-12-31"));
        when(agreementService.getById(dto.agreementId())).thenReturn(null);

        assertThrows(AgreementDoesNotExists.class, () -> paymentScheduleCreationService.createSchedule(dto));
    }

    private Product createProduct() {
        Product newProduct = new Product();
        newProduct.setCode("CL2.0");
        newProduct.setMinTerm(3);
        newProduct.setMaxTerm(20);
        newProduct.setMinPrincipalAmount(BigDecimal.valueOf(50_000));
        newProduct.setMaxPrincipalAmount(BigDecimal.valueOf(600_000));
        newProduct.setMinInterest(BigDecimal.valueOf(10));
        newProduct.setMaxInterest(BigDecimal.valueOf(21));
        newProduct.setMinOriginationAmount(BigDecimal.valueOf(1000));
        newProduct.setMaxOriginationAmount(BigDecimal.valueOf(5000));
        return newProduct;
    }

    private Agreement createAgreement() {
        Product product = createProduct();
        return Agreement.builder()
                .product(product)
                .clientId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .interest(BigDecimal.valueOf(10))
                .termInMonths(12)
                .principalAmount(BigDecimal.valueOf(65000))
                .originationAmount(BigDecimal.valueOf(5000))
                .status(AgreementStatus.NEW)
                .build();
    }

    private List<LoanPayment> createLoanPayments(PaymentSchedule schedule) {
        BigDecimal periodPayment = BigDecimal.valueOf(5714.53);
        double[] interestPayments = {541.67, 498.56, 455.09, 411.26, 367.07, 322.51, 277.57, 232.27, 186.58, 140.52, 94.06, 47.23};
        String[] dates = {"2023-12-30", "2024-01-30", "2024-02-29", "2024-03-29", "2024-04-29", "2024-05-29", "2024-06-29", "2024-07-29", "2024-08-29", "2024-09-29", "2024-10-29", "2024-11-29"};

        List<LoanPayment> payments = new ArrayList<>();
        for (int i = 0; i < dates.length; i++) {
            var payment = LoanPayment.builder()
                    .paymentId(0)
                    .periodNumber(i)
                    .paymentDate(LocalDate.parse(dates[i]))
                    .status(LoanPaymentStatus.FUTURE)
                    .paymentSchedule(schedule)
                    .interestPayment(BigDecimal.valueOf(interestPayments[i]))
                    .periodPayment(periodPayment)
                    .principalPayment(periodPayment.subtract(BigDecimal.valueOf(interestPayments[i])))
                    .build();

            payments.add(payment);
        }
        return payments;
    }
}
