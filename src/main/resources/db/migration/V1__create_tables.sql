CREATE TABLE IF NOT EXISTS schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    asset_value DECIMAL(19,2),
    deposit_amount DECIMAL(19,2),
    balloon_payment DECIMAL(19,2),
    info TEXT,
    period_payment DECIMAL(19,2),
    total_interest DECIMAL(19,2),
    total_payment DECIMAL(19,2)
);

CREATE TABLE IF NOT EXISTS installments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id INT,
    period INTEGER,
    payment DECIMAL(19,2),
    principle DECIMAL(19,2),
    interest DECIMAL(19,2),
    balance DECIMAL(19,2),
    FOREIGN KEY (schedule_id) REFERENCES schedules(id)
);