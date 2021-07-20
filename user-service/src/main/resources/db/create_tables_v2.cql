CREATE KEYSPACE IF NOT EXISTS chattifier WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

-- CREATE TABLE IF NOT EXISTS
--     chattifier.security_tokens
-- (
--     series      text,
--     token_date  timestamp,
--     authorities set<text>,
--     user_agent  text,
--     token_value text,
--     login       text,
--     pass        text,
--     ip_address  text,
--     PRIMARY KEY (series)
-- );

CREATE TABLE IF NOT EXISTS
    chattifier.users
(
    user_id   uuid,
    username  text,
    bio       text,
    rooms_ids set<uuid>,
    lastname  text,
    firstname text,
    pass      text,
    PRIMARY KEY (user_id)
);
CREATE INDEX nameIndexForUsers ON chattifier.users (username);

select *
from chattifier.users
where user_id = ?;

CREATE TABLE IF NOT EXISTS
    chattifier.chat_rooms
(
    room_id       uuid,
    room_name     text,
    creation_date timestamp,
    creator_login text,
    participants  set<uuid>,
    banner        text,
    sender        uuid,
    private       boolean,
    PRIMARY KEY (room_id)
);

CREATE TABLE IF NOT EXISTS
    chattifier.messages_by_room
(
    room_id        uuid,
    message_id     timeuuid,
    data           text,
    sender         uuid,
    system_message boolean,
    PRIMARY KEY ((room_id), message_id)
) WITH CLUSTERING ORDER BY (message_id DESC);