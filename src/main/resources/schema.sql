DROP TABLE if EXISTS slot;
DROP TABLE if EXISTS locker;

CREATE TABLE locker (
id INT AUTO_INCREMENT PRIMARY KEY,
capacity INT NOT null
);

CREATE TABLE slot (
id INT AUTO_INCREMENT PRIMARY KEY,
locker_id INT,
foreign key (locker_id) references locker(id),
has_bag BOOLEAN,
ticket_no VARCHAR(8)
);
