package com.academy.fintech.pe.core.service.agreement.db.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @NotNull
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "min_term")
    private int minTerm;

    @NotNull
    @Column(name = "max_term")
    private int maxTerm;

    @NotNull
    @Column(name = "min_principal_amount")
    private BigDecimal minPrincipalAmount;

    @NotNull
    @Column(name = "max_principal_amount")
    private BigDecimal maxPrincipalAmount;

    @NotNull
    @Column(name = "min_interest")
    private BigDecimal minInterest;

    @NotNull
    @Column(name = "max_interest")
    private BigDecimal maxInterest;

    @NotNull
    @Column(name = "min_origination_amount")
    private BigDecimal minOriginationAmount;

    @NotNull
    @Column(name = "max_origination_amount")
    private BigDecimal maxOriginationAmount;
}
