-- V5__add_is_recurring_column.sql
ALTER TABLE booking ADD COLUMN is_recurring BOOLEAN DEFAULT FALSE;
