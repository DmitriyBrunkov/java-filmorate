package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Feed {
    private Integer eventId;
    private Integer userId;
    private Integer entityId;
    private Long timestamp;
    @Size(max = 10)
    private String eventType;
    @Size(max = 10)
    private String operation;
}