package com.github.kmpk.democrud.service;

import com.github.kmpk.democrud.model.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookService {
    Optional<Book> findById(long id);

    Collection<Book> getAll();

    Book create(Book book);

    void delete(long id);

    void update(Book book);
}
