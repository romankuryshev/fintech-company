package com.academy.fintech.dwh.core.agreement.db.agreement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementEventService {
    private final AgreementEventRepository agreementEventRepository;

    public void save(AgreementEvent agreementEvent) {
        agreementEventRepository.save(agreementEvent);
    }
}
