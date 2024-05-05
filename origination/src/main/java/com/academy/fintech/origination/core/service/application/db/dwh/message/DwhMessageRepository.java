package com.academy.fintech.origination.core.service.application.db.dwh.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DwhMessageRepository extends JpaRepository<DwhMessage, Long> {
    List<DwhMessage> findAllByStatus(RetryStatus status);
}
