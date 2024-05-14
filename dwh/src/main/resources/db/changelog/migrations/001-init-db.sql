CREATE TABLE IF NOT EXISTS agreement_event
(
    event_id           bigserial not null,
    agreement_id       uuid,
    product_code       varchar(10),
    client_id          uuid,
    interest           numeric,
    term_in_months     int,
    principal_amount   numeric,
    origination_amount numeric,
    status             varchar(20),
    disbursement_date  timestamp,
    next_payment_date  timestamp,
    event_date_time    timestamp,
    event_date         date      not null,

    CONSTRAINT agreement_event_pk PRIMARY KEY (event_id, event_date)
) PARTITION BY RANGE (event_date);

CREATE TABLE IF NOT EXISTS application_event
(
    event_id                    bigserial not null,
    application_id              uuid,
    client_id                   uuid,
    request_disbursement_amount numeric,
    status                      varchar(20),
    agreement_id                uuid,
    event_date_time             timestamp,
    event_date                  timestamp not null ,
    CONSTRAINT application_event_pk PRIMARY KEY (event_id, event_date)
) PARTITION BY RANGE (event_date);