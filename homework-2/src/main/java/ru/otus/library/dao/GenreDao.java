package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    long insert(Genre genre);

    void update(Genre genre);

    boolean deleteById(long id);

    Optional<Genre> findById(long id);

    List<Genre> findAll();
}
