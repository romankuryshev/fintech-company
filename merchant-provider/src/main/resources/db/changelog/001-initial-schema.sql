CREATE TABLE disbursement
(
    id           bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    agreement_id uuid      NOT NULL UNIQUE,
    amount       numeric   NOT NULL,
    date         timestamp NOT NULL,
    status       varchar(15) CHECK ( status IN ('COMPLETED', 'AWAITS'))
);