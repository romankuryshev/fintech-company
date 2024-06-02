package com.academy.fintech.pe.integration.grpc.service.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementCreationServiceGrpc;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        properties = {
        "grpc.server.inProcessName=test",
        "grpc.server.port=-1",
        "grpc.client.testClient.address=in-process:test"
        }
)
@Testcontainers
@DirtiesContext
class AgreementControllerIT {
    @Container
    static PostgreSQLContainer<?> postgresSQLContainer =
            new PostgreSQLContainer<>("postgres:14.1-alpine");
    @Autowired
    private AgreementRepository agreementRepository;
    @GrpcClient("testClient")
    private AgreementCreationServiceGrpc.AgreementCreationServiceBlockingStub client;
    @DynamicPropertySource
    static void postgresPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @Test
    void givenValidRequest_whenCreate_thenReturnMessageOk() {
        AgreementRequest request = AgreementRequest.newBuilder()
                .setProductCode("CL1.0")
                .setClientId("00000000-0000-0000-0000-000000000001")
                .setInterest("13")
                .setTerm(12)
                .setDisbursementAmount("50000")
                .setOriginationAmount("2000")
                .build();

        AgreementResponse response = client.create(request);

        Agreement savedAgreement = agreementRepository.findById(UUID.fromString(response.getId())).orElse(null);
        assertThat(savedAgreement).isNotNull();
        assertThat(savedAgreement.getProduct().getCode()).isEqualTo("CL1.0");
        assertThat(savedAgreement.getClientId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThat(savedAgreement.getInterest()).isEqualTo("13");
        assertThat(savedAgreement.getTermInMonths()).isEqualTo(12);
        assertThat(savedAgreement.getOriginationAmount()).isEqualTo("2000");
        assertThat(savedAgreement.getPrincipalAmount()).isEqualTo("52000");
    }

    @Test
    void givenInvalidRequest_whenCreate_thenReturnMessageError() {
        AgreementRequest request = AgreementRequest.newBuilder()
                // invalid product code
                .setProductCode("CL2.0")
                .setClientId("00000000-0000-0000-0000-000000000001")
                .setInterest("13")
                .setTerm(12)
                .setDisbursementAmount("50000")
                .setOriginationAmount("2000")
                .build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> client.create(request));
        assertThat(exception.getStatus().getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
    }
}