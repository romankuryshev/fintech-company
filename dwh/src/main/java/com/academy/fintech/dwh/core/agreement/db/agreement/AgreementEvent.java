package com.academy.fintech.dwh.core.agreement.db.agreement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "agreement_event")
@NoArgsConstructor
@AllArgsConstructor
public class AgreementEvent {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "agreement_id")
    private UUID agreementId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "interest")
    private BigDecimal interest;

    @Column(name = "term_in_months")
    private int termInMonths;

    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    @Column(name = "origination_amount")
    private BigDecimal originationAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;

    @Column(name = "event_date_time")
    private LocalDateTime eventDateTime;

    @Column(name = "event_date")
    private LocalDate eventDate;
}
