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
  FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);

INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (1, 'Andrei', 'Popescu', 'EUR', 450);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (37962, 'Andrei', 'Popescu', 'EUR', 469.81);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (90460, 'Cezara', 'Avram', 'RON', 12034.5);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (10452, 'Ioana-Larisa', 'Marin', 'RON', 33210.0);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (86396, 'Zane', 'Schaefer', 'USD', 22.67);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (55393, 'Cillian', 'Perkins', 'GBP', 2438.11);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (14090, 'Anton', 'Grigore', 'RON', 95120.5);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (86577, 'Cristiana', 'Iacob', 'RON', 130.9);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (43437, 'Daniel', 'Miron', 'EUR', 3428.22);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (13973, 'Nicolae', 'Adrian', 'RON', -12.7);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (42805, 'Teodora', 'Panait', 'RON', 23475.85);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (17776, 'Ovidiu', 'Sava', 'USD', 287.0);
INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (20291, 'George', 'Tudor', 'RON', 34287.99);

INSERT INTO card (id, bank_account_id) VALUES (744, 37962);
INSERT INTO card (id, bank_account_id) VALUES (993, 37962);
INSERT INTO card (id, bank_account_id) VALUES (433, 90460);
INSERT INTO card (id, bank_account_id) VALUES (635, 10452);
INSERT INTO card (id, bank_account_id) VALUES (814, 10452);
INSERT INTO card (id, bank_account_id) VALUES (860, 86396);
INSERT INTO card (id, bank_account_id) VALUES (944, 55393);
INSERT INTO card (id, bank_account_id) VALUES (482, 55393);
INSERT INTO card (id, bank_account_id) VALUES (920, 55393);
INSERT INTO card (id, bank_account_id) VALUES (727, 14090);
INSERT INTO card (id, bank_account_id) VALUES (516, 14090);
INSERT INTO card (id, bank_account_id) VALUES (191, 86577);
INSERT INTO card (id, bank_account_id) VALUES (473, 43437);
INSERT INTO card (id, bank_account_id) VALUES (530, 13973);
INSERT INTO card (id, bank_account_id) VALUES (689, 13973);
INSERT INTO card (id, bank_account_id) VALUES (366, 42805);
INSERT INTO card (id, bank_account_id) VALUES (258, 42805);
INSERT INTO card (id, bank_account_id) VALUES (614, 17776);
INSERT INTO card (id, bank_account_id) VALUES (509, 20291);
INSERT INTO card (id, bank_account_id) VALUES (407, 20291);
INSERT INTO card (id, bank_account_id) VALUES (238, 20291);