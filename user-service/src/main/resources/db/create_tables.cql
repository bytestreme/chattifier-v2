CREATE TABLE users
(
    userId   uuid,
    time     timestamp,
    username text,
    roles    list<text>,
    password text,
    room     list<text>,
    primary key ( userId )
);

CREATE INDEX nameIndexForUsers ON users (username);

CREATE TABLE rooms
(
    roomId uuid,
    name   text,
    type   int,
    primary key ( roomId )
);

CREATE TABLE participant
(
    userId uuid,
    roomId uuid,
    primary key (userId, roomId)
);

CREATE TABLE messages
(
    messageId uuid,
    time      timestamp,
    sender    uuid,
    receiver  uuid,
    room      uuid,
    status    tinyint, //-1 deleted 0 send, 1 received, 2 read
    message   text,
    PRIMARY KEY (sender, receiver, room)
) WITH CLUSTERING ORDER BY (time ASC);