package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Feed {
    private Integer eventId;
    private Integer userId;
    private Integer entityId;
    private Long timestamp;
    private String eventType;
    private String operation;


}

/*
"timestamp": 123344556,
        "userId": 123,
        "eventType": "LIKE", // одно из значениий LIKE, REVIEW или FRIEND
        "operation": "REMOVE", // одно из значениий REMOVE, ADD, UPDATE
        "eventId": 1234, //primary key
        "entityId": 1234   // идентификатор сущности, с которой произошло событие*/
