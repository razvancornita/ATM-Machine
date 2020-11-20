DROP TABLE IF EXISTS bank_account;
DROP TABLE IF EXISTS card;

CREATE TABLE bank_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  owner_first_name VARCHAR(40) NOT NULL,
  owner_last_name VARCHAR(40) NOT NULL,
  currency VARCHAR(5) NOT NULL,
  balance INT DEFAULT 0
);

CREATE TABLE card (
  id INT AUTO_INCREMENT PRIMARY KEY,
  bank_account_id INT NOT NULL,
  FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);

INSERT INTO bank_account (id, owner_first_name, owner_last_name, currency, balance) VALUES (1, 'Andrei', 'Popescu', 'EUR', 450);

INSERT INTO card (id, bank_account_id) VALUES (1, 1);
INSERT INTO card (id, bank_account_id) VALUES (2, 1);