package com.academy.fintech.dwh.core.agreement.db.agreement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AgreementEventId implements Serializable {
    private Long eventId;
    private LocalDate eventDate;
}
