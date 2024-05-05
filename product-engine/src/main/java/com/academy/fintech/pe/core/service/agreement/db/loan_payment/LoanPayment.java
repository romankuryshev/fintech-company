package com.academy.fintech.pe.core.service.agreement.db.loan_payment;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_schedule_payment")
public class LoanPayment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "payment_schedule_id")
    private PaymentSchedule paymentSchedule;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LoanPaymentStatus status;

    @NotNull
    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "period_payment")
    private BigDecimal periodPayment;

    @NotNull
    @Column(name = "interest_payment")
    private BigDecimal interestPayment;

    @NotNull
    @Column(name = "principal_payment")
    private BigDecimal principalPayment;

    @NotNull
    @Column(name = "period_number")
    private int periodNumber;

    public BigDecimal getPaymentAmount() {
        return interestPayment.add(periodPayment.add(principalPayment));
    }
}
