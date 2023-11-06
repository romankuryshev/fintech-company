package com.academy.fintech.pe.core.service.loan.cash_loan_agreement.db;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.loan.cash_loan.db.CashLoan;
import com.academy.fintech.pe.core.service.user.db.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "agreementId")
public class CashLoanAgreement extends Agreement {
    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "userId")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="cashLoanName", referencedColumnName="name"),
            @JoinColumn(name="cashLoanVersion", referencedColumnName="version")
    })
    private CashLoan cashLoan;

    @NotNull
    private double interest;

    @NotNull
    private int term;

    @NotNull
    private BigDecimal principalAmount;

    @NotNull
    private BigDecimal originationAmount;

    @NotNull
    private BigDecimal disbursementAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CashLoanAgreementStatus cashLoanAgreementStatus;

    @NotNull
    private LocalDate disbursementDate;
}
