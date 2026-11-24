package ru.otus.library.shell;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

import java.util.List;

@ShellComponent
public class GenreCommands {

    private static final String[] HEADERS = {"ID", "Name"};

    private final GenreDao genreDao;
    private final TableRenderer tableRenderer;

    public GenreCommands(GenreDao genreDao, TableRenderer tableRenderer) {
        this.genreDao = genreDao;
        this.tableRenderer = tableRenderer;
    }

    @ShellMethod(key = "genre add", value = "Add a new genre")
    public String add(@ShellOption(value = "--name", help = "Genre name") String name) {
        long id = genreDao.insert(new Genre(null, name));
        return "Genre created with id " + id;
    }

    @ShellMethod(key = "genre list", value = "List all genres")
    public String list() {
        List<Genre> genres = genreDao.findAll();
        if (genres.isEmpty()) {
            return "No genres found";
        }
        List<String[]> rows = genres.stream()
                .map(genre -> new String[]{String.valueOf(genre.id()), genre.name()})
                .toList();
        return tableRenderer.render(HEADERS, rows);
    }

    @ShellMethod(key = "genre delete", value = "Delete a genre by id")
    public String delete(@ShellOption(value = "--id", help = "Genre id") long id) {
        try {
            return genreDao.deleteById(id)
                    ? "Genre " + id + " deleted"
                    : "Genre with id " + id + " not found";
        } catch (DataIntegrityViolationException e) {
            return "Cannot delete genre " + id + ": the genre is referenced by books";
        }
    }
}
