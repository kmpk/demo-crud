package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
@Profile("Hibernate")
public class BookHibernateDao implements BookDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Book> find(long id) {
        Book book = entityManager.find(Book.class, id);
        return Optional.ofNullable(book);
    }

    @Override
    public boolean existsById(long id) {
        return entityManager.find(Book.class, id) != null;
    }

    @Override
    public void delete(long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        }
    }

    @Override
    public Book save(Book book) {
        if (book.isNew()) {
            entityManager.persist(book);
        } else {
            entityManager.merge(book);
        }
        return book;
    }

    @Override
    public void update(Book book) {
        entityManager.merge(book);
    }

    @Override
    public Collection<Book> getAll() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();
    }
}
