package ru.yandex.practicum;

public class InvalidWordLengthException extends IllegalArgumentException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
}