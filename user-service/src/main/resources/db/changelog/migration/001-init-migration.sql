CREATE TABLE IF NOT EXISTS user_role
(
    id   bigserial primary key,
    name varchar(30)
);

CREATE TABLE IF NOT EXISTS client
(
    id         uuid primary key,
    username   varchar(50),
    password   varchar(100),
    first_name varchar(50),
    last_name  varchar(50),
    salary     numeric,
    email      varchar(50),
    role_id    bigint,
    FOREIGN KEY (role_id) REFERENCES user_role (id)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id      bigserial primary key,
    user_id uuid not null,
    token   text not null,
    FOREIGN KEY (user_id) REFERENCES client (id)
);