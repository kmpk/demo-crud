package com.github.kmpk.democrud.dao;

import com.github.kmpk.democrud.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.kmpk.democrud.TestData.testBook1;
import static com.github.kmpk.democrud.TestData.testBook2;
import static com.github.kmpk.democrud.TestData.testBooks;
import static com.github.kmpk.democrud.dao.BookJdbcDao.DELETE_QUERY;
import static com.github.kmpk.democrud.dao.BookJdbcDao.SELECT_ALL_QUERY;
import static com.github.kmpk.democrud.dao.BookJdbcDao.SELECT_COUNT_QUERY;
import static com.github.kmpk.democrud.dao.BookJdbcDao.SELECT_QUERY;
import static com.github.kmpk.democrud.dao.BookJdbcDao.UPDATE_QUERY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

class JdbcDaoTest {
    private static JdbcTemplate jdbcTemplate;
    private static BookJdbcDao dao;

    @BeforeAll
    static void init() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        dao = new BookJdbcDao(jdbcTemplate);
    }

    @Test
    void find() {
        long id = 1L;
        Mockito.when(jdbcTemplate.queryForObject(eq(SELECT_QUERY), any(RowMapper.class), eq(id))).thenReturn(testBook1);
        assertEquals(Optional.of(testBook1), dao.find(id));

        id = 2L;
        Mockito.when(jdbcTemplate.queryForObject(eq(SELECT_QUERY), any(RowMapper.class), eq(id))).thenReturn(testBook2);
        assertEquals(Optional.of(testBook2), dao.find(id));
    }

    @Test
    void existsById() {
        long id = 1L;
        Mockito.when(jdbcTemplate.queryForObject(SELECT_COUNT_QUERY, Integer.class, id)).thenReturn(1);
        assertTrue(dao.existsById(id));

        id = 2L;
        Mockito.when(jdbcTemplate.queryForObject(SELECT_COUNT_QUERY, Integer.class, id)).thenReturn(1);
        assertTrue(dao.existsById(id));

        id = 3L;
        Mockito.when(jdbcTemplate.queryForObject(SELECT_COUNT_QUERY, Integer.class, id)).thenReturn(0);
        assertFalse(dao.existsById(id));
    }

    @Test
    void getAll() {
        Mockito.when(jdbcTemplate.query(eq(SELECT_ALL_QUERY), any(RowMapper.class))).thenReturn(testBooks);
        assertEquals(testBooks, dao.getAll());
    }

    @Test
    void delete() {
        long id = 1;
        dao.delete(id);
        Mockito.verify(jdbcTemplate).update(DELETE_QUERY, id);
    }

    @Test
    void save() {
        Mockito.when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(invocation -> {
            List<Map<String, Object>> keyList = invocation.getArgument(1, GeneratedKeyHolder.class).getKeyList();
            keyList.add(Map.of("id", testBook1.getId()));
            return 1;
        });
        Mockito.when(jdbcTemplate.queryForObject(eq(SELECT_QUERY), any(RowMapper.class), anyLong())).thenReturn(testBook1);
        assertEquals(testBook1, dao.save(new Book(null, testBook1.getTitle(), testBook1.getDescription(), testBook1.getAuthor(), testBook1.getIsbn())));
    }

    @Test
    void update() {
        Mockito.when(jdbcTemplate.update(UPDATE_QUERY, testBook1.getIsbn(), testBook1.getTitle(), testBook1.getAuthor(),
                testBook1.getDescription(), testBook1.getId())).thenReturn(1);
        dao.update(testBook1);
        Mockito.verify(jdbcTemplate).update(UPDATE_QUERY, testBook1.getIsbn(), testBook1.getTitle(), testBook1.getAuthor(),
                testBook1.getDescription(), testBook1.getId());
    }
}