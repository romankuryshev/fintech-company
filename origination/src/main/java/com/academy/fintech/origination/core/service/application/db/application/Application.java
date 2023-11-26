package com.academy.fintech.origination.core.service.application.db.application;


import com.academy.fintech.origination.core.service.application.db.client.Client;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "application")
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "request_disbursement_amount")
    private BigDecimal requestDisbursementAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
