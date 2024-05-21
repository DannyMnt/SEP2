-- Drop the tables if they exist
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create the users table
CREATE TABLE users
(
    userid       UUID         NOT NULL PRIMARY KEY,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    creationdate TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    firstname    VARCHAR(255),
    lastname     VARCHAR(255),
    dateofbirth  DATE,
    sex          VARCHAR(10),
    phonenumber  VARCHAR(20)
);

-- Change the owner of the users table
ALTER TABLE users OWNER TO postgres;

-- Create the events table
CREATE TABLE events
(
    eventid     UUID                     NOT NULL PRIMARY KEY,
    title       VARCHAR(255)             NOT NULL,
    description TEXT,
    starttime   TIMESTAMPTZ              NOT NULL,
    endtime     TIMESTAMPTZ              NOT NULL,
    ownerid     UUID CONSTRAINT events_organizer_fkey REFERENCES users(userid) ON DELETE CASCADE,
    location    VARCHAR(255)
);

CREATE TABLE userEvents (
    UserID UUID REFERENCES Users(UserID),
    EventID UUID REFERENCES Events(EventID),
    
    PRIMARY KEY (UserID, EventID) 
);


-- Change the owner of the events table
ALTER TABLE events OWNER TO postgres;

-- Insert data into the users table
INSERT INTO users (userid, email, password, firstname, lastname, dateofbirth, sex, phonenumber)
VALUES
    ('ccde07db-cc2a-41bb-9090-e5f072e065d7', 'user1@example.com', 'password1', 'John', 'Doe', '1990-01-01', 'Male', '004072998568'),
    (gen_random_uuid(), 'user2@example.com', 'password2', 'Alice', 'Smith', '1985-05-15', 'Female', '0040727606560'),
    (gen_random_uuid(), 'user3@example.com', 'password3', 'Bob', 'Johnson', '1978-09-30', 'Other', '4525141231'),
    (gen_random_uuid(), 'user4@example.com', 'password4', 'Emily', 'Brown', '2000-03-20', 'Female', '4229166701'),
    (gen_random_uuid(), 'user5@example.com', 'password5', 'Michael', 'Davis', '1995-11-10', 'Male', '45241452');

-- Insert data into the events table
INSERT INTO events (eventid, title, description, starttime, endtime, ownerid, location)
VALUES
    (gen_random_uuid(), 'Event 1', 'Description of Event 1', '2024-05-10 10:00:00', '2024-05-10 12:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 2', 'Description of Event 2', '2024-05-11 13:00:00', '2024-05-11 15:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 3', 'Description of Event 3', '2024-05-12 09:00:00', '2024-05-12 11:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 4', 'Description of Event 4', '2024-05-13 14:00:00', '2024-05-13 16:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 5', 'Description of Event 5', '2024-05-14 11:00:00', '2024-05-14 13:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka');
