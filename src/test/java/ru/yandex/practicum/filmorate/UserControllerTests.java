package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DataBinder;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
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
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        userController.add(user);
        for (User userFromCollection : userController.getAll()) {
            if (userFromCollection.getId().equals(user.getId())) {
                assertEquals(user, userFromCollection);
            }
        }
    }

    @Test
    public void shouldSubstituteNameWithLoginWhenNameIsEmpty() {
        User user = User.builder()
                .login("dolore")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        userController.add(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void shouldThrowExceptionWhenPutNonExistentId() {
        User user = User.builder()
                .id(1234)
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        final UserValidationException exception = assertThrows(UserValidationException.class,
                () -> userController.put(user)
        );
        assertEquals("ID: " + user.getId() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenWrongEmail() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail!mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsNull() {
        User user = User.builder()
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenLoginHasSpace() {
        User user = User.builder()
                .login("dol ore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayIsInFuture() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(3023, 8, 20))
                .build();
        final DataBinder dataBinder = new DataBinder(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
