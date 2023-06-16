package org.study.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.study.entity.Book;
import org.study.exceptions.DuplicateEntryException;
import org.study.repository.BookRepository;

@ApplicationScoped
public class BookService {

    @Inject
    BookRepository bookRepository;

    public void addBook(Book book) throws DuplicateEntryException {
        if (bookRepository.findBookByKey(book.getName()) != null) {
            throw new DuplicateEntryException();
        }
        bookRepository.createBook(book);
    }

    public Book findBook(String key) {
        return bookRepository.findBookByKey(key);
    }


    public Book removeBook(String key) {
        return bookRepository.deleteBook(key);
    }
}
