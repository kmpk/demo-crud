package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Profile("JPA")
@RequiredArgsConstructor
public class BookDaoJpaAdapter implements BookDao {
    private final BookJpaRepository repository;

    @Override
    public Optional<Book> find(long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    public void update(Book book) {
        repository.save(book);
    }

    @Override
    public Collection<Book> getAll() {
        return repository.findAll();
    }
}
