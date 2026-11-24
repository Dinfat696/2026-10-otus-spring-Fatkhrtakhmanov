package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    long insert(Author author);

    void update(Author author);

    boolean deleteById(long id);

    Optional<Author> findById(long id);

    List<Author> findAll();
}
