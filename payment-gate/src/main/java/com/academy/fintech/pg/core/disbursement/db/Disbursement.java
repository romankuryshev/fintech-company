package com.academy.fintech.pg.core.disbursement.db;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "agreement_id")
    private UUID agreementId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DisbursementStatus status;

    @Column(name = "check_count")
    private int checkCount;

    @Column(name = "next_check_date")
    private LocalDateTime nextCheckDate;

    public int incrementCheckCount() {
        checkCount += 1;
        return checkCount;
    }
}
