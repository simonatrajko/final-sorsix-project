-- V11__add_day_of_week_column.sql
ALTER TABLE schedule_slot
    ADD COLUMN day_of_week VARCHAR(10) NOT NULL DEFAULT 'MONDAY';
