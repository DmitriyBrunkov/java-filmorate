package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() throws UserNotFoundException, UserValidationException {
        User assertUser = new User();
        assertUser.setLogin("dolore");
        assertUser.setName("Nick Name");
        assertUser.setEmail("mail@mail.ru");
        assertUser.setBirthday(LocalDate.of(1946, 8, 20));
        userStorage.add(assertUser);
        User user = userStorage.get(1);
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals(assertUser.getName(), user.getName());
        Assertions.assertEquals(assertUser.getEmail(), user.getEmail());
        Assertions.assertEquals(assertUser.getLogin(), user.getLogin());
    }

    @Test
    public void testFindFilmById() throws FilmNotFoundException {
        Film assertFilm = new Film();
        assertFilm.setName("Pulp Fiction");
        assertFilm.setDescription("adipisicing");
        assertFilm.setReleaseDate(LocalDate.of(1967, 3, 25));
        assertFilm.setDuration(100);
        filmStorage.put(assertFilm);
        Film film = filmStorage.get(1);
        Assertions.assertEquals(1, film.getId());
        Assertions.assertEquals(assertFilm.getName(), film.getName());
        Assertions.assertEquals(assertFilm.getDescription(), film.getDescription());
        Assertions.assertEquals(assertFilm.getDuration(), film.getDuration());
    }
}