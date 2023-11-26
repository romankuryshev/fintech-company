package com.academy.fintech.origination.unit.core.service.application.domain_service;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.domain_service.ApplicationOperationService;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationAlreadyExistsException;
import com.academy.fintech.origination.core.service.application.domain_service.exception.ApplicationDeleteException;
import com.academy.fintech.origination.grpc.service.application.v1.dto.CreateRequestDto;
import com.academy.fintech.origination.grpc.service.application.v1.dto.RemoveRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationOperationServiceTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationOperationService applicationOperationService;

    private static Stream<Arguments> provideStatusesForCreateApplication() {
        return Stream.of(
                Arguments.of(ApplicationStatus.ACCEPTED),
                Arguments.of(ApplicationStatus.ACTIVE),
                Arguments.of(ApplicationStatus.CLOSED),
                Arguments.of(ApplicationStatus.SCORING)
        );
    }

    private static Stream<Arguments> provideStatusesForDeleteApplication() {
        return Stream.of(
                Arguments.of(ApplicationStatus.ACCEPTED),
                Arguments.of(ApplicationStatus.ACTIVE),
                Arguments.of(ApplicationStatus.CLOSED)
        );
    }

    private Client createClient() {
        return Client.builder()
                .firstName("roman")
                .latName("kuryshev")
                .email("roman@mail.ru")
                .salary(BigDecimal.valueOf(100_000))
                .build();
    }

    @Test
    void givenUserWithoutApplications_whenCreateApplication_thenReturnNewApplication() {
        Client client = createClient();
        CreateRequestDto dto = new CreateRequestDto(BigDecimal.valueOf(100_000));
        Application expectedApplication = Application.builder()
                .status(ApplicationStatus.NEW)
                .client(client)
                .requestDisbursementAmount(dto.disbursementAmount())
                .build();

        Application application = applicationOperationService.createApplication(client, dto);

        assertThat(application).isEqualTo(expectedApplication);
    }

    @Test
    void givenUserWithApplication_whenCreateApplication_thenThrowException() {
        Client client = createClient();
        CreateRequestDto dto = new CreateRequestDto(BigDecimal.valueOf(100_000));
        Application existedApplication = Application.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .status(ApplicationStatus.NEW)
                .client(client)
                .requestDisbursementAmount(dto.disbursementAmount())
                .build();
        when(applicationService.findAllByClient(client)).thenReturn(List.of(existedApplication));

        ApplicationAlreadyExistsException exception = assertThrows(ApplicationAlreadyExistsException.class, () -> {
            applicationOperationService.createApplication(client, dto);
        });

        assertThat(exception.getApplicationId()).isEqualTo(existedApplication.getId());
    }

    @ParameterizedTest
    @MethodSource({"provideStatusesForCreateApplication"})
    void givenUserWithNotNewApplication_whenCreateApplication_thenReturnApplication(ApplicationStatus status) {
        Client client = createClient();
        CreateRequestDto dto = new CreateRequestDto(BigDecimal.valueOf(100_000));
        Application existedApplication = Application.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .status(status)
                .client(client)
                .requestDisbursementAmount(dto.disbursementAmount())
                .build();
        Application expectedApplication = Application.builder()
                .status(ApplicationStatus.NEW)
                .client(client)
                .requestDisbursementAmount(dto.disbursementAmount())
                .build();
        when(applicationService.findAllByClient(client)).thenReturn(List.of(existedApplication));

        Application application = applicationOperationService.createApplication(client, dto);

        assertThat(application).isEqualTo(expectedApplication);
    }

    @ParameterizedTest
    @MethodSource({"provideStatusesForDeleteApplication"})
    void givenExistedId_whenRemoveApplication_thenApplicationRemoved(ApplicationStatus status) {
        RemoveRequestDto dto = new RemoveRequestDto(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        Application application = Application.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .status(status)
                .requestDisbursementAmount(BigDecimal.valueOf(100_000))
                .build();
        when(applicationService.findById(dto.applicationId())).thenReturn(application);

        assertThrows(ApplicationDeleteException.class, () -> {
            applicationOperationService.removeApplication(dto);
        });
    }

    @Test
    void givenNotExistedId_whenRemoveApplication_thenThrowsException() {
        RemoveRequestDto dto = new RemoveRequestDto(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        when(applicationService.findById(dto.applicationId())).thenReturn(null);

        assertThrows(ApplicationDeleteException.class, () -> {
            applicationOperationService.removeApplication(dto);
        });
    }


}