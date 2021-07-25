CREATE KEYSPACE IF NOT EXISTS chat_ks WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

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
    chat_ks.chat_rooms_by_id
(
    room_id       uuid,
    room_name     text,
    creation_date timestamp,
    creator_uid   uuid,
    participants  set<uuid>,
    banner        text,
    one_to_one    boolean,
    PRIMARY KEY (room_id)
);

insert into chat_ks.chat_rooms_by_id(room_id, room_name, creation_date, creator_uid, participants, banner, one_to_one)
VALUES ('0c05078c-d92f-4bea-8f4c-d5ace7772372', 'room 1', now(), 'b9792eab-1ed3-4834-8329-277b6109a7b9',
        {'b9792eab-1ed3-4834-8329-277b6109a7b9', 'ac1739c9-4e90-47e8-9026-d57b1deb723d'}, 'banner text', true);

-- insert into chat_ks.chat_rooms_by_id(room_id, room_name, creation_date, creator_uid, participants, banner, one_to_one)
-- VALUES (0c05078c-d92f-4bea-8f4c-d5ace7772372, 'room 1', dateof(now()), b9792eab-1ed3-4834-8329-277b6109a7b9,
--         {b9792eab-1ed3-4834-8329-277b6109a7b9, ac1739c9-4e90-47e8-9026-d57b1deb723d}, 'banner text', true);

