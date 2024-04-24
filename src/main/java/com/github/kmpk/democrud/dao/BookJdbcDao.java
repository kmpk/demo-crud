package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
@RequiredArgsConstructor
public class BookJdbcDao implements BookDao {
    public static final String SELECT_QUERY = "SELECT * FROM book WHERE id=?";
    public static final String SELECT_COUNT_QUERY = "SELECT count(*) FROM book WHERE id=?";
    public static final String SELECT_ALL_QUERY = "SELECT * FROM book";
    public static final String DELETE_QUERY = "DELETE FROM book WHERE id=?";
    public static final String UPDATE_QUERY = "UPDATE book SET isbn = ?,title = ?,author = ?,description = ? WHERE id = ?";
    public static final String INSERT_QUERY = "INSERT INTO book(title, isbn, author, description) VALUES (?,?,?,?)";

    private final JdbcTemplate jdbcTemplate;
    private final BookRowMapper mapper = new BookRowMapper();

    @Override
    public Optional<Book> find(long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SELECT_QUERY, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(long id) {
        return jdbcTemplate.queryForObject(SELECT_COUNT_QUERY, Integer.class, id) == 1;
    }

    @Override
    public Collection<Book> getAll() {
        return jdbcTemplate.query(SELECT_ALL_QUERY, mapper);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_QUERY, id);
    }

    @Override
    public Book save(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getDescription());
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return jdbcTemplate.queryForObject(SELECT_QUERY, mapper, keyHolder.getKeys().get("id"));
    }

    @Override
    public void update(Book book) {
        jdbcTemplate.update(UPDATE_QUERY,
                book.getIsbn(), book.getTitle(), book.getAuthor(), book.getDescription(), book.getId());
    }
}
