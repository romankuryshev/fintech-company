package com.academy.fintech.pe.core.service.loan.loan_schedule.db;

import com.academy.fintech.pe.core.service.loan.cash_loan_agreement.db.CashLoanAgreement;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Data
public class LoanPaymentScheduleId implements Serializable {
    private int version;
    private CashLoanAgreement cashLoanAgreement;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanPaymentScheduleId that = (LoanPaymentScheduleId) o;
        return version == that.version && Objects.equals(cashLoanAgreement, that.cashLoanAgreement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, cashLoanAgreement);
    }
}
