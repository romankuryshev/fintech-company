package com.academy.fintech.origination.unit.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationStatus;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void givenActiveClient_whenFindAllByClient_thenReturnApplications() {
        Client client = Client.builder()
                .firstName("roman")
                .latName("kuryshev")
                .email("roman@mail.ru")
                .salary(BigDecimal.valueOf(30000))
                .build();
        Application application1 = Application.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .client(client)
                .status(ApplicationStatus.NEW)
                .requestDisbursementAmount(BigDecimal.valueOf(100_000))
                .build();
        Application application2 = Application.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .client(client)
                .status(ApplicationStatus.ACCEPTED)
                .requestDisbursementAmount(BigDecimal.valueOf(50_000))
                .build();
        when(applicationRepository.findAllByClient(client)).thenReturn(List.of(application1, application2));

        List<Application> applications = applicationService.findAllByClient(client);

        assertThat(applications).isEqualTo(List.of(application1, application2));
    }
}