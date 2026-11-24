package ru.otus.library.shell;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

import java.util.List;

@ShellComponent
public class AuthorCommands {

    private static final String[] HEADERS = {"ID", "Name"};

    private final AuthorDao authorDao;
    private final TableRenderer tableRenderer;

    public AuthorCommands(AuthorDao authorDao, TableRenderer tableRenderer) {
        this.authorDao = authorDao;
        this.tableRenderer = tableRenderer;
    }

    @ShellMethod(key = "author add", value = "Add a new author")
    public String add(@ShellOption(value = "--name", help = "Author name") String name) {
        long id = authorDao.insert(new Author(null, name));
        return "Author created with id " + id;
    }

    @ShellMethod(key = "author list", value = "List all authors")
    public String list() {
        List<Author> authors = authorDao.findAll();
        if (authors.isEmpty()) {
            return "No authors found";
        }
        List<String[]> rows = authors.stream()
                .map(author -> new String[]{String.valueOf(author.id()), author.name()})
                .toList();
        return tableRenderer.render(HEADERS, rows);
    }

    @ShellMethod(key = "author delete", value = "Delete an author by id")
    public String delete(@ShellOption(value = "--id", help = "Author id") long id) {
        try {
            return authorDao.deleteById(id)
                    ? "Author " + id + " deleted"
                    : "Author with id " + id + " not found";
        } catch (DataIntegrityViolationException e) {
            return "Cannot delete author " + id + ": the author is referenced by books";
        }
    }
}
