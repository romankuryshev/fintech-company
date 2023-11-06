package com.academy.fintech.pe.core.service.loan.cash_loan.db;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
//@EqualsAndHashCode
public class CashLoanId implements Serializable {
    private String name;
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashLoanId that = (CashLoanId) o;
        return Objects.equals(name, that.name) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
