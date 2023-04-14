package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DataBinder;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTests {
    @Autowired
    private UserController userController;

    @Autowired
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Autowired
    Validator validator = factory.getValidator();

    @Test
    public void shouldAddUser() {
        User user = new User();
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        userController.add(user);
        for (User userFromCollection : userController.getAll()) {
            if (userFromCollection.getId().equals(user.getId())) {
                assertEquals(user, userFromCollection);
            }
        }
    }

    @Test
    public void shouldSubstituteNameWithLoginWhenNameIsEmpty() {
        User user = new User();
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        userController.add(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void shouldThrowExceptionWhenPutNonExistentId() {
        User user = new User();
        user.setId(1234);
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userController.put(user)
        );
        assertEquals("ID: " + user.getId() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenWrongEmail() {
        User user = new User();
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setEmail("mail!mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsNull() {
        User user = new User();
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenLoginHasSpace() {
        User user = new User();
        user.setLogin("dol ore");
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayIsInFuture() {
        User user = new User();
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(3023, 8, 20));
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
