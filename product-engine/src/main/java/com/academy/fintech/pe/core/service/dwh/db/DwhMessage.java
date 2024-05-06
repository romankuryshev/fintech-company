package com.academy.fintech.pe.core.service.dwh.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DwhMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RetryStatus status;

    @Column(name = "counter")
    private int counter;

    @Column(name = "inserted")
    private LocalDateTime inserted;

    public void incrementCounter() {
        counter += 1;
    }
}
