DROP DATABASE  IF EXISTS rentings;
CREATE DATABASE rentings;
CREATE USER IF NOT EXISTS 'ensf480'@'localhost' IDENTIFIED BY 'password';
GRANT ALL ON rentings.* to 'ensf480'@'localhost';

USE rentings;

CREATE TABLE user (
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    last_login DATETIME DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO user (email, password, role, last_login)
VALUES (
        'manager', 'password', 'MANAGER', NOW()
       );

CREATE TABLE manager_configuration (
    id INT AUTO_INCREMENT,
    period_days INT NOT NULL,
    fees DOUBLE NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO manager_configuration (period_days, fees)
VALUES (
        60, 100.0
       );

CREATE TABLE property (
    id INT AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL,
    property_type VARCHAR(20) NOT NULL,
    number_of_bedrooms INT NOT NULL,
    number_of_bathrooms INT NOT NULL,
    is_furnished BOOLEAN NOT NULL,
    city_quadrant VARCHAR(20) NOT NULL,
    landlord VARCHAR(255) NOT NULL,
    is_fee_paid BOOLEAN NOT NULL DEFAULT false,
    property_status VARCHAR(20) NOT NULL,
    date_published DATETIME DEFAULT NULL,
    payment_date DATETIME DEFAULT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (landlord) REFERENCES user(email) ON DELETE CASCADE
);

CREATE TABLE property_form (
    id INT AUTO_INCREMENT,
    property_type VARCHAR(20),
    number_of_bedrooms INT,
    number_of_bathrooms INT,
    city_quadrant VARCHAR(20),

    PRIMARY KEY (id)
);

CREATE TABLE property_subject (
    id INT AUTO_INCREMENT,
    renter varchar(255) NOT NULL UNIQUE,
    property_form INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (renter) references user(email) ON DELETE CASCADE,
    FOREIGN KEY (property_form) references property_form(id) ON DELETE CASCADE
);
