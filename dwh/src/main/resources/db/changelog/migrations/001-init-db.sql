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

select partman.create_parent(
       p_parent_table := 'public.agreement_event',
       p_control := 'event_date',
       p_type := 'native',
       p_interval := 'P1M',
       p_start_partition := '2024-05-13 00:00:00'::text,
       p_premake := 12
       );

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

select partman.create_parent(
               p_parent_table := 'public.application_event',
               p_control := 'event_date',
               p_type := 'native',
               p_interval := 'P1M',
               p_start_partition := '2024-05-13 00:00:00'::text,
               p_premake := 12
       );

update partman.part_config
set  infinite_time_partitions = true
where parent_table in ('agreement_event', 'application_event');
