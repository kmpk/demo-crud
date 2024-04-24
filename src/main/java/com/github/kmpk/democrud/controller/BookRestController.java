package com.github.kmpk.democrud.controller;

import com.github.kmpk.democrud.exception.IllegalRequestDataException;
import com.github.kmpk.democrud.model.Book;
import com.github.kmpk.democrud.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(BookRestController.REST_URL)
@Validated
@RequiredArgsConstructor
public class BookRestController {
    public static final String REST_URL = "/books";

    private final BookService service;

    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Book book, @PathVariable long id) {
        if (book.isNew() || id != book.getId()) {
            throw new IllegalRequestDataException("Entity id should be " + id);
        }
        service.update(book);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody @Valid Book book) {
        if (!book.isNew()) {
            throw new IllegalRequestDataException("New entity must have no id");
        }
        return service.create(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
