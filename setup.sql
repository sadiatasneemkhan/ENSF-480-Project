CREATE DATABASE IF NOT EXISTS rentings;
CREATE USER IF NOT EXISTS 'ensf480'@'localhost' IDENTIFIED BY 'password';
GRANT ALL ON rentings.* to 'ensf480'@'localhost';

USE rentings;

CREATE TABLE user (
    id INT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_registered BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);

INSERT INTO user (username, password, role, is_registered)
VALUES (
        'manager', 'password', 'MANAGER', true
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
    date_published DATETIME,

    PRIMARY KEY (id),
    FOREIGN KEY (landlord) REFERENCES user(username) ON DELETE CASCADE
);

CREATE TABLE property_form (
    id INT AUTO_INCREMENT,
    property_type VARCHAR(20),
    number_of_bedrooms INT,
    number_of_bathrooms INT,
    city_quadrant VARCHAR(20),

    PRIMARY KEY (id)
);

CREATE TABLE renter_subscription (
    id INT AUTO_INCREMENT,
    renter VARCHAR(255) NOT NULL,
    property_id INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (renter) REFERENCES user(username) ON DELETE CASCADE,
    FOREIGN KEY (property_id) REFERENCES property(id) ON DELETE CASCADE
);

CREATE TABLE subscribed_property_form (
    id INT AUTO_INCREMENT,
    property_form_id INT NOT NULL,
    renter_subscription_id INT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (property_form_id) REFERENCES property_form(id) ON DELETE CASCADE,
    FOREIGN KEY (renter_subscription_id) REFERENCES renter_subscription(id) ON DELETE CASCADE
);