package com.academy.fintech.origination.core.service.application.db.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
