package com.github.kmpk.democrud.service;

import com.github.kmpk.democrud.dao.BookDao;
import com.github.kmpk.democrud.exception.EntityNotFoundException;
import com.github.kmpk.democrud.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao dao;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return dao.find(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Book> getAll() {
        return dao.getAll();
    }

    @Override
    public Book create(Book book) {
        return dao.save(book);
    }

    @Override
    public void delete(long id) {
        dao.delete(id);
    }

    @Override
    public void update(Book book) {
        if (!dao.existsById(book.getId())) {
            throw new EntityNotFoundException("Book with id=" + book.getId() + " is not found");
        }
        dao.update(book);
    }
}
