package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "agreement")
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "code")
    private Product product;

    @NotNull
    @Column(name = "client_id")
    private UUID clientId;

    @NotNull
    @Column(name = "interest")
    private BigDecimal interest;

    @NotNull
    @Column(name = "term")
    private int term;

    @NotNull
    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    @NotNull
    @Column(name = "origination_amount")
    private BigDecimal originationAmount;

    @NotNull
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private AgreementStatus status;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;
}
