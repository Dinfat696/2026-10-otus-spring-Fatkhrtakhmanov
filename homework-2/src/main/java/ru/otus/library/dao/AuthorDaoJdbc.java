package ru.otus.library.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private static final String INSERT_SQL = "INSERT INTO authors (name) VALUES (:name)";
    private static final String UPDATE_SQL = "UPDATE authors SET name = :name WHERE id = :id";
    private static final String DELETE_SQL = "DELETE FROM authors WHERE id = :id";
    private static final String FIND_BY_ID_SQL = "SELECT id, name FROM authors WHERE id = :id";
    private static final String FIND_ALL_SQL = "SELECT id, name FROM authors ORDER BY id";

    private static final RowMapper<Author> ROW_MAPPER =
            (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("name"));

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AuthorDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insert(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL,
                new MapSqlParameterSource("name", author.name()),
                keyHolder,
                new String[]{"id"});
        return requireGeneratedId(keyHolder);
    }

    @Override
    public void update(Author author) {
        jdbcTemplate.update(UPDATE_SQL, new MapSqlParameterSource()
                .addValue("id", author.id())
                .addValue("name", author.name()));
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_SQL, Map.of("id", id)) > 0;
    }

    @Override
    public Optional<Author> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, Map.of("id", id), ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, ROW_MAPPER);
    }

    private long requireGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to obtain generated id for a new author");
        }
        return key.longValue();
    }
}
