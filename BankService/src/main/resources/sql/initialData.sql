insert into bankdb.public.tbl_bank_user (id, email, finger_print, first_name, last_name) values
(1, 'test1@test.com', 'FAKE_DATA', 'test1', 'test1');
insert into bankdb.public.tbl_bank_user (id, email, finger_print, first_name, last_name) values
(2, 'test2@test.com', 'FAKE_DATA', 'test2', 'test2');


delete from bankdb.public.tbl_bank_card where id in (1, 2);
insert into bankdb.public.tbl_bank_card (id, card_number, balance_amount, is_blocked, pin_code, wrong_auth_attempt_count, user_id, preferred_auth_method) values
(1, 1, 1000, false, '$2a$10$vMtEDs1M1x0fVVKNAWTeiexrpqqKeZ1qS5CBDqGYyNnFt218waf0W', 0, 1, 1);
insert into bankdb.public.tbl_bank_card (id, card_number, balance_amount, is_blocked, pin_code, wrong_auth_attempt_count, user_id, preferred_auth_method) values
(2, 2, 2000, false, '$2a$10$R9yrl2LqvYV/zCcu95PBdeDTCHZw0r/ai2Hac4pUg8mN7xwYmyf3W', 0, 2, 2);
