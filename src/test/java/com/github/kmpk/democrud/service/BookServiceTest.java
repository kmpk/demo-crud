package com.github.kmpk.democrud.service;

import com.github.kmpk.democrud.dao.BookDao;
import com.github.kmpk.democrud.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.github.kmpk.democrud.TestData.testBook1;
import static com.github.kmpk.democrud.TestData.testBook2;
import static com.github.kmpk.democrud.TestData.testBooks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookServiceTest {

    private static BookDao dao;
    private static BookService bookService;

    @BeforeAll
    static void init() {
        dao = Mockito.mock(BookDao.class);
        bookService = new BookServiceImpl(dao);
    }

    @Test
    void findById() {
        long id = 1L;
        when(dao.find(id)).thenReturn(Optional.of(testBook1));
        assertEquals(Optional.of(testBook1), bookService.findById(id));

        id = 2L;
        when(dao.find(id)).thenReturn(Optional.of(testBook2));
        assertEquals(Optional.of(testBook2), bookService.findById(id));
    }

    @Test
    void getAll() {
        when(dao.getAll()).thenReturn(testBooks);
        assertEquals(testBooks, bookService.getAll());
    }

    @Test
    void create() {
        Book book = new Book(null, testBook1.getTitle(), testBook1.getDescription(), testBook1.getAuthor(), testBook1.getIsbn());
        when(dao.save(book))
                .thenReturn(testBook1);
        assertEquals(testBook1, bookService.create(book));
    }

    @Test
    void delete() {
        long id = 1L;
        doNothing().when(dao).delete(id);
        bookService.delete(id);
        verify(dao).delete(id);
    }

    @Test
    void update() {
        doNothing().when(dao).update(testBook1);
        when(dao.existsById(testBook1.getId())).thenReturn(true);
        bookService.update(testBook1);
        verify(dao).update(testBook1);
        verify(dao).existsById(testBook1.getId());
    }
}