INSERT INTO tasks.tasks_states (id, name) VALUES (1, 'ASIGNADA');
INSERT INTO tasks.tasks_states (id, name) VALUES (2, 'CERRADA');
INSERT INTO tasks.tasks_states (id, name) VALUES (3, 'CANCELADA');

INSERT INTO tasks.tasks_categories (id, name) VALUES (1, 'INTEGRACION');

INSERT INTO tasks.tasks (name, description, created_at, task_state_id, user_id) VALUES ('Tarea Ejemplo', 'Integrar XTF', '2019-11-10 10:30:00', 1, 1);

