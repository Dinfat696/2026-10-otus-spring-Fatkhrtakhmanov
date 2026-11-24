package ru.otus.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorCommandsTest {

    @Mock
    private AuthorDao authorDao;

    private AuthorCommands authorCommands;

    @BeforeEach
    void setUp() {
        authorCommands = new AuthorCommands(authorDao, new TableRenderer());
    }

    @Test
    void addCreatesAuthor() {
        when(authorDao.insert(any())).thenReturn(7L);

        assertThat(authorCommands.add("Leo Tolstoy")).isEqualTo("Author created with id 7");
    }

    @Test
    void listRendersAuthors() {
        when(authorDao.findAll()).thenReturn(List.of(
                new Author(1L, "Leo Tolstoy"),
                new Author(2L, "Joshua Bloch")));

        assertThat(authorCommands.list())
                .contains("Leo Tolstoy")
                .contains("Joshua Bloch");
    }

    @Test
    void listReportsWhenThereAreNoAuthors() {
        when(authorDao.findAll()).thenReturn(List.of());

        assertThat(authorCommands.list()).isEqualTo("No authors found");
    }

    @Test
    void deleteReportsMissingAuthor() {
        when(authorDao.deleteById(5)).thenReturn(false);

        assertThat(authorCommands.delete(5)).isEqualTo("Author with id 5 not found");
    }

    @Test
    void deleteRemovesAuthor() {
        when(authorDao.deleteById(5)).thenReturn(true);

        assertThat(authorCommands.delete(5)).isEqualTo("Author 5 deleted");
    }

    @Test
    void deleteReportsReferencedAuthor() {
        when(authorDao.deleteById(1)).thenThrow(new DataIntegrityViolationException("fk constraint"));

        assertThat(authorCommands.delete(1))
                .isEqualTo("Cannot delete author 1: the author is referenced by books");
    }
}
