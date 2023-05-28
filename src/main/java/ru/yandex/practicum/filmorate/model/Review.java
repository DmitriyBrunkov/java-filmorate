package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Integer reviewId;
    @NotBlank(message = "Отзыв не может быть пустым")
    @Size(max = 500, message = "Отзыв не может содержать более 500 символов")
    private String content;
    @NotNull
    private Boolean isPositive; //тип отзыва — негативный/положительный
    @NotNull
    private Integer userId;
    @NotNull
    private Integer filmId;
    private Integer useful; //рейтинг полезности
}