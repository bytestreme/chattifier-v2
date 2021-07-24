CREATE TABLE IF NOT EXISTS
    chat_ks.users_by_id
(
    user_id   uuid,
    username  text,
    bio       text,
    room_ids set<uuid>,
    lastname  text,
    firstname text,
    pass      text,
    PRIMARY KEY (user_id)
);
CREATE INDEX nameIndexForUsers ON chat_ks.users_by_id (username);

insert into chat_ks.users_by_id(user_id, username, bio, room_ids, lastname, firstname, pass)
VALUES ('b9792eab-1ed3-4834-8329-277b6109a7b9', 'admin', 'somebio', {}, 'last', 'first',
        '$2y$12$CZ6wZzlf8aQxuC/J7LbEv.Sf8fmYVFUcrFCUu2H5mUYh7h/UHritq');

insert into chat_ks.users_by_id(user_id, username, bio, room_ids, lastname, firstname, pass)
VALUES ('ac1739c9-4e90-47e8-9026-d57b1deb723d', 'user', 'somebio', {}, 'last', 'first',
        '$2y$12$tC4aebf7lbSTZSWsOvJIye0Nh7XFDOILBJtwIgpcq8mFdjHHJvaJ.');

