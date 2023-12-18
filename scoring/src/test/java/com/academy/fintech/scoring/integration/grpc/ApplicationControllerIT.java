package com.academy.fintech.scoring.integration.grpc;

import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import com.academy.fintech.scoring.application_processing.AgreementResponse;
import com.academy.fintech.scoring.application_processing.ApplicationProcessingServiceGrpc.ApplicationProcessingServiceBlockingStub;
import com.academy.fintech.scoring.application_processing.ClientAgreementsRequest;
import com.academy.fintech.scoring.application_processing.ClientAgreementsResponse;
import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import com.academy.fintech.scoring.application_processing.ProcessApplicationResponse;
import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import com.academy.fintech.scoring.core.pe.client.grpc.ProductEngineGrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
        properties = {
                "grpc.server.inProcessName=test",
                "grpc.server.port=-1",
                "grpc.client.testClient.address=in-process:test"
        }
)
public class ApplicationControllerIT {

    @MockBean
    private ProductEngineGrpcClient productEngineGrpcClient;

    @GrpcClient("testClient")
    private ApplicationProcessingServiceBlockingStub stub;

    @Test
    void givenValidRequest_whenProcessApplication_thenReturnAccepted() {
        // given
        ProcessApplicationRequest request = ProcessApplicationRequest.newBuilder()
                .setApplicationId("00000000-0000-0000-0000-000000000001")
                .setClientId("00000000-0000-0000-0000-000000000001")
                .setDisbursementAmount("100000")
                .setClientSalary("50000")
                .build();
        AgreementResponse agreementResponse1 = AgreementResponse.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000001")
                .setNextPaymentDate(LocalDate.now().minusDays(1).toString())
                .build();
        AgreementResponse agreementResponse2 = AgreementResponse.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000002")
                .setNextPaymentDate(LocalDate.now().minusDays(2).toString())
                .build();
        List<AgreementResponse> agreementResponses = List.of(agreementResponse1, agreementResponse2);
        ClientAgreementsResponse clientAgreementsResponse = ClientAgreementsResponse.newBuilder()
                .addAllAgreements(agreementResponses)
                .build();

        AdvancedPaymentResponse paymentResponse = AdvancedPaymentResponse.newBuilder()
                .setPaymentAmount("15000")
                .build();

        ProductResponse productResponse = ProductResponse.newBuilder()
                .setCode("CL1.0")
                .setMinInterest("8")
                .setMaxInterest("15")
                .setMinTermInMonths(3)
                .setMaxTermInMonths(20)
                .setMinPrincipalAmount("20000")
                .setMaxPrincipalAmount("200000")
                .build();

        when(productEngineGrpcClient.getClientAgreements(any(ClientAgreementsRequest.class))).thenReturn(clientAgreementsResponse);
        when(productEngineGrpcClient.getPaymentAmount(any(AdvancedPaymentRequest.class))).thenReturn(paymentResponse);
        when(productEngineGrpcClient.getProduct(any(ProductRequest.class))).thenReturn(productResponse);

        // when
        ProcessApplicationResponse response = stub.processApplication(request);

        // then
        assertThat(response.getStatus()).isEqualTo("ACCEPTED");
    }

    @Test
    void givenInvalidRequest_whenProcessApplication_thenReturnCanceled() {
        // given
        ProcessApplicationRequest request = ProcessApplicationRequest.newBuilder()
                .setApplicationId("00000000-0000-0000-0000-000000000001")
                .setClientId("00000000-0000-0000-0000-000000000001")
                .setDisbursementAmount("100000")
                .setClientSalary("50000")
                .build();
        AgreementResponse agreementResponse1 = AgreementResponse.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000001")
                .setNextPaymentDate(LocalDate.now().minusDays(1).toString())
                .build();
        AgreementResponse agreementResponse2 = AgreementResponse.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000002")
                .setNextPaymentDate(LocalDate.now().minusDays(2).toString())
                .build();
        List<AgreementResponse> agreementResponses = List.of(agreementResponse1, agreementResponse2);
        ClientAgreementsResponse clientAgreementsResponse = ClientAgreementsResponse.newBuilder()
                .addAllAgreements(agreementResponses)
                .build();

        AdvancedPaymentResponse paymentResponse = AdvancedPaymentResponse.newBuilder()
                .setPaymentAmount("30000") // this bad param for score
                .build();

        ProductResponse productResponse = ProductResponse.newBuilder()
                .setCode("CL1.0")
                .setMinInterest("8")
                .setMaxInterest("15")
                .setMinTermInMonths(3)
                .setMaxTermInMonths(20)
                .setMinPrincipalAmount("20000")
                .setMaxPrincipalAmount("200000")
                .build();

        when(productEngineGrpcClient.getClientAgreements(any(ClientAgreementsRequest.class))).thenReturn(clientAgreementsResponse);
        when(productEngineGrpcClient.getPaymentAmount(any(AdvancedPaymentRequest.class))).thenReturn(paymentResponse);
        when(productEngineGrpcClient.getProduct(any(ProductRequest.class))).thenReturn(productResponse);

        // when
        ProcessApplicationResponse response = stub.processApplication(request);

        // then
        assertThat(response.getStatus()).isEqualTo("CANCELED");
    }
}
