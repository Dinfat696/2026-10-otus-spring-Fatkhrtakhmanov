package ru.otus.library.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import java.util.List;
import java.util.Optional;

@ShellComponent
public class BookCommands {

    private static final String[] HEADERS = {"ID", "Title", "Author", "Genre"};

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final TableRenderer tableRenderer;

    public BookCommands(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao, TableRenderer tableRenderer) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.tableRenderer = tableRenderer;
    }

    @ShellMethod(key = "book add", value = "Add a new book")
    public String add(@ShellOption(value = "--title", help = "Book title") String title,
                      @ShellOption(value = "--author-id", help = "Existing author id") long authorId,
                      @ShellOption(value = "--genre-id", help = "Existing genre id") long genreId) {
        Optional<Author> author = authorDao.findById(authorId);
        if (author.isEmpty()) {
            return "Author with id " + authorId + " not found";
        }
        Optional<Genre> genre = genreDao.findById(genreId);
        if (genre.isEmpty()) {
            return "Genre with id " + genreId + " not found";
        }

        long id = bookDao.insert(new Book(null, title, author.get(), genre.get()));
        return "Book created with id " + id;
    }

    @ShellMethod(key = "book list", value = "List all books")
    public String list() {
        List<Book> books = bookDao.findAll();
        if (books.isEmpty()) {
            return "No books found";
        }
        List<String[]> rows = books.stream()
                .map(book -> new String[]{
                        String.valueOf(book.id()),
                        book.title(),
                        book.author().name(),
                        book.genre().name()})
                .toList();
        return tableRenderer.render(HEADERS, rows);
    }

    @ShellMethod(key = "book show", value = "Show a book by id")
    public String show(@ShellOption(value = "--id", help = "Book id") long id) {
        return bookDao.findById(id)
                .map(this::describe)
                .orElse("Book with id " + id + " not found");
    }

    @ShellMethod(key = "book update", value = "Update an existing book")
    public String update(@ShellOption(value = "--id", help = "Book id") long id,
                         @ShellOption(value = "--title", help = "New title") String title,
                         @ShellOption(value = "--author-id", help = "New author id") long authorId,
                         @ShellOption(value = "--genre-id", help = "New genre id") long genreId) {
        if (bookDao.findById(id).isEmpty()) {
            return "Book with id " + id + " not found";
        }
        Optional<Author> author = authorDao.findById(authorId);
        if (author.isEmpty()) {
            return "Author with id " + authorId + " not found";
        }
        Optional<Genre> genre = genreDao.findById(genreId);
        if (genre.isEmpty()) {
            return "Genre with id " + genreId + " not found";
        }

        bookDao.update(new Book(id, title, author.get(), genre.get()));
        return "Book " + id + " updated";
    }

    @ShellMethod(key = "book delete", value = "Delete a book by id")
    public String delete(@ShellOption(value = "--id", help = "Book id") long id) {
        return bookDao.deleteById(id)
                ? "Book " + id + " deleted"
                : "Book with id " + id + " not found";
    }

    private String describe(Book book) {
        return """
                id:     %d
                title:  %s
                author: %s (id %d)
                genre:  %s (id %d)""".formatted(
                book.id(), book.title(),
                book.author().name(), book.author().id(),
                book.genre().name(), book.genre().id());
    }
}
