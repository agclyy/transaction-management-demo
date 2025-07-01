-- create sequence for transaction id
DROP SEQUENCE IF EXISTS transaction_seq;
CREATE SEQUENCE transaction_seq START WITH 1 INCREMENT BY 1;

-- create table transactions
DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
  transaction_id CHAR(16) PRIMARY KEY,
  remitter_account VARCHAR(20),
  from_currency CHAR(3),
  from_amount DECIMAL(19,4),
  beneficiary_account VARCHAR(20),
  to_currency CHAR(3),
  to_amount DECIMAL(19,4),
  exchange_rate DECIMAL(10,6),
  fee_currency CHAR(3),
  fee DECIMAL(19,4),
  transaction_status VARCHAR(10),
  transaction_type VARCHAR(10),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_user VARCHAR(20),
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_user VARCHAR(20),
  deleted_at TIMESTAMP,
  deleted_user VARCHAR(20),
  comments TEXT
);
CREATE INDEX idx_remitter_account ON transactions(remitter_account);
CREATE INDEX idx_beneficiary_account ON transactions(beneficiary_account);
CREATE INDEX idx_account_status ON transactions(remitter_account, transaction_status);
CREATE INDEX idx_date_status ON transactions(created_at, transaction_status);
CREATE INDEX idx_created_at ON transactions(created_at);
CREATE INDEX idx_updated_at ON transactions(updated_at);