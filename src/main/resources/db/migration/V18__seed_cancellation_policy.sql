INSERT INTO cancellation_policies (name, scope_type, active)
VALUES ('Default Cancellation Policy', 'GLOBAL', TRUE);

INSERT INTO cancellation_policy_rules (policy_id, min_hours_before_journey, refund_percentage)
SELECT id, 48, 90.00 FROM cancellation_policies WHERE name = 'Default Cancellation Policy';

INSERT INTO cancellation_policy_rules (policy_id, min_hours_before_journey, refund_percentage)
SELECT id, 24, 75.00 FROM cancellation_policies WHERE name = 'Default Cancellation Policy';

INSERT INTO cancellation_policy_rules (policy_id, min_hours_before_journey, refund_percentage)
SELECT id, 12, 50.00 FROM cancellation_policies WHERE name = 'Default Cancellation Policy';

INSERT INTO cancellation_policy_rules (policy_id, min_hours_before_journey, refund_percentage)
SELECT id, 0, 0.00 FROM cancellation_policies WHERE name = 'Default Cancellation Policy';
