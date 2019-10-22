CREATE DATABASE IF NOT EXISTS tps;

USE tps;

CREATE TABLE product
(
    cusip      varchar(9)    NOT NULL PRIMARY KEY,
    face_value int(11)       NOT NULL DEFAULT '100',
    coupon     decimal(5, 4) NOT NULL DEFAULT '0.04',
    remaining  bigint(20)    NOT NULL DEFAULT '200000000',
    maturity   date
) ENGINE = InnoDB;

CREATE TABLE trader
(
    t_id      int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    t_name    varchar(20) NOT NULL,
    t_account varchar(20) NOT NULL,
    t_pwd     varchar(20) NOT NULL
) ENGINE = InnoDB;

# Assume there is only one seller
CREATE TABLE seller
(
    s_id   int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    s_name varchar(20) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE client
(
    c_id   int(11)     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    c_name varchar(20) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE trader_leg
(
    txn_id             int(11) AUTO_INCREMENT NOT NULL,
    inter_id           varchar(11)            NOT NULL,
    inter_v_num        int(11)                NOT NULL,
    status             varchar(20),
    reject_code        varchar(20),
    reject_reason      text,
    t_id               int(11),
    s_id               int(11),
    cusip              varchar(9)             NOT NULL,
    price              decimal(6, 2)          NOT NULL DEFAULT '100',
    notional_amount    int(11)                NOT NULL,
    matched_seller_leg int(11),
    FOREIGN KEY fk_t_id (t_id)
        REFERENCES trader (t_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_s_id (s_id)
        REFERENCES seller (s_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    PRIMARY KEY (txn_id, inter_id)
) ENGINE = InnoDB;

CREATE TABLE sales_leg
(
    txn_id             int(11) AUTO_INCREMENT NOT NULL,
    inter_id           varchar(11)            NOT NULL,
    inter_v_num        int(11)                NOT NULL,
    status             varchar(20),
    reject_code        varchar(20),
    reject_reason      text,
    s_id               int(11),
    c_id               int(11),
    cusip              varchar(9)             NOT NULL,
    price              decimal(6, 2)          NOT NULL DEFAULT '100',
    notional_amount    int(11)                NOT NULL,
    matched_trader_leg int(11),
    FOREIGN KEY fk_s_id (s_id)
        REFERENCES seller (s_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    FOREIGN KEY fk_c_id (c_id)
        REFERENCES client (c_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    PRIMARY KEY (txn_id, inter_id)
) ENGINE = InnoDB;

# DROP SCHEMA tps;