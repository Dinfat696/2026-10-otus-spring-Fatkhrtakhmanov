package ru.otus.library.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private static final String INSERT_SQL = "INSERT INTO genres (name) VALUES (:name)";
    private static final String UPDATE_SQL = "UPDATE genres SET name = :name WHERE id = :id";
    private static final String DELETE_SQL = "DELETE FROM genres WHERE id = :id";
    private static final String FIND_BY_ID_SQL = "SELECT id, name FROM genres WHERE id = :id";
    private static final String FIND_ALL_SQL = "SELECT id, name FROM genres ORDER BY id";

    private static final RowMapper<Genre> ROW_MAPPER =
            (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name"));

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GenreDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long insert(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL,
                new MapSqlParameterSource("name", genre.name()),
                keyHolder,
                new String[]{"id"});
        return requireGeneratedId(keyHolder);
    }

    @Override
    public void update(Genre genre) {
        jdbcTemplate.update(UPDATE_SQL, new MapSqlParameterSource()
                .addValue("id", genre.id())
                .addValue("name", genre.name()));
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update(DELETE_SQL, Map.of("id", id)) > 0;
    }

    @Override
    public Optional<Genre> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, Map.of("id", id), ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, ROW_MAPPER);
    }

    private long requireGeneratedId(KeyHolder keyHolder) {
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to obtain generated id for a new genre");
        }
        return key.longValue();
    }
}
