package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Integer id;
    @Email
    @Size(max = 255)
    private String email;
    @NotBlank
    @Pattern(regexp = "([^\\s]+)")
    @Size(max = 255)
    private String login;
    @Size(max = 255)
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}
