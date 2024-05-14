CREATE TABLE IF NOT EXISTS dwh_message
(
    id       bigserial PRIMARY KEY,
    key      uuid        not null,
    message  text        not null,
    status   varchar(20) not null,
    inserted timestamp   not null,
    counter  int         not null default 0
)