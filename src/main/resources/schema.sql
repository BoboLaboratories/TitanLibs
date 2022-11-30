CREATE TABLE IF NOT EXISTS table1 (

    uuid   VARCHAR(36) NOT NULL PRIMARY KEY,
    name   VARCHAR(16) NOT NULL,
    amount INT         NOT NULL,

    CHECK (amount >= 0)

);

-- SEPARATOR --

CREATE TABLE IF NOT EXISTS table2 (

    id      INT AUTO_INCREMENT PRIMARY KEY,
    time    TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
    uuid    VARCHAR(36) NOT NULL,
    amount  INT NOT NULL,
    reason  VARCHAR(255),

    FOREIGN KEY (uuid) REFERENCES table1 (uuid)
    ON UPDATE CASCADE
    ON DELETE RESTRICT

);

-- SEPARATOR --