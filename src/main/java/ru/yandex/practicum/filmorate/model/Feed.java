package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Feed {
    private Integer eventId;
    private Integer userId;
    private Integer entityId;
    private Long timestamp;
    private String eventType;
    private String operation;
}