package com.academy.fintech.pe.core.service.account.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Account findByAgreementId(UUID agreementId);
}
