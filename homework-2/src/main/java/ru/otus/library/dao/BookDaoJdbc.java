package ru.otus.library.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookDaoJdbc implements BookDao {

    private static final String BOOK_COLUMNS = """
            SELECT b.id, b.title,
                   a.id AS author_id, a.name AS author_name,
                   g.id AS genre_id, g.name AS genre_name
            FROM books b
                     JOIN authors a ON a.id = b.author_id
                     JOIN genres g ON g.id = b.genre_id
            """;
    private static final String INSERT_SQL =
            "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :authorId, :genreId)";
    private static final String UPDATE_SQL =
            "UPDATE books SET title = :title, author_id = :authorId, genre_id = :genreId WHERE id = :id";
    private static final String DELETE_SQL = "DELETE FROM books WHERE id = :id";
    private static final String FIND_BY_ID_SQL = BOOK_COLUMNS + " WHERE b.id = :id";
    private static final String FIND_ALL_SQL = BOOK_COLUMNS + " ORDER BY b.id";

    private static final RowMapper<Book> ROW_MAPPER = (rs, rowNum) -> new Book(
            rs.getLong("id"),
            rs.getString("title"),
            new Author(rs.getLong("author_id"), rs.getString("author_name")),
            new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL,
                new MapSqlParameterSource()
                        .addValue("title", book.title())
                        .addValue("authorId", book.author().id())
                        .addValue("genreId", book.genre().id()),
                keyHolder,
                new String[]{"id"});
        return requireGeneratedId(keyHolder);
    }

    @Override
    public void update(Book book) {
        jdbcTemplate.update(UPDATE_SQL, new MapSqlParameterSource()
                .addValue("id", book.id())
                .addValue("title", book.title())
                .addValue("authorId", book.author().id())
                .addValue("genreId", book.genre().id()));
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_SQL, Map.of("id", id)) > 0;
    }

    @Override
    public Optional<Book> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, Map.of("id", id), ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, ROW_MAPPER);
    }

    private long requireGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to obtain generated id for a new book");
        }
        return key.longValue();
    }
}
