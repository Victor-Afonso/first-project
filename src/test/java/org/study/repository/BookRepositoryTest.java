package org.study.repository;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.entity.Book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class BookRepositoryTest {

    @InjectMocks
    private BookRepository bookRepository;

    @Mock
    private RedisDataSource ds;

    @Mock
    private ReactiveRedisDataSource reactive;

    @Mock
    private ReactiveKeyCommands<String> keyCommands;

    @Mock
    private ValueCommands<String, Book> countCommands;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(ds.value(String.class, Book.class)).thenReturn(countCommands);
        when(reactive.key()).thenReturn(keyCommands);

        bookRepository = new BookRepository(ds, reactive);
    }

    @Test
    void testFindBookByKey() {
        String key = "bookKey";
        Book book = new Book();
        book.setName("testBook");

        when(countCommands.get(key)).thenReturn(book);

        Book result = bookRepository.findBookByKey(key);

        assertEquals(book, result);
    }

    @Test
    void testCreateBook() {
        Book book = new Book();
        book.setName("testBook");

        bookRepository.createBook(book);

        verify(countCommands, times(1)).set(book.getName(), book);
    }

    @Test
    void testDeleteBook() {
        String key = "bookKey";
        Book book = new Book();
        book.setName("testBook");

        when(countCommands.getdel(key)).thenReturn(book);

        Book result = bookRepository.deleteBook(key);

        assertEquals(book, result);
    }
}
