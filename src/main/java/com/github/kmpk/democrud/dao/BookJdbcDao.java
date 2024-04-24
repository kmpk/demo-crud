package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class BookJdbcDao implements BookDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final BookRowMapper mapper = new BookRowMapper();

    public BookJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("book").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Book> find(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM book WHERE id=?", mapper, id));
    }

    @Override
    public boolean existsById(long id) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM book WHERE id=?", Integer.class, id) == 1;
    }

    @Override
    public Collection<Book> getAll() {
        return jdbcTemplate.query("SELECT * FROM book", mapper);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    @Override
    public Book save(Book book) {
        Number number = jdbcInsert.executeAndReturnKey(mapper.getParameters(book));
        book.setId((Long) number);
        return book;
    }

    @Override
    public void update(Book book) {
        jdbcTemplate.update("UPDATE book SET isbn = ?,title = ?,author = ?,description = ? WHERE id = ?",
                book.getIsbn(), book.getTitle(), book.getAuthor(), book.getDescription(), book.getId());
    }
}
