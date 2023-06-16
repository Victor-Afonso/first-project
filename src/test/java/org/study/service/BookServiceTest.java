package org.study.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.entity.Book;
import org.study.exceptions.DuplicateEntryException;
import org.study.repository.BookRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBookThrowsExceptionForDuplicateBook() throws DuplicateEntryException {
        Book book = new Book();
        book.setName("test_1f8b720a67d8");
        book.setPages(34);
        book.setAuthor("AuthorTest");
        book.setCreatedAt(LocalDate.of(2015, 2, 8));

        when(bookRepository.findBookByKey(book.getName())).thenReturn(book);

        assertThrows(DuplicateEntryException.class, () -> bookService.addBook(book));
    }

    @Test
    void testAddBookSuccess() throws DuplicateEntryException {
        Book book = new Book();
        book.setName("test_1f8b720a67d8");
        book.setPages(34);
        book.setAuthor("AuthorTest");
        book.setCreatedAt(LocalDate.of(2015, 2, 8));


        when(bookRepository.findBookByKey(book.getName())).thenReturn(null);

        bookService.addBook(book);

        verify(bookRepository, times(1)).createBook(book);
    }

    @Test
    void testFindBookSuccess() {
        Book book = new Book();
        book.setName("test_1f8b720a67d8");
        book.setPages(34);
        book.setAuthor("AuthorTest");
        book.setCreatedAt(LocalDate.of(2015, 2, 8));


        when(bookRepository.findBookByKey(book.getName())).thenReturn(book);

        Book result = bookService.findBook(book.getName());

        assertEquals(book, result);
    }

    @Test
    void testRemoveBookSuccess() {
        String bookName = "testBook";
        Book book = new Book();
        book.setName(bookName);

        when(bookRepository.deleteBook(bookName)).thenReturn(book);

        Book result = bookService.removeBook(bookName);

        assertEquals(book, result);
    }
}
