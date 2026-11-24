package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookDaoJdbcTest {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private GenreDao genreDao;

    @Test
    void findByIdReturnsBookWithAuthorAndGenre() {
        Book seed = bookDao.findAll().stream()
                .filter(book -> book.title().equals("War and Peace"))
                .findFirst()
                .orElseThrow();

        Book found = bookDao.findById(seed.id()).orElseThrow();

        assertThat(found.title()).isEqualTo("War and Peace");
        assertThat(found.author().name()).isEqualTo("Leo Tolstoy");
        assertThat(found.genre().name()).isEqualTo("Novel");
    }

    @Test
    void findAllContainsSeedBooks() {
        assertThat(bookDao.findAll())
                .extracting(Book::title)
                .contains("War and Peace", "Effective Java", "Clean Code");
    }

    @Test
    void insertStoresBookWithAuthorAndGenre() {
        long authorId = authorDao.insert(new Author(null, "Frank Herbert"));
        long genreId = genreDao.insert(new Genre(null, "Science Fiction"));

        long id = bookDao.insert(new Book(null, "Dune",
                new Author(authorId, "Frank Herbert"), new Genre(genreId, "Science Fiction")));

        Book stored = bookDao.findById(id).orElseThrow();
        assertThat(stored.title()).isEqualTo("Dune");
        assertThat(stored.author()).isEqualTo(new Author(authorId, "Frank Herbert"));
        assertThat(stored.genre()).isEqualTo(new Genre(genreId, "Science Fiction"));
    }

    @Test
    void updateChangesTitleAuthorAndGenre() {
        long oldAuthorId = authorDao.insert(new Author(null, "Old Author"));
        long oldGenreId = genreDao.insert(new Genre(null, "Old Genre"));
        long id = bookDao.insert(new Book(null, "Old Title",
                new Author(oldAuthorId, "Old Author"), new Genre(oldGenreId, "Old Genre")));

        long newAuthorId = authorDao.insert(new Author(null, "New Author"));
        long newGenreId = genreDao.insert(new Genre(null, "New Genre"));
        bookDao.update(new Book(id, "New Title",
                new Author(newAuthorId, "New Author"), new Genre(newGenreId, "New Genre")));

        Book updated = bookDao.findById(id).orElseThrow();
        assertThat(updated.title()).isEqualTo("New Title");
        assertThat(updated.author()).isEqualTo(new Author(newAuthorId, "New Author"));
        assertThat(updated.genre()).isEqualTo(new Genre(newGenreId, "New Genre"));
    }

    @Test
    void deleteByIdRemovesBook() {
        long authorId = authorDao.insert(new Author(null, "Author"));
        long genreId = genreDao.insert(new Genre(null, "Genre"));
        long id = bookDao.insert(new Book(null, "Temporary", new Author(authorId, "Author"), new Genre(genreId, "Genre")));

        assertThat(bookDao.deleteById(id)).isTrue();
        assertThat(bookDao.findById(id)).isEmpty();
    }

    @Test
    void deleteByIdReturnsFalseForMissingBook() {
        assertThat(bookDao.deleteById(9999)).isFalse();
    }

    @Test
    void findByIdReturnsEmptyForMissingBook() {
        assertThat(bookDao.findById(9999)).isEmpty();
    }
}
