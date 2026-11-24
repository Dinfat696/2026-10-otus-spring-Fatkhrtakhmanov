package ru.otus.library.domain;

public record Book(Long id, String title, Author author, Genre genre) {
}
