```sql
CREATE TABLE films (
    film_id SERIAL PRIMARY KEY, -- Уникальный идентификатор фильма
    name VARCHAR NOT NULL, -- Название фильма
    description CHAR(200), -- Описание фильма
    releaseDate DATE, -- Дата выхода фильма
    duration INTEGER, -- Длительность фильма в минутах
    rating INTEGER, -- Рейтинг фильма (ссылка на таблицу ratings)
    CONSTRAINT fk_rating FOREIGN KEY (rating) REFERENCES ratings(id) ON DELETE SET NULL
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY, -- Уникальный идентификатор пользователя
    email VARCHAR NOT NULL UNIQUE, -- Электронная почта пользователя
    login VARCHAR NOT NULL UNIQUE, -- Логин пользователя
    name VARCHAR NOT NULL, -- Имя пользователя
    birthday DATE -- Дата рождения пользователя
);

CREATE TABLE likes (
    like_id SERIAL PRIMARY KEY, -- Уникальный идентификатор лайка
    user_id INTEGER NOT NULL, -- Идентификатор пользователя
    film_id INTEGER NOT NULL, -- Идентификатор фильма
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_film FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

CREATE TABLE friendships (
    id SERIAL PRIMARY KEY, -- Уникальный идентификатор дружбы
    user_id INTEGER NOT NULL, -- Идентификатор пользователя
    friend_id INTEGER NOT NULL, -- Идентификатор друга
    CONSTRAINT fk_user1 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user2 FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE genres (
    genre_id SERIAL PRIMARY KEY, -- Уникальный идентификатор жанра
    name VARCHAR NOT NULL UNIQUE -- Название жанра
);

CREATE TABLE film_genres (
    id SERIAL PRIMARY KEY, -- Уникальный идентификатор записи
    genre_id INTEGER NOT NULL, -- Идентификатор жанра
    film_id INTEGER NOT NULL, -- Идентификатор фильма
    CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE,
    CONSTRAINT fk_film_genre FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY, -- Уникальный идентификатор рейтинга
    name VARCHAR NOT NULL UNIQUE -- Название рейтинга
);
```