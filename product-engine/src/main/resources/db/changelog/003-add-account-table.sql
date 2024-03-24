CREATE TABLE IF NOT EXISTS account
(
    account_id   uuid PRIMARY KEY,
    agreement_id uuid    not null,
    balance      numeric not null,
    duty         numeric not null
);

CREATE INDEX IF NOT EXISTS agreement_id_idx ON account (agreement_id);