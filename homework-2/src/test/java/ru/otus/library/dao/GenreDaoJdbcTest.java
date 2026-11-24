package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GenreDaoJdbcTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    void insertAssignsGeneratedIdAndCanBeRead() {
        long id = genreDao.insert(new Genre(null, "Poetry"));

        assertThat(id).isPositive();
        assertThat(genreDao.findById(id)).contains(new Genre(id, "Poetry"));
    }

    @Test
    void findAllContainsSeedGenres() {
        assertThat(genreDao.findAll())
                .extracting(Genre::name)
                .contains("Novel", "Technical", "Software Engineering");
    }

    @Test
    void updateChangesName() {
        long id = genreDao.insert(new Genre(null, "Old Genre"));

        genreDao.update(new Genre(id, "New Genre"));

        assertThat(genreDao.findById(id)).contains(new Genre(id, "New Genre"));
    }

    @Test
    void deleteByIdRemovesGenre() {
        long id = genreDao.insert(new Genre(null, "Temporary"));

        assertThat(genreDao.deleteById(id)).isTrue();
        assertThat(genreDao.findById(id)).isEmpty();
    }

    @Test
    void deleteByIdReturnsFalseForMissingGenre() {
        assertThat(genreDao.deleteById(9999)).isFalse();
    }

    @Test
    void findByIdReturnsEmptyForMissingGenre() {
        assertThat(genreDao.findById(9999)).isEmpty();
    }
}
