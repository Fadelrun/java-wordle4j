package ru.yandex.practicum;

public class WordNotFoundException extends IllegalArgumentException {
    public WordNotFoundException(String message) {
        super(message);
    }
}