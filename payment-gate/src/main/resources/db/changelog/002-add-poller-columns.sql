ALTER TABLE disbursement
ADD COLUMN check_count int;

ALTER TABLE disbursement
ADD COLUMN next_check_date timestamp;

BEGIN;
ALTER TABLE disbursement DROP CONSTRAINT disbursement_status_check;
ALTER TABLE disbursement ADD CONSTRAINT disbursement_status_check
    CHECK ( status IN ('COMPLETED', 'AWAITS', 'ERROR') );
COMMIT;
