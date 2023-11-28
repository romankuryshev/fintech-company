package com.academy.fintech.pe.integration.grpc.service.agreement.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductRepository;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleCreationServiceGrpc;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleRequest;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.PaymentScheduleResponse;
import com.academy.fintech.pe.grpc.service.agreement.payment_schedule.dto.PaymentScheduleRequestDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        properties = {
                "grpc.server.inProcessName=test",
                "grpc.server.port=-1",
                "grpc.client.testClient.address=in-process:test"
        }
)
@Testcontainers
@DirtiesContext
public class PaymentScheduleIControllerIT {

    @Container
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine");

    @GrpcClient("testClient")
    private PaymentScheduleCreationServiceGrpc.PaymentScheduleCreationServiceBlockingStub client;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private  AgreementRepository agreementRepository;

    @Autowired
    private PaymentScheduleRepository scheduleRepository;

    @DynamicPropertySource
    static void postgresPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @Test
    void givenValidRequest_whenCreateSchedule_thenScheduleCreated() {
        Agreement agreement = createAgreement();
        agreementRepository.save(agreement);
        var dto = PaymentScheduleRequest.newBuilder()
                .setAgreementId(agreement.getId().toString())
                .setDisbursementDate("2024-02-29")
                .build();

        PaymentScheduleResponse response = client.createSchedule(dto);

        PaymentSchedule schedule = scheduleRepository.findByAgreementIdAndVersion(UUID.fromString(response.getAgreementId()), response.getVersion()).orElse(null);
        assertThat(schedule).isNotNull();
        assertThat(schedule.getPayments()).isNotNull();
        assertThat(schedule.getPayments().isEmpty()).isNotEqualTo(true);
    }

    private Agreement createAgreement() {
        Product product = productRepository.findByCode("CL1.0");;
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
}
