package com.academy.fintech.pe.core.service.loan.cash_loan.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@IdClass(CashLoanId.class)
public class CashLoan {
    @NotNull
    @Id
    private String name;

    @NotNull
    @Id
    private String version;

    @NotNull
    private int minTerm;

    @NotNull
    private int maxTerm;

    @NotNull
    private BigDecimal min_principal_amount;

    @NotNull
    private BigDecimal max_principal_amount;

    @NotNull
    private double min_interest;

    @NotNull
    private double max_interest;

    @NotNull
    private BigDecimal min_origination_amount;

    @NotNull
    private BigDecimal max_origination_amount;
}
