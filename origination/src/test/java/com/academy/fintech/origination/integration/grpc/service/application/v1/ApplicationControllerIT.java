package com.academy.fintech.origination.integration.grpc.service.application.v1;

import com.academy.fintech.application.*;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
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
public class ApplicationControllerIT {

    @Container
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:14.1-alpine");

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ClientService clientService;

    @GrpcClient("testClient")
    private ApplicationServiceGrpc.ApplicationServiceBlockingStub client;

    @DynamicPropertySource
    static void postgresPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
        System.out.println(postgresSQLContainer.getJdbcUrl());
        System.out.println(postgresSQLContainer.getUsername());
        System.out.println(postgresSQLContainer.getPassword());
    }

    private Application createApplication(Client client) {
        return Application.builder()
                .client(client)
                .status(ApplicationStatus.NEW)
                .requestDisbursementAmount(BigDecimal.valueOf(200_000))
                .build();
    }

    @Test
    void givenValidRequest_whenCreateApplication_thenReturnApplicationId() {
        CreateRequest request = CreateRequest.newBuilder()
                .setFirstName("roman")
                .setLastName("kuryshev")
                .setEmail("roman@mail.ru")
                .setSalary(10_000)
                .setDisbursementAmount(200_000)
                .build();

        CreateResponse response = client.create(request);

        assertThat(response).isNotEqualTo("");
        Application application = applicationService.findById(UUID.fromString(response.getApplicationId()));
        assert application != null;
        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.NEW);
        assertThat(application.getRequestDisbursementAmount()).isEqualTo(BigDecimal.valueOf(200_000));
    }

    @Test
    void givenValidApplicationId_whenRemoveApplication_thenApplicationRemoved() {
        Client dbClient = Client.builder()
                .firstName("roman")
                .latName("kuryshev")
                .email("roman1@mail.ru")
                .salary(BigDecimal.valueOf(30000))
                .build();;
        clientService.save(dbClient);
        Application application = createApplication(dbClient);
        applicationService.save(application);
        RemoveRequest request = RemoveRequest.newBuilder()
                .setApplicationId(application.getId().toString())
                .build();

        RemoveResponse response = client.remove(request);

        assertThat(response.getSuccess()).isEqualTo(true);
        assertThat(applicationService.findById(application.getId())).isEqualTo(null);
    }

    @Test
    void givenInvalidRequest_whenCreateApplication_thenThrowsError() {
        Client dbClient = Client.builder()
                .firstName("roman")
                .latName("kuryshev")
                .email("roman2@mail.ru")
                .salary(BigDecimal.valueOf(30000))
                .build();;
        clientService.save(dbClient);
        Application application = createApplication(dbClient);
        applicationService.save(application);
        CreateRequest request = CreateRequest.newBuilder()
                .setFirstName("roman")
                .setLastName("kuryshev")
                .setEmail("roman2@mail.ru")
                .setSalary(10_000)
                .setDisbursementAmount(200_000)
                .build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> client.create(request));

        assertThat(exception.getStatus().getCode()).isEqualTo(Status.ALREADY_EXISTS.getCode());
        assert exception.getTrailers() != null;
        String actualId = exception.getTrailers().get(Metadata.Key.of("applicationid", Metadata.ASCII_STRING_MARSHALLER));
        assertThat(actualId).isEqualTo(application.getId().toString());
    }

    @Test
    void givenInvalidApplicationId_whenRemoveApplication_thenThrowsException() {
        RemoveRequest request = RemoveRequest.newBuilder()
                .setApplicationId("00000000-0000-0000-0000-000000000001")
                .build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> client.remove(request));

        assertThat(exception.getStatus().getCode()).isEqualTo(Status.CANCELLED.getCode());
    }
}
