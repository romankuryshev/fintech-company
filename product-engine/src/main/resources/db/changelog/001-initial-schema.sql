CREATE TABLE user_info
(
    user_id    int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name varchar(30) not null,
    last_name  varchar(30) not null,
    email      varchar(30) not null,
    income     numeric     not null
);

CREATE TABLE agreement_type
(
    agreement_type_id   int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    agreement_type_name varchar(30) not null
);

CREATE TABLE agreement
(
    agreement_id      int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    agreement_type_id int not null,

    CONSTRAINT agreement_type_fk FOREIGN KEY (agreement_type_id)
        REFERENCES agreement_type (agreement_type_id)
);

CREATE TABLE cash_loan
(
    name                   varchar(20) not null,
    version                varchar(10) not null,
    min_term               smallint    not null,
    max_term               smallint    not null,
    min_interest           real        not null,
    max_interest           real        not null,
    min_principal_amount   numeric     not null,
    max_principal_amount   numeric     not null,
    min_origination_amount numeric     not null,
    max_origination_amount numeric     not null,

    CONSTRAINT cash_loan_pk PRIMARY KEY (name, version)
);

CREATE TABLE loan_payment_status
(
    payment_status_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              varchar(30)
);

CREATE TABLE cash_loan_status
(
    cash_loan_status_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                varchar(30)
);

CREATE TABLE cash_loan_agreement
(
    agreement_id                  int PRIMARY KEY,
    cash_loan_name                varchar(20) not null,
    cash_loan_version             varchar(10) not null,
    user_id                       int         not null,
    agreement_number              int         not null UNIQUE,
    term                          int         not null,
    status_id                     int         not null,
    loan_payment_schedule_version int         not null,
    interest                      real        not null,
    principal_amount              numeric     not null,
    origination_amount            numeric     not null,
    disbursement_amount           numeric     not null,
    disbursement_date             date        not null,

    CONSTRAINT agreement_fk FOREIGN KEY (agreement_id)
        REFERENCES agreement (agreement_id),

    CONSTRAINT user_fk FOREIGN KEY (user_id)
        REFERENCES user_info (user_id),

    CONSTRAINT status_fk FOREIGN KEY (status_id)
        REFERENCES cash_loan_status (cash_loan_status_id),

    CONSTRAINT cash_loan_fk FOREIGN KEY (cash_loan_name, cash_loan_version)
        REFERENCES cash_loan (name, version)
);

CREATE TABLE loan_payment_schedule
(
    version      int not null,
    agreement_id int not null,

    CONSTRAINT loam_payment_schedule_pk PRIMARY KEY (version, agreement_id),

    CONSTRAINT cash_loan_agreement_fk FOREIGN KEY (agreement_id)
        REFERENCES cash_loan_agreement (agreement_id)
);


CREATE TABLE loan_payment
(
    loan_payment_id               int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    loan_payment_schedule_version int     not null,
    agreement_id                  int     not null,
    status_id                     int     not null,
    payment_date                  date    not null,
    amount                        numeric not null,

    CONSTRAINT status_fk FOREIGN KEY (status_id)
        REFERENCES loan_payment_status (payment_status_id),

    CONSTRAINT loan_payment_schedule_fk FOREIGN KEY (loan_payment_schedule_version, agreement_id)
        REFERENCES loan_payment_schedule (version, agreement_id)
);