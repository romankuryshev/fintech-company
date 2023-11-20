package com.academy.fintech.pe.core.service.agreement.db.loan_payment;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentSchedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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
}
