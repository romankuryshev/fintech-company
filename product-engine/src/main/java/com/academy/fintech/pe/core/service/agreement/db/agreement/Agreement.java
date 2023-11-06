package com.academy.fintech.pe.core.service.agreement.db.agreement;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int agreementId;

    @Enumerated(EnumType.STRING)
    private AgreementType agreementTypeId;
}
