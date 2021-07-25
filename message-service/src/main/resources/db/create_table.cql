CREATE TABLE IF NOT EXISTS
    chat_ks.messages_by_room
(
    room_id        uuid,
    message_id     timeuuid,
    data           text,
    sender         uuid,
    system_message boolean,
    timestamp      timestamp,
    PRIMARY KEY ((room_id), message_id)
) WITH CLUSTERING ORDER BY (message_id DESC);
