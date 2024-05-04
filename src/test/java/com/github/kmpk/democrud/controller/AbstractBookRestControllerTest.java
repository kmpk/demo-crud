package com.github.kmpk.democrud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kmpk.democrud.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.kmpk.democrud.TestData.testBook1;
import static com.github.kmpk.democrud.TestData.testBook2;
import static com.github.kmpk.democrud.TestData.testBooks;
import static com.github.kmpk.democrud.controller.BookRestController.REST_URL;
import static org.hamcrest.core.IsNot.not;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.text.IsEmptyString.emptyString;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15.5:///test-db"})
@AutoConfigureMockMvc
abstract class AbstractBookRestControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void get() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + 111))
                .andExpect(status().isNotFound())
                .andDo(print());

        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + testBook1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(testBook1)))
                .andDo(print());

        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + testBook2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(testBook2)))
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(testBooks)))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void update() throws Exception {
        Book book = new Book(testBook1.getId(), "newTitle", "newDescription", "newAuthor", "newIsbn");

        mvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isNoContent())
                .andDo(print());

        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(book)))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void updateInvalid() throws Exception {
        Book book = new Book(testBook1.getId(), null, null, "", "");

        mvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail", not(emptyString())))
                .andExpect(jsonPath("invalid_params", not(emptyString())))
                .andDo(print());

        book = new Book(testBook1.getId(), "newTitle", "newDescription", "newAuthor", "newIsbn");

        mvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + book.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail", not(emptyString())))
                .andDo(print());

        book = new Book(testBook1.getId(), "newTitle", "newDescription", "newAuthor", testBook2.getIsbn());

        mvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail", not(emptyString())))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void create() throws Exception {
        Book book = new Book(null, "newTitle", "newDescription", "newAuthor", "newIsbn");

        Book returned = mapper.readValue(mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString(), Book.class);

        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + returned.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(returned)))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void createInvalid() throws Exception {
        Book book = new Book(null, null, null, "", "");

        mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail", not(emptyString())))
                .andExpect(jsonPath("invalid_params", not(emptyString())))
                .andDo(print());

        book = new Book(testBook1.getId(), "newTitle", "newDescription", "newAuthor", "newIsbn");

        mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail", not(emptyString())))
                .andDo(print());

        book = new Book(null, "newTitle", "newDescription", "newAuthor", testBook2.getIsbn());

        mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail", not(emptyString())))
                .andDo(print());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + testBook1.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());


        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + testBook1.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}