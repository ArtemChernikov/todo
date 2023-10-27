INSERT INTO priorities (name, position)
VALUES ('Высокий', 1);
INSERT INTO priorities (name, position)
VALUES ('Обычный', 2);

UPDATE tasks
SET priority_id = (SELECT id FROM priorities WHERE name = 'Высокий');