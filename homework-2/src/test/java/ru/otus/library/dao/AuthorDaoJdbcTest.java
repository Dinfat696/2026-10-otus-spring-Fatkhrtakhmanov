package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDao authorDao;

    @Test
    void insertAssignsGeneratedIdAndCanBeRead() {
        long id = authorDao.insert(new Author(null, "Nikolai Gogol"));

        assertThat(id).isPositive();
        assertThat(authorDao.findById(id)).contains(new Author(id, "Nikolai Gogol"));
    }

    @Test
    void findAllContainsSeedAuthors() {
        assertThat(authorDao.findAll())
                .extracting(Author::name)
                .contains("Leo Tolstoy", "Joshua Bloch", "Robert Martin");
    }

    @Test
    void updateChangesName() {
        long id = authorDao.insert(new Author(null, "Old Name"));

        authorDao.update(new Author(id, "New Name"));

        assertThat(authorDao.findById(id)).contains(new Author(id, "New Name"));
    }

    @Test
    void deleteByIdRemovesAuthor() {
        long id = authorDao.insert(new Author(null, "Temporary"));

        assertThat(authorDao.deleteById(id)).isTrue();
        assertThat(authorDao.findById(id)).isEmpty();
    }

    @Test
    void deleteByIdReturnsFalseForMissingAuthor() {
        assertThat(authorDao.deleteById(9999)).isFalse();
    }

    @Test
    void findByIdReturnsEmptyForMissingAuthor() {
        assertThat(authorDao.findById(9999)).isEmpty();
    }
}
