ALTER TABLE application
ADD COLUMN IF NOT EXISTS agreement_id uuid unique;

CREATE INDEX agreement_id_idx ON application (agreement_id);