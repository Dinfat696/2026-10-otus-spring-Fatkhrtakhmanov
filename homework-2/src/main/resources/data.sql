INSERT INTO authors (name)
VALUES ('Leo Tolstoy'),
       ('Joshua Bloch'),
       ('Robert Martin');

INSERT INTO genres (name)
VALUES ('Novel'),
       ('Technical'),
       ('Software Engineering');

INSERT INTO books (title, author_id, genre_id)
VALUES ('War and Peace', (SELECT id FROM authors WHERE name = 'Leo Tolstoy'),
        (SELECT id FROM genres WHERE name = 'Novel')),
       ('Effective Java', (SELECT id FROM authors WHERE name = 'Joshua Bloch'),
        (SELECT id FROM genres WHERE name = 'Technical')),
       ('Clean Code', (SELECT id FROM authors WHERE name = 'Robert Martin'),
        (SELECT id FROM genres WHERE name = 'Software Engineering'));
