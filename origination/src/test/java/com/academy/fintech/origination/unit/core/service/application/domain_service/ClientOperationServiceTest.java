package com.academy.fintech.origination.unit.core.service.application.domain_service;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.domain_service.ClientOperationService;
import com.academy.fintech.origination.grpc.service.application.v1.dto.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientOperationServiceTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientOperationService clientOperationService;

    private Client createClient() {
        return Client.builder()
                .firstName("roman")
                .latName("kuryshev")
                .email("roman@mail.ru")
                .salary(BigDecimal.valueOf(30_000))
                .build();
    }

    @Test
    void givenNewClient_whenFindOrCreateClient_thenReturnNewClient() {
        ClientDto dto = new ClientDto("roman", "kuryshev", "roman@mail.ru", BigDecimal.valueOf(30_000));
        Client expectedClient = createClient();
        when(clientService.findByEmail(dto.email())).thenReturn(null);

        Client actualClient = clientOperationService.findOrCreateClient(dto);

        assertThat(actualClient.getFirstName()).isEqualTo("roman");
        assertThat(actualClient.getLatName()).isEqualTo("kuryshev");
        assertThat(actualClient.getEmail()).isEqualTo("roman@mail.ru");
        assertThat(actualClient.getSalary()).isEqualTo(BigDecimal.valueOf(30_000));
    }

    @Test
    void givenClientWithExistedEmail_whenFindOrCreateClient_thenReturnClientFromDb() {
        ClientDto dto = new ClientDto("ivan", "ivanov", "roman@mail.ru", BigDecimal.valueOf(100_000));;
        Client existedClient = createClient();
        when(clientService.findByEmail(dto.email())).thenReturn(existedClient);

        Client actualClient = clientOperationService.findOrCreateClient(dto);

        assertThat(actualClient.getEmail()).isEqualTo(dto.email());
        assertThat(actualClient.getFirstName()).isNotEqualTo(dto.firstName());
        assertThat(actualClient.getLatName()).isNotEqualTo(dto.lastName());
    }


}