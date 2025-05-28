-- V1.0__create_triggers.sql

-- Trigger 1: Automatic Notification Creation
CREATE OR REPLACE FUNCTION trigger_create_notification_on_token_purchase()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO notifications (message, issued_date, status, meter_id, user_id)
VALUES (
           'Your electricity token purchase of ' || NEW.amount || ' RWF for ' || NEW.token_value_days || ' days has been successful. Token: ' || NEW.token,
           CURRENT_TIMESTAMP,
           'UNREAD',
           NEW.meter_id,
           (SELECT user_id FROM meters WHERE id = NEW.meter_id)
       );
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER create_notification_on_token_purchase
    AFTER INSERT ON purchased_tokens
    FOR EACH ROW
    EXECUTE FUNCTION trigger_create_notification_on_token_purchase();

-- Trigger 2: Input Validation
CREATE OR REPLACE FUNCTION trigger_validate_token_purchase()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.amount < 100 OR NEW.amount % 100 != 0 THEN
        RAISE EXCEPTION 'Amount must be a multiple of 100 and at least 100 RWF. Got: %', NEW.amount;
END IF;
    IF NEW.amount > 182500 THEN
        RAISE EXCEPTION 'Amount cannot exceed 182,500 RWF (5 years). Got: %', NEW.amount;
END IF;
    IF NEW.token_value_days != NEW.amount / 100 THEN
        RAISE EXCEPTION 'Token value days must be amount / 100. Expected: %, Got: %', NEW.amount / 100, NEW.token_value_days;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER validate_token_purchase
    BEFORE INSERT ON purchased_tokens
    FOR EACH ROW
    EXECUTE FUNCTION trigger_validate_token_purchase();

-- Trigger 3: Log Token Status Changes
CREATE TABLE IF NOT EXISTS token_status_log (
                                                id BIGSERIAL PRIMARY KEY,
                                                token_id BIGINT NOT NULL,
                                                old_status VARCHAR,
                                                new_status VARCHAR,
                                                changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                FOREIGN KEY (token_id) REFERENCES purchased_tokens(id)
    );

CREATE OR REPLACE FUNCTION trigger_log_token_status_change()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.token_status != OLD.token_status THEN
        INSERT INTO token_status_log (token_id, old_status, new_status, changed_at)
        VALUES (NEW.id, OLD.token_status, NEW.token_status, CURRENT_TIMESTAMP);
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER log_token_status_change
    AFTER UPDATE OF token_status ON purchased_tokens
    FOR EACH ROW
    EXECUTE FUNCTION trigger_log_token_status_change();


-- INSERT INTO purchased_tokens (token, token_value_days, amount, meter_id, token_status)
-- VALUES ('TEST123', 5, 500, 1, 'NEW');
-- SELECT * FROM notifications WHERE meter_id = 1 AND message LIKE '%TEST123%';