-- V1__Create_Ticket_Table.sql
create table if not exists public.concerts (
    id              uuid             not null primary key,
    name            varchar(100)     not null,
    ticket_price    double precision not null,
    ticket_capacity bigint           not null,
    date            TIMESTAMP     not null,
    created_at      TIMESTAMP     not null,
    updated_at      TIMESTAMP     not null
);

INSERT INTO public.concerts (id, ticket_price, created_at, date, ticket_capacity, updated_at, name)
VALUES ('9b522efc-6703-4126-815d-a2d4028bbafe', 50.00, '2024-06-01 12:00:00', '2024-10-01 12:45:00', 100, '2024-06-01 12:00:00', 'Concert A'),
       ('aaa40ce1-9ce8-4347-8976-7c9d3a53c4c1', 60.00, '2024-06-01 12:00:00', '2024-11-05 12:45:00', 100, '2024-07-05 12:00:00', 'Concert B'),
       ('70bf8df0-ba1b-4691-9c97-7c678b67d098', 70.00, '2024-06-01 12:00:00', '2024-12-10 12:45:00', 100, '2024-06-10 12:00:00', 'Concert C');

create table if not exists public.users (
    id            uuid         not null primary key,
    email         varchar(100) not null unique,
    username      varchar(50),
    password_hash char(60), -- bcrypt hash
    isoauth2user  boolean default false,
    created_at    TIMESTAMP not null,
    updated_at    TIMESTAMP not null
);

create table if not exists public.tickets (
    id         uuid      not null primary key,
    concert_id uuid references concerts (id),
    user_id    uuid references users (id),
    status     varchar(20),
    created_at TIMESTAMP not null,
    updated_at TIMESTAMP not null
);
