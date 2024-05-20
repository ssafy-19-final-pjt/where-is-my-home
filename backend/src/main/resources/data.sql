INSERT INTO member (create_date, email, is_deleted, modify_date, name, profile, refresh_token, id)
VALUES (TIMESTAMP '2024-05-19 00:00:00', 'test@test.com', false, TIMESTAMP '2024-05-19 00:00:00', 'test', NULL, '', 11);

INSERT INTO general_member (member_id, user_enc_password, id)
VALUES (11, '2492b60365ec438c50a4b37302790e61000eaf6866338c4f39d5d7c1cfdd4b78', 11);

INSERT INTO member_secret (member_id, salt, id)
VALUES (11, '7b6fcbe49833ef658d3f87e2cec1b83c', DEFAULT);

INSERT INTO login_attempt (count, login_recent_attemp, member_id, id)
VALUES (0, TIMESTAMP '2024-05-19 00:00:00', 11, DEFAULT);

INSERT INTO board (content,create_date,hit,member_id,modify_date,title,id) VALUES ('testContent',TIMESTAMP '2024-05-19 00:00:00',0,11,TIMESTAMP '2024-05-19 00:00:00','testTitle',11)