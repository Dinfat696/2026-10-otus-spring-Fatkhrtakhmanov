package ru.otus.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookCommandsTest {

    @Mock
    private BookDao bookDao;

    @Mock
    private AuthorDao authorDao;

    @Mock
    private GenreDao genreDao;

    private BookCommands bookCommands;

    @BeforeEach
    void setUp() {
        bookCommands = new BookCommands(bookDao, authorDao, genreDao, new TableRenderer());
    }

    @Test
    void addFailsWhenAuthorIsMissing() {
        when(authorDao.findById(99)).thenReturn(Optional.empty());

        assertThat(bookCommands.add("Title", 99, 1))
                .isEqualTo("Author with id 99 not found");
        verifyNoInteractions(bookDao);
    }

    @Test
    void addFailsWhenGenreIsMissing() {
        when(authorDao.findById(1)).thenReturn(Optional.of(new Author(1L, "Author")));
        when(genreDao.findById(2)).thenReturn(Optional.empty());

        assertThat(bookCommands.add("Title", 1, 2))
                .isEqualTo("Genre with id 2 not found");
        verify(bookDao, never()).insert(any());
    }

    @Test
    void addCreatesBook() {
        when(authorDao.findById(1)).thenReturn(Optional.of(new Author(1L, "Author")));
        when(genreDao.findById(2)).thenReturn(Optional.of(new Genre(2L, "Genre")));
        when(bookDao.insert(any())).thenReturn(10L);

        assertThat(bookCommands.add("Title", 1, 2))
                .isEqualTo("Book created with id 10");
    }

    @Test
    void listRendersBooks() {
        when(bookDao.findAll()).thenReturn(List.of(
                new Book(1L, "War and Peace", new Author(1L, "Leo Tolstoy"), new Genre(1L, "Novel"))));

        assertThat(bookCommands.list())
                .contains("War and Peace")
                .contains("Leo Tolstoy")
                .contains("Novel");
    }

    @Test
    void listReportsWhenThereAreNoBooks() {
        when(bookDao.findAll()).thenReturn(List.of());

        assertThat(bookCommands.list()).isEqualTo("No books found");
    }

    @Test
    void showReturnsBookDetails() {
        when(bookDao.findById(1)).thenReturn(Optional.of(
                new Book(1L, "War and Peace", new Author(1L, "Leo Tolstoy"), new Genre(1L, "Novel"))));

        assertThat(bookCommands.show(1))
                .contains("War and Peace")
                .contains("Leo Tolstoy")
                .contains("Novel");
    }

    @Test
    void showReportsMissingBook() {
        when(bookDao.findById(5)).thenReturn(Optional.empty());

        assertThat(bookCommands.show(5)).isEqualTo("Book with id 5 not found");
    }

    @Test
    void updateFailsWhenBookIsMissing() {
        when(bookDao.findById(5)).thenReturn(Optional.empty());

        assertThat(bookCommands.update(5, "Title", 1, 1))
                .isEqualTo("Book with id 5 not found");
        verify(bookDao, never()).update(any());
    }

    @Test
    void updateChangesBook() {
        when(bookDao.findById(5)).thenReturn(Optional.of(
                new Book(5L, "Old", new Author(1L, "A"), new Genre(1L, "G"))));
        when(authorDao.findById(2)).thenReturn(Optional.of(new Author(2L, "New Author")));
        when(genreDao.findById(3)).thenReturn(Optional.of(new Genre(3L, "New Genre")));

        assertThat(bookCommands.update(5, "New", 2, 3)).isEqualTo("Book 5 updated");
        verify(bookDao).update(new Book(5L, "New", new Author(2L, "New Author"), new Genre(3L, "New Genre")));
    }

    @Test
    void deleteReportsMissingBook() {
        when(bookDao.deleteById(5)).thenReturn(false);

        assertThat(bookCommands.delete(5)).isEqualTo("Book with id 5 not found");
    }

    @Test
    void deleteRemovesBook() {
        when(bookDao.deleteById(5)).thenReturn(true);

        assertThat(bookCommands.delete(5)).isEqualTo("Book 5 deleted");
    }
}
