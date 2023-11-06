package com.academy.fintech.pe.core.service.loan.loan_payment.db;

import com.academy.fintech.pe.core.service.loan.loan_schedule.db.LoanPaymentSchedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class LoanPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "loanPaymentScheduleVersion", referencedColumnName = "version"),
            @JoinColumn(name = "agreementId", referencedColumnName = "agreementId")
    })
    private LoanPaymentSchedule LoanPaymentSchedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LoanPaymentStatus loanPaymentStatus;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate date;
}
