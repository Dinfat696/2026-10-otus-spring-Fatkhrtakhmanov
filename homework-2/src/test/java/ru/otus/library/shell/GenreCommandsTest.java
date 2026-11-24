package ru.otus.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreCommandsTest {

    @Mock
    private GenreDao genreDao;

    private GenreCommands genreCommands;

    @BeforeEach
    void setUp() {
        genreCommands = new GenreCommands(genreDao, new TableRenderer());
    }

    @Test
    void addCreatesGenre() {
        when(genreDao.insert(any())).thenReturn(3L);

        assertThat(genreCommands.add("Novel")).isEqualTo("Genre created with id 3");
    }

    @Test
    void listRendersGenres() {
        when(genreDao.findAll()).thenReturn(List.of(
                new Genre(1L, "Novel"),
                new Genre(2L, "Technical")));

        assertThat(genreCommands.list())
                .contains("Novel")
                .contains("Technical");
    }

    @Test
    void listReportsWhenThereAreNoGenres() {
        when(genreDao.findAll()).thenReturn(List.of());

        assertThat(genreCommands.list()).isEqualTo("No genres found");
    }

    @Test
    void deleteReportsMissingGenre() {
        when(genreDao.deleteById(5)).thenReturn(false);

        assertThat(genreCommands.delete(5)).isEqualTo("Genre with id 5 not found");
    }

    @Test
    void deleteRemovesGenre() {
        when(genreDao.deleteById(5)).thenReturn(true);

        assertThat(genreCommands.delete(5)).isEqualTo("Genre 5 deleted");
    }

    @Test
    void deleteReportsReferencedGenre() {
        when(genreDao.deleteById(1)).thenThrow(new DataIntegrityViolationException("fk constraint"));

        assertThat(genreCommands.delete(1))
                .isEqualTo("Cannot delete genre 1: the genre is referenced by books");
    }
}
