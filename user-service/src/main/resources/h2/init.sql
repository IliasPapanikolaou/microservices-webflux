create table users (
    id bigint auto_increment,
    name varchar(50),
    balance double,
    primary key (id)
);

create table transactions (
    id bigint auto_increment,
    user_id bigint,
    amount double,
    transaction_date timestamp,
    foreign key (user_id) references users(id) on delete cascade
);

insert into users
    (name, balance)
    values
    ('George Poorman', 1000.0),
    ('Jack Richman', 1000.0),
    ('Maria Buying', 2000.0),
    ('Harris Philton', 3000.0);