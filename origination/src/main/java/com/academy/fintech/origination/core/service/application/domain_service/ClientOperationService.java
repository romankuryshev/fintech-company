package com.academy.fintech.origination.core.service.application.domain_service;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.grpc.service.application.v1.dto.ClientDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientOperationService {

    private final ClientService clientService;

    public Client findOrCreateClient(ClientDto dto) {
        Client client = clientService.findByEmail(dto.email());
        if (client != null)
            return client;

        client = Client.builder()
                .firstName(dto.firstName())
                .latName(dto.lastName())
                .email(dto.email())
                .salary(dto.salary())
                .build();

        clientService.save(client);
        return client;
    }
}
