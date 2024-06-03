ALTER TABLE client
DROP COLUMN first_name,
DROP COLUMN last_name;

ALTER TABLE application
ADD COLUMN product_code varchar(10),
ADD COLUMN term_in_months int,
ADD COLUMN interest numeric;
