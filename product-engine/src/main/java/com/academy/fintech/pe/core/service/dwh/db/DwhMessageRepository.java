package com.academy.fintech.pe.core.service.dwh.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DwhMessageRepository extends JpaRepository<DwhMessage, Long> {
    List<DwhMessage> findAllByStatus(RetryStatus status);
}
