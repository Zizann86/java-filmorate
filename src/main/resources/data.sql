MERGE INTO ratings AS r
    USING (VALUES
               (1, 'G'),
               (2, 'PG'),
               (3, 'PG-13'),
               (4, 'R'),
               (5, 'NC-17')
    ) AS u (id, name)
    ON r.id = u.id
    WHEN MATCHED THEN
        UPDATE SET r.name = u.name
    WHEN NOT MATCHED THEN
        INSERT (id, name) VALUES (u.id, u.name);

MERGE INTO genres AS g
    USING (VALUES
               ('Комедия'),
               ('Драма'),
               ('Мультфильм'),
               ('Триллер'),
               ('Документальный'),
               ('Боевик')
    ) AS new_genres(name)
    ON g.name = new_genres.name
    WHEN NOT MATCHED THEN
        INSERT (name) VALUES (new_genres.name);