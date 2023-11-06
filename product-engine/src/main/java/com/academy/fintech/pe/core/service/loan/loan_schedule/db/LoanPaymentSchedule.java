package com.academy.fintech.pe.core.service.loan.loan_schedule.db;

import com.academy.fintech.pe.core.service.loan.cash_loan_agreement.db.CashLoanAgreement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@IdClass(LoanPaymentScheduleId.class)
public class LoanPaymentSchedule {
    @Id
    @NotNull
    private int version;

    @Id
    @NotNull
    @ManyToOne
    @JoinColumn(name = "agreementId", referencedColumnName = "agreementId")
    private CashLoanAgreement cashLoanAgreement;
}
