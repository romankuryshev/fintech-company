BEGIN;
ALTER TABLE application
DROP CONSTRAINT status_check;
ALTER TABLE application
ADD CONSTRAINT status_check
    CHECK ( status in ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED', 'CANCELED'))
