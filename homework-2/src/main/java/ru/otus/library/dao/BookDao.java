package ru.otus.library.dao;

import ru.otus.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    long insert(Book book);

    void update(Book book);

    boolean deleteById(long id);

    Optional<Book> findById(long id);

    List<Book> findAll();
}
