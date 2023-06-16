package org.study.repository;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.study.entity.Book;

@ApplicationScoped
public class BookRepository {

    private final ReactiveKeyCommands<String> keyCommands;
    private final ValueCommands<String, Book> countCommands;

    public BookRepository(RedisDataSource ds, ReactiveRedisDataSource reactive) {
        countCommands = ds.value(String.class, Book.class);
        keyCommands = reactive.key();
    }

    public Book findBookByKey(String key) {
        return countCommands.get(key);
    }

    public void createBook (Book book) {
        countCommands.set(book.getName(), book);
    }

    public Book deleteBook(String key) {
        return countCommands.getdel(key);
    }
}
