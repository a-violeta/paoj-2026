--user, card, account, transaction
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

-- USERS
CREATE TABLE users (
                       id VARCHAR(50) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       phone VARCHAR(30) --nullable???
);

-- ACCOUNTS (superclass + subclasses)
CREATE TABLE accounts (
                          iban VARCHAR(34) PRIMARY KEY,
                          balance NUMERIC(15,2) NOT NULL,
                          currency VARCHAR(10) NOT NULL,
                          type VARCHAR(20) NOT NULL, -- CHECKING / SAVINGS / LOAN

                          overdraft_limit NUMERIC(15,2),        -- CheckingAccount
                          interest_rate NUMERIC(5,2),           -- Savings + Loan
                          loan_amount NUMERIC(15,2),            -- LoanAccount
                          remaining_amount NUMERIC(15,2),       -- LoanAccount
                          due_date DATE,                        -- LoanAccount

                          created_at TIMESTAMP,
                          active BOOLEAN DEFAULT TRUE,

                          user_id VARCHAR(50) NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

-- CARDS
CREATE TABLE cards (
                       card_number VARCHAR(16) PRIMARY KEY,
                       cvv VARCHAR(3) NOT NULL,
                       expiration_date DATE NOT NULL,
                       active BOOLEAN DEFAULT TRUE,

                       type VARCHAR(20), -- DEBIT / CREDIT / VIRTUAL

                       user_id VARCHAR(50) NOT NULL,
                       iban VARCHAR(34) NOT NULL,

                       FOREIGN KEY (user_id) REFERENCES users(id),
                       FOREIGN KEY (iban) REFERENCES accounts(iban)
);

-- TRANSACTIONS
CREATE TABLE transactions (
                              id VARCHAR(50) PRIMARY KEY,
                              timestamp TIMESTAMP NOT NULL,
                              amount NUMERIC(15,2) NOT NULL,

                              type VARCHAR(30) NOT NULL,
    -- DEPOSIT / WITHDRAWAL / TRANSFER / INTERNATIONAL_TRANSFER

                              fee NUMERIC(10,2), -- doar pentru InternationalTransfer

                              source_iban VARCHAR(34),
                              destination_iban VARCHAR(34),

                              FOREIGN KEY (source_iban) REFERENCES accounts(iban),
                              FOREIGN KEY (destination_iban) REFERENCES accounts(iban)
);

--SELECT u.name, a.iban, a.balance FROM users u JOIN accounts a ON u.id = a.user_id;

--SELECT a.iban, c.card_number, c.type FROM accounts a JOIN cards c ON a.iban = c.iban;

--SELECT t.id, t.amount, t.type, s.iban AS source, d.iban AS destination FROM transactions t LEFT JOIN accounts s ON t.source_iban = s.iban LEFT JOIN accounts d ON t.destination_iban = d.iban;