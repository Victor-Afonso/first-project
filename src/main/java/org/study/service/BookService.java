package org.study.service;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.study.entity.Book;
import org.study.exceptions.DuplicateEntryException;

import java.util.UUID;

@ApplicationScoped
public class BookService {

    private final ReactiveKeyCommands<String> keyCommands;
    private final ValueCommands<String, Book> countCommands;



    public BookService(RedisDataSource ds, ReactiveRedisDataSource reactive) {
        countCommands = ds.value(String.class, Book.class);
        keyCommands = reactive.key();

    }
    public void addBook(Book book) throws DuplicateEntryException {
        if (countCommands.get(book.getName()) != null) {
            throw new DuplicateEntryException();
        }
        countCommands.set(book.getName(), book);
    }

    public Book findBook(String name) {
        return countCommands.get(name);
    }


    public Book removeBook(String name) {
        return countCommands.getdel(name);
    }
}
