-- Remove the existing foreign key constraint
ALTER TABLE booking
DROP CONSTRAINT IF EXISTS fk_booking_slot;

-- Add a new foreign key with ON DELETE CASCADE
ALTER TABLE booking
    ADD CONSTRAINT fk_booking_slot
        FOREIGN KEY (slot_id)
            REFERENCES schedule_slot(id)
            ON DELETE CASCADE;
