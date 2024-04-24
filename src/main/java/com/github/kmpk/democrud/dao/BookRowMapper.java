package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book result = new Book();
        result.setId(rs.getLong("id"));
        result.setIsbn(rs.getString("isbn"));
        result.setTitle(rs.getString("title"));
        result.setAuthor(rs.getString("author"));
        result.setDescription(rs.getString("Description"));
        return result;
    }

    public Map<String, Object> getParameters(Book book) {
        return Map.of(
                "isbn", book.getIsbn(),
                "title", book.getTitle(),
                "author", book.getAuthor(),
                "description", book.getDescription()
        );
    }
}
