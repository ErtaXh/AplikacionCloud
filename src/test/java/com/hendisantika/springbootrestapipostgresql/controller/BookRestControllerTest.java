package com.hendisantika.springbootrestapipostgresql.controller;

import com.hendisantika.springbootrestapipostgresql.entity.Book;
import com.hendisantika.springbootrestapipostgresql.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
@ExtendWith(MockitoExtension.class)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;

    List<String> tags=new ArrayList<String>();

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setName("Book1");
        book1.setDescription("Description1");

        tags.add("Tag1");
        tags.add("Tag2");
        book1.setTags(tags);

        book2 = new Book();
        book2.setId(2L);
        book2.setName("Book2");
        book2.setDescription("Description2");
        book2.setTags(tags);
    }


    @Test
    void testGetAllBooks() throws Exception {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }


    @Test
    void testFindBookWithName() throws Exception {
        when(bookRepository.findByName(eq("Book1"))).thenReturn(Arrays.asList(book1));

        mockMvc.perform(get("/api/books")
                        .param("name", "Book1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Book1"));
    }


    @Test
    void testDeleteBookWithId() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAllBooks() throws Exception {
        mockMvc.perform(delete("/api/books"))
                .andExpect(status().isOk());
    }
}
