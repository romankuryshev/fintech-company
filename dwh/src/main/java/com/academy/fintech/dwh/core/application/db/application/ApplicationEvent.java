package com.academy.fintech.dwh.core.application.db.application;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
@IdClass(ApplicationEventId.class)
@Table(name ="application_event")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEvent {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "application_id")
    private UUID applicationId;

    @JoinColumn(name = "client_id")
    private UUID clientId;

    @Column(name = "request_disbursement_amount")
    private BigDecimal requestDisbursementAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "agreement_id")
    private UUID agreementId;

    @Column(name = "event_date_time")
    private LocalDateTime eventDateTime;
}
