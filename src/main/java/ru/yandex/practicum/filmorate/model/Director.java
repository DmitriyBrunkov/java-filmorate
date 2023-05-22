package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Director {
    private Integer id;
    @NotBlank
    @Size(max = 255)
    private String name;
}
