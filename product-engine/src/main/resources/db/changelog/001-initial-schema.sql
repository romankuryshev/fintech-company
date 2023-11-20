CREATE TABLE product
(
    code                   varchar(30) not null PRIMARY KEY,
    min_term               smallint    not null,
    max_term               smallint    not null,
    min_interest           numeric     not null,
    max_interest           numeric     not null,
    min_principal_amount   numeric     not null,
    max_principal_amount   numeric     not null,
    min_origination_amount numeric     not null,
    max_origination_amount numeric     not null
);

CREATE TABLE agreement
(
    id                 uuid        not null PRIMARY KEY,
    product_code       varchar(30) not null,
    client_id          uuid        not null,
    interest           numeric     not null,
    term               serial      not null,
    principal_amount   numeric     not null,
    origination_amount numeric     not null,
    status             varchar(30) not null check (status in ('NEW', 'ACTIVE', 'CLOSED')),
    disbursement_date  date,
    next_payment_date  date,

    CONSTRAINT product_code_fk FOREIGN KEY (product_code)
        REFERENCES product (code)
);

CREATE TABLE payment_schedule
(
    id           bigserial not null PRIMARY KEY,
    agreement_id uuid      not null,
    version      int       not null,

    CONSTRAINT agreement_fk FOREIGN KEY (agreement_id)
        REFERENCES agreement (id),

    CONSTRAINT unique_schedule UNIQUE (agreement_id, version)
);

CREATE TABLE payment_schedule_payment
(
    paymentId           bigserial   not null PRIMARY KEY,
    payment_schedule_id bigserial   not null,
    status              varchar(30) not null check ( status in ('PAID', 'OVERDUE', 'FUTURE')),
    payment_date        date        not null,
    period_payment      numeric     not null,
    interest_payment    numeric     not null,
    principal_payment   numeric     not null,
    period_number       int         not null,

    CONSTRAINT payment_schedule_pk FOREIGN KEY (payment_schedule_id)
        REFERENCES payment_schedule (id)
);