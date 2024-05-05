package com.academy.fintech.origination.core.service.application.db.dwh.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private long id;

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
