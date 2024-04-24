package com.github.kmpk.democrud;

import com.github.kmpk.democrud.model.Book;

import java.util.List;

public class TestData {
    public static final Book testBook1 = new Book(1L, "title1", "description1", "author1", "isbn1");
    public static final Book testBook2 = new Book(2L, "title2", "description2", "author2", "isbn2");
    public static final List<Book> testBooks = List.of(testBook1, testBook2);
}
