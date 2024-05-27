
CREATE SCHEMA IF NOT EXISTS sep2;
SET SCHEMA 'sep2';
DROP TABLE IF EXISTS userEvents CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create the users table
CREATE TABLE users
(
    userid         UUID         NOT NULL PRIMARY KEY,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password       VARCHAR(255) NOT NULL,
    creationdate   TIMESTAMPTZ  DEFAULT CURRENT_TIMESTAMP,
    firstname      VARCHAR(255),
    lastname       VARCHAR(255),
    dateofbirth    DATE,
    sex            VARCHAR(10),
    phonenumber    VARCHAR(20),
    profilePicture VARCHAR(255) DEFAULT 'unknown'
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

-- Change the owner of the events table
ALTER TABLE events OWNER TO postgres;

-- Create the userEvents table
CREATE TABLE userEvents (
    UserID UUID REFERENCES Users(UserID),
    EventID UUID REFERENCES Events(EventID),

    PRIMARY KEY (UserID, EventID)
);



-- Insert data into the users table
INSERT INTO users (userid, email, password, firstname, lastname, dateofbirth, sex, phonenumber, profilePicture)
VALUES
    ('ccde07db-cc2a-41bb-9090-e5f072e065d7', 'user1@example.com', 'Jq9UdF9BkZPaXbtsGuMBRA==:xwlbh2bswYx4DnRmwr+do4N0xlw9rqgHp85wNEvFFa4=', 'John', 'Doe', '1990-01-01', 'Male', '004072998568', 'profilePicture-ccde07db-cc2a-41bb-9090-e5f072e065d7'),
    (gen_random_uuid(), 'user2@example.com', 'ITmRaGEJs0YqIQkWFkQC6Q==:P8HHL4Zq9D2zSk0L87mGe/ldfQQ4MzUPzCM1dm2qfg4=', 'Alice', 'Smith', '1985-05-15', 'Female', '0040727606560', 'unknown'),
    (gen_random_uuid(), 'user3@example.com', 'tVkVy5fQi3mHlwCVZXpmqw==:lgeJGlbrjcnZHK9Kgoi15lApET6SNaZB2Dab0hqTg78=', 'Bob', 'Johnson', '1978-09-30', 'Other', '4525141231', 'unknown'),
    (gen_random_uuid(), 'user4@example.com', 'P80F9sw6F0s6Lual6pq1sw==:fx4NFf5jujAV1IhFIW18CCjVwplJBdYX0cRGCDWqP2I=', 'Emily', 'Brown', '2000-03-20', 'Female', '4229166701', 'unknown'),
    (gen_random_uuid(), 'user5@example.com', 'OB7t7KoyJ/xAVOgsRbKv6Q==:Zs36w47KXGRQTdan8pgxifCTH29YK/ca+IQfrKfjquI=', 'Michael', 'Davis', '1995-11-10', 'Male', '45241452', 'unknown');

-- Insert data into the events table
INSERT INTO events (eventid, title, description, starttime, endtime, ownerid, location)
VALUES
    (gen_random_uuid(), 'Event 1', 'Description of Event 1', '2024-05-10 10:00:00', '2024-05-10 12:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 2', 'Description of Event 2', '2024-05-11 13:00:00', '2024-05-11 15:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 3', 'Description of Event 3', '2024-05-12 09:00:00', '2024-05-12 11:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 4', 'Description of Event 4', '2024-05-13 14:00:00', '2024-05-13 16:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka'),
    (gen_random_uuid(), 'Event 5', 'Description of Event 5', '2024-05-14 11:00:00', '2024-05-14 13:00:00', 'ccde07db-cc2a-41bb-9090-e5f072e065d7', 'Horsens, Kamtjatka');