-- V1__Create_Ticket_Table.sql

create table if not exists public.users
(
    user_id      bigint generated by default as identity primary key,
    created_at   timestamp(6) not null,
    updated_at   timestamp(6) not null,
    email        varchar(255) not null unique,
    isoauth2user boolean default false,
    password     varchar(255) not null,
    username     varchar(255)
);


-- Create the Ticket table
CREATE TABLE IF NOT EXISTS public.tickets
(
    id      SERIAL PRIMARY KEY,
    concert VARCHAR(255)   NOT NULL,
    date    DATE           NOT NULL,
    venue   VARCHAR(255)   NOT NULL,
    price   NUMERIC(10, 2) NOT NULL,
    status  VARCHAR(50)    NOT NULL,
    user_id BIGINT REFERENCES users (user_id)
);
-- Insert some initial data
INSERT INTO ticket (concert, date, venue, price, status)
VALUES ('Concert A', '2024-09-01', 'Venue A', 50.00, 'AVAILABLE'),
       ('Concert B', '2024-09-05', 'Venue B', 60.00, 'AVAILABLE'),
       ('Concert C', '2024-09-10', 'Venue C', 70.00, 'AVAILABLE');
