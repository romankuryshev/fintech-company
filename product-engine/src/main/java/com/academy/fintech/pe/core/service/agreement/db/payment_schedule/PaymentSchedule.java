package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPayment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @NotNull
    @Column(name = "version")
    private int version;

    @OneToMany(mappedBy = "paymentSchedule", fetch = FetchType.EAGER)
    private List<LoanPayment> payments;

    public PaymentSchedule(Agreement agreement, int version) {
        this.agreement = agreement;
        this.version = version;
    }
}
