# Filmorate

Приложение Filmorate родилось как приложение для составления рейтингов фильмов с котролем пользователей.

Команда разработчиков №12 добавила в него новый, восхитительный функционал:
1. Функциональность «Общие фильмы»
   - вывод общих с другом фильмов с сортировкой по их популярности
2. Функциональность «Лента событий»
    - возможность просмотра последних событий на платформе
3. Добавление режиссёров в фильмы
    - добавлена информация о режиссерах фильма и вывод фильмов по режиссерам с сортиовками
4. Функциональность «Рекомендации»
    - реализована простая рекомендательная система для фильмов
5. Удаление фильмов и пользователей
    - добавлена функциональность для удаления фильма и пользователя по идентификатору
6. Вывод самых популярных фильмов по жанру и годам
    - добавлена возможность выводить топ фильмов по количеству лайков с фильтрацией
7. Функциональность «Поиск»
    - реализован поиск по названию фильмов и по режиссёру
8. Функциональность «Отзывы»
    - добавлены отзывы на фильмы с рейтингом

ER-diagram:
<picture>    
<img src="src/main/resources/filmorate_er_diagram.png">
</picture>

Request examples:

Get names of user's confirmed friends:

```
SELECT name
FROM users
WHERE id IN (SELECT friend_id
             FROM friends
             WHERE user_id=request_id             
             AND status='confirmed'
             UNION
             SELECT user_id
             FROM friends
             WHERE friend_id=request_id)
             AND status='confirmed');
```

Get rating of all comedy:

```
SELECT name,
       rt.name
FROM films
JOIN ratings AS rt ON films.rating=rt.rating_id;
WHERE films.id IN (SELECT film_id
                   FROM genres
                   WHERE genre_id=(SELECT genre_id
                                   FROM genre_names
                                   WHERE name='comedy'))  
```