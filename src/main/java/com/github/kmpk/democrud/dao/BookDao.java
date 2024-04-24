package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookDao {
    Optional<Book> find(long id);

    boolean existsById(long id);

    void delete(long id);

    Book save(Book book);

    void update(Book book);

    Collection<Book> getAll();
}
