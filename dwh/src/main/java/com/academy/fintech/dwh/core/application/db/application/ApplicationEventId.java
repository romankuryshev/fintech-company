package com.academy.fintech.dwh.core.application.db.application;

import java.io.Serializable;
import java.time.LocalDate;

public class ApplicationEventId implements Serializable {
    private Long eventId;
    private LocalDate eventDate;
}
