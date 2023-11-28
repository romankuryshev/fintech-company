package com.academy.fintech.pe.unit.grpc.service.agreement.agreement.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.PaymentScheduleCreationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.exception.AgreementDoesNotExists;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleController;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleRequest;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleResponse;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper.LoanPaymentMapper;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper.LoanPaymentMapperImpl;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper.PaymentScheduleMapper;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.mapper.PaymentScheduleMapperImpl;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentScheduleControllerTest {

    @Mock
    private PaymentScheduleCreationService creationService;

    @Spy
    private LoanPaymentMapper paymentMapper = new LoanPaymentMapperImpl();

    @Spy
    @InjectMocks
    private PaymentScheduleMapper mapper = new PaymentScheduleMapperImpl();

    @InjectMocks
    private PaymentScheduleController controller;

    @Test
    void givenValidRequest_whenCreateSchedule_thenReturnResponseOk() throws Exception {
        // given
        Agreement agreement = new Agreement();
        agreement.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));

        var request = PaymentScheduleRequest.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000002")
                .setDisbursementDate("2023-12-31")
                .build();
        PaymentSchedule schedule = new PaymentSchedule(agreement, 1);
        schedule.setPayments(createLoanPayments(schedule));
        StreamRecorder<PaymentScheduleResponse> responseObserver = StreamRecorder.create();
        when(creationService.createSchedule(any(PaymentScheduleRequestDto.class))).thenReturn(schedule);

        // when
        controller.createSchedule(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            Assertions.fail("The call did not terminate in time");
        }

        // then
        List<PaymentScheduleResponse> results = responseObserver.getValues();
        assertThat(results.size()).isEqualTo(1);
        PaymentScheduleResponse response = results.get(0);
        assertThat(response.getPaymentsCount()).isEqualTo(schedule.getPayments().size());
        assertThat(response.getAgreementId()).isEqualTo("00000000-0000-0000-0000-000000000002");
    }

    @Test
    void givenInvalidRequest_whenCreateSchedule_thenReturnResponseError() throws Exception {
        var request = PaymentScheduleRequest.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000002")
                .setDisbursementDate("2023-12-31")
                .build();
        StreamRecorder<PaymentScheduleResponse> responseObserver = StreamRecorder.create();
        when(creationService.createSchedule(any(PaymentScheduleRequestDto.class))).thenThrow(new AgreementDoesNotExists("Agreement does not exists"));


        AgreementDoesNotExists exception = assertThrows(AgreementDoesNotExists.class, () -> controller.createSchedule(request, responseObserver));
    }


    private List<LoanPayment> createLoanPayments(PaymentSchedule schedule) {
        BigDecimal periodPayment = BigDecimal.valueOf(5714.53);
        double[] interestPayments = {541.67, 498.56, 455.09, 411.26, 367.07, 322.51, 277.57, 232.27, 186.58, 140.52, 94.06, 47.23};
        String[] dates = {"2023-12-30", "2024-01-30", "2024-02-29", "2024-03-29", "2024-04-29", "2024-05-29", "2024-06-29", "2024-07-29", "2024-08-29", "2024-09-29", "2024-10-29", "2024-11-29"};

        List<LoanPayment> payments = new ArrayList<>();
        for (int i = 0; i < dates.length; i++) {
            var payment = LoanPayment.builder()
                    .paymentId(i)
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
