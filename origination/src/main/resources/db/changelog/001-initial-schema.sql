CREATE TABLE client
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(30) NOT NULL,
    email      VARCHAR(40) NOT NULL UNIQUE,
    salary     NUMERIC     NOT NULL
);

CREATE TABLE application
(
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id                   UUID        NOT NULL,
    request_disbursement_amount NUMERIC     NOT NULL,
    status                      VARCHAR(20) NOT NULL,

    CONSTRAINT client_fk
        FOREIGN KEY (client_id) REFERENCES client (id),

    CONSTRAINT status_check
        CHECK ( status in ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED'))
)