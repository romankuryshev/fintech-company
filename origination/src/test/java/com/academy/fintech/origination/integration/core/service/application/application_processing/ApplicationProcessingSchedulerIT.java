package com.academy.fintech.origination.integration.core.service.application.application_processing;

import com.academy.fintech.origination.core.scoring.client.grpc.ScoringGrpcClient;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientRepository;
import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import com.academy.fintech.scoring.application_processing.ProcessApplicationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class ApplicationProcessingSchedulerIT {

    private static final int schedulerTimeout = 11000;

    @Container
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine");

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @MockBean
    private ScoringGrpcClient scoringGrpcClient;

    @DynamicPropertySource
    static void postgresPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @Test
    void givenNewApplication_whenSchedulerStarts_thenApplicationStatusChanging() {
        Client client = Client.builder()
                .firstName("roman")
                .latName("kuryshev")
                .email("roman@mail.ru")
                .salary(BigDecimal.valueOf(30000))
                .build();
        Application application = Application.builder()
                .client(client)
                .status(ApplicationStatus.NEW)
                .requestDisbursementAmount(BigDecimal.valueOf(100_000))
                .build();
        ProcessApplicationResponse response = ProcessApplicationResponse.newBuilder()
                .setStatus("ACCEPTED")
                .build();
        clientRepository.save(client);
        applicationRepository.save(application);
        UUID applicationId = application.getId();

        when(scoringGrpcClient.processApplication(any(ProcessApplicationRequest.class))).thenReturn(response);

        try {
            Thread.sleep(schedulerTimeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertThat(applicationRepository.findById(applicationId).get().getStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
    }
}
