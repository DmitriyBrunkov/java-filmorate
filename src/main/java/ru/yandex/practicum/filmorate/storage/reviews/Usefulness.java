package ru.yandex.practicum.filmorate.storage.reviews;

enum Usefulness {
    USEFUL("1"),
    USELESS("-1");

    private final String value;

    Usefulness(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}