package com.academy.fintech.origination.core.service.application.db.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Nullable
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    @Nullable
    public Client findById(UUID clientID) {
        return clientRepository.findById(clientID).orElse(null);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
