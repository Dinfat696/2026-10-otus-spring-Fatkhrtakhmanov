package ru.otus.quiz.dao;

public class QuizDataException extends RuntimeException {

    public QuizDataException(String message) {
        super(message);
    }

    public QuizDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
