SELECT * FROM chat_log WHERE sender = 'bob' ORDER BY time ASC
SELECT * FROM chat_log WHERE receiver = 'alice' ORDER BY time ASC

CREATE INDEX chat_log_uid ON chat_log (sender);
CREATE INDEX chat_log_uid ON chat_log (receiver);