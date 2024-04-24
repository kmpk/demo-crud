package com.github.kmpk.democrud.controller;

import com.github.kmpk.democrud.exception.IllegalRequestDataException;
import com.github.kmpk.democrud.model.Book;
import com.github.kmpk.democrud.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Book Controller", description = "Controller responsible for operations with books")
public class BookRestController {
    public static final String REST_URL = "/api/books";

    private final BookService service;

    @Operation(summary = "Returns book info",
            description = "Provide an id to get book info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book info returned successfully"),
            @ApiResponse(responseCode = "404", description = "Book with provided id not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> get(@PathVariable long id) {
        return ResponseEntity.of(service.findById(id));
    }

    @Operation(summary = "Returns all books info",
            description = "Retrieve information about all books")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Book> getAll() {
        return service.getAll();
    }


    @Operation(summary = "Updates book info",
            description = "Provide a book id and updated book details to update book info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data provided"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid request body")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Book book, @PathVariable long id) {
        if (book.isNew() || id != book.getId()) {
            throw new IllegalRequestDataException("Entity id should be " + id);
        }
        service.update(book);
    }

    @Operation(summary = "Creates new book",
            description = "Provide details of the new book to create. Book id must be omitted or set to null")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request, invalid data provided"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity, invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody @Valid Book book) {
        if (!book.isNew()) {
            throw new IllegalRequestDataException("New entity must have no id");
        }
        return service.create(book);
    }

    @Operation(summary = "Removes book",
            description = "Provide a book id to remove")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }
}
