package com.hendisantika.springbootrestapipostgresql.controller;

import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorRestController.class)
@ExtendWith(MockitoExtension.class)
public class AuthorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorRepository authorRepository;

    private Author author1;
    private Author author2;

    @BeforeEach
    void setUp() {
        author1 = new Author();
        author1.setId(1L);
        author1.setName("Author1");
        author1.setSurname("Surname1");

        author2 = new Author();
        author2.setId(2L);
        author2.setName("Author2");
        author2.setSurname("Surname2");
    }

    @Test
    void testAddAuthor() throws Exception {
        when(authorRepository.save(any(Author.class))).thenReturn(author1);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Author1\",\"surname\":\"Surname1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Author1"))
                .andExpect(jsonPath("$.surname").value("Surname1"));
    }

    @Test
    void testGetAllAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testGetAuthorWithId() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author1));

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Author1"))
                .andExpect(jsonPath("$.surname").value("Surname1"));
    }

    @Test
    void testFindAuthorWithName() throws Exception {
        when(authorRepository.findByName("Author1")).thenReturn(Arrays.asList(author1));

        mockMvc.perform(get("/api/authors")
                        .param("name", "Author1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testUpdateAuthorFromDB() throws Exception {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author1));
        when(authorRepository.save(any(Author.class))).thenReturn(author1);

        mockMvc.perform(put("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\",\"surname\":\"UpdatedSurname\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.surname").value("UpdatedSurname"));
    }

    @Test
    void testDeleteAuthorWithId() throws Exception {
        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAllAuthors() throws Exception {
        mockMvc.perform(delete("/api/authors"))
                .andExpect(status().isOk());
    }
}