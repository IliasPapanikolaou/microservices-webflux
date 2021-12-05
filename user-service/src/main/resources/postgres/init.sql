CREATE TABLE users (
   id serial primary key,
   name varchar(50),
   balance decimal
);

CREATE TABLE transactions (
  id serial primary key,
  user_id bigint,
  amount decimal,
  transaction_date timestamp,
  CONSTRAINT fk_user_id
    FOREIGN KEY(user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

INSERT INTO users
(name, balance)
VALUES
    ('George Poorman', 1000.0),
    ('Jack Richman', 1000.0),
    ('Maria Buying', 2000.0),
    ('Harris Philton', 3000.0);