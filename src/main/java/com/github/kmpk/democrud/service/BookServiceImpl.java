package com.github.kmpk.democrud.service;

import com.github.kmpk.democrud.dao.BookDao;
import com.github.kmpk.democrud.exception.EntityNotFoundException;
import com.github.kmpk.democrud.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static com.github.kmpk.democrud.config.CacheConfig.BOOKS_CACHE;
import static com.github.kmpk.democrud.config.CacheConfig.BOOK_CACHE;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao dao;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = BOOK_CACHE)
    public Optional<Book> findById(long id) {
        return dao.find(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = BOOKS_CACHE)
    public Collection<Book> getAll() {
        return dao.getAll();
    }

    @Override
    @CacheEvict(cacheNames = BOOKS_CACHE, allEntries = true)
    public Book create(Book book) {
        return dao.save(book);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = BOOK_CACHE, key = "#id"),
            @CacheEvict(cacheNames = BOOKS_CACHE, allEntries = true)})
    public void delete(long id) {
        dao.delete(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = BOOK_CACHE, key = "#book.id"),
            @CacheEvict(cacheNames = BOOKS_CACHE, allEntries = true)})
    public void update(Book book) {
        if (!dao.existsById(book.getId())) {
            throw new EntityNotFoundException("Book with id=" + book.getId() + " is not found");
        }
        dao.update(book);
    }
}
