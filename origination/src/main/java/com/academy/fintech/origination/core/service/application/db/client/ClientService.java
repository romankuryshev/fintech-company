package com.academy.fintech.origination.core.service.application.db.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Nullable
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
