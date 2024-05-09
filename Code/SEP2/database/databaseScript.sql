drop table users cascade;
drop table events cascade;

create table users
(
    userid       uuid         not null
        primary key,
    email        varchar(255) not null
        unique,
    password     varchar(255) not null,
    creationdate timestamp with time zone default CURRENT_TIMESTAMP,
    firstname    varchar(255),
    lastname     varchar(255),
    dateofbirth  date,
    sex          char,
    phonenumber  varchar(20)
);

alter table sep2.users
    owner to postgres;

create table events
(
    eventid     uuid                     not null
        primary key,
    title       varchar(255)             not null,
    description text,
    starttime   timestamp with time zone not null,
    endtime     timestamp with time zone not null,
    ownerid     uuid
        constraint events_organizer_fkey
            references sep2.users
            on delete cascade
);

alter table events
    owner to postgres;




-- Insert data into the users table

    INSERT INTO users (userid, email, password, firstname, lastname, dateofbirth, sex, phonenumber)
VALUES
    ('ccde07db-cc2a-41bb-9090-e5f072e065d7', 'user1@example.com', 'password1', 'John', 'Doe', '1990-01-01', 'M', '1234567890'),
    (gen_random_uuid(), 'user2@example.com', 'password2', 'Alice', 'Smith', '1985-05-15', 'F', '9876543210'),
    (gen_random_uuid(), 'user3@example.com', 'password3', 'Bob', 'Johnson', '1978-09-30', 'M', '5551234567'),
    (gen_random_uuid(), 'user4@example.com', 'password4', 'Emily', 'Brown', '2000-03-20', 'F', '7779876543'),
    (gen_random_uuid(), 'user5@example.com', 'password5', 'Michael', 'Davis', '1995-11-10', 'M', '1112223333');



-- Insert data into the events table
INSERT INTO events (eventid, title, description, starttime, endtime, ownerid)
VALUES
    (gen_random_uuid(), 'Event 1', 'Description of Event 1', '2024-05-10 10:00:00', '2024-05-10 12:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7'),
    (gen_random_uuid(), 'Event 2', 'Description of Event 2', '2024-05-11 13:00:00', '2024-05-11 15:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7'),
    (gen_random_uuid(), 'Event 3', 'Description of Event 3', '2024-05-12 09:00:00', '2024-05-12 11:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7'),
    (gen_random_uuid(), 'Event 4', 'Description of Event 4', '2024-05-13 14:00:00', '2024-05-13 16:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7'),
    (gen_random_uuid(), 'Event 5', 'Description of Event 5', '2024-05-14 11:00:00', '2024-05-14 13:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7');