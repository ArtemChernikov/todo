CREATE TABLE IF NOT EXISTS priorities
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR UNIQUE NOT NULL,
    position INT
);

ALTER TABLE tasks
    ADD COLUMN priority_id int REFERENCES priorities (id);