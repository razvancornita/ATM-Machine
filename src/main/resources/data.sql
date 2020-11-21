DROP TABLE IF EXISTS card;
DROP TABLE IF EXISTS bank_account;

CREATE TABLE bank_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  owner_first_name VARCHAR(40) NOT NULL,
  owner_last_name VARCHAR(40) NOT NULL,
  currency VARCHAR(5) NOT NULL,
  balance DOUBLE DEFAULT 0
);

CREATE TABLE card (
  id INT AUTO_INCREMENT PRIMARY KEY,
  bank_account_id INT NOT NULL,
  pin VARCHAR(4) NOT NULL,
  FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);

INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (1, 'Andrei', 'Popescu', 'EUR', 450);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (37962, 'Andrei', 'Popescu', 'EUR', 469.81);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (90460, 'Cezara', 'Avram', 'RON', 12034.5);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (10452, 'Ioana-Larisa', 'Marin', 'RON', 33210.0);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (86396, 'Zane', 'Schaefer', 'USD', 22.67);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (14090, 'Anton', 'Grigore', 'RON', 95120.5);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (86577, 'Cristiana', 'Iacob', 'RON', 130.9);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (43437, 'Daniel', 'Miron', 'EUR', 3428.22);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (13973, 'Nicolae', 'Adrian', 'RON', -12.7);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (42805, 'Teodora', 'Panait', 'RON', 23475.85);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (17776, 'Ovidiu', 'Sava', 'USD', 287.0);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (20291, 'George', 'Tudor', 'RON', 34287.99);

INSERT INTO card (id, bank_account_id, pin) VALUES (744, 37962, '9757');
INSERT INTO card (id, bank_account_id, pin) VALUES (993, 37962, '3490');
INSERT INTO card (id, bank_account_id, pin) VALUES (433, 90460, '3244');
INSERT INTO card (id, bank_account_id, pin) VALUES (635, 10452, '1215');
INSERT INTO card (id, bank_account_id, pin) VALUES (814, 10452, '0449');
INSERT INTO card (id, bank_account_id, pin) VALUES (860, 86396, '6465');
INSERT INTO card (id, bank_account_id, pin) VALUES (727, 14090, '8658');
INSERT INTO card (id, bank_account_id, pin) VALUES (516, 14090, '6597');
INSERT INTO card (id, bank_account_id, pin) VALUES (191, 86577, '5275');
INSERT INTO card (id, bank_account_id, pin) VALUES (473, 43437, '3348');
INSERT INTO card (id, bank_account_id, pin) VALUES (530, 13973, '7923');
INSERT INTO card (id, bank_account_id, pin) VALUES (689, 13973, '7118');
INSERT INTO card (id, bank_account_id, pin) VALUES (366, 42805, '7351');
INSERT INTO card (id, bank_account_id, pin) VALUES (258, 42805, '1925');
INSERT INTO card (id, bank_account_id, pin) VALUES (614, 17776, '8518');
INSERT INTO card (id, bank_account_id, pin) VALUES (509, 20291, '5877');
INSERT INTO card (id, bank_account_id, pin) VALUES (407, 20291, '4249');
INSERT INTO card (id, bank_account_id, pin) VALUES (238, 20291, '4885');