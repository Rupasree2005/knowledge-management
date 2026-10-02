package com.example.knowledge_management.controller;

import com.example.knowledge_management.dto.KnowledgeDocumentRequest;
import com.example.knowledge_management.dto.KnowledgeDocumentResponse;
import com.example.knowledge_management.repo.UserRepository;
import com.example.knowledge_management.service.KnowledgeDocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import com.example.knowledge_management.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(KnowledgeDocumentController.class)
class KnowledgeDocumentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KnowledgeDocumentService service;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserRepository userRepository;
    @TestConfiguration
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    void createDocument_shouldReturnCreatedDocument() throws Exception {

        // Arrange
        KnowledgeDocumentRequest request =
                new KnowledgeDocumentRequest();

        request.setTitle("Java Basics");
        request.setContent(
                "Java is an object-oriented programming language."
        );
        request.setCategory("Java");

        KnowledgeDocumentResponse response =
                new KnowledgeDocumentResponse();

        response.setId(1L);
        response.setTitle("Java Basics");
        response.setContent(
                "Java is an object-oriented programming language."
        );
        response.setCategory("Java");

        when(service.createDocument(any(KnowledgeDocumentRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                        post("/api/documents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.content")
                        .value(
                                "Java is an object-oriented programming language."
                        ))
                .andExpect(jsonPath("$.category").value("Java"));

        // Verify
        verify(service)
                .createDocument(any(KnowledgeDocumentRequest.class));
    }
    @Test
    void getDocumentById_shouldReturnDocument() throws Exception {

        // Arrange
        KnowledgeDocumentResponse response =
                new KnowledgeDocumentResponse();

        response.setId(1L);
        response.setTitle("Java Basics");
        response.setContent(
                "Java is an object-oriented programming language."
        );
        response.setCategory("Java");

        when(service.getDocumentById(1L))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                        get("/api/documents/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.content")
                        .value(
                                "Java is an object-oriented programming language."
                        ))
                .andExpect(jsonPath("$.category").value("Java"));

        // Verify
        verify(service).getDocumentById(1L);
    }
    @Test
    void getAllDocuments_shouldReturnDocuments() throws Exception {

        // Arrange
        KnowledgeDocumentResponse document1 =
                new KnowledgeDocumentResponse();

        document1.setId(1L);
        document1.setTitle("Java Basics");
        document1.setContent(
                "Java is an object-oriented programming language."
        );
        document1.setCategory("Java");

        KnowledgeDocumentResponse document2 =
                new KnowledgeDocumentResponse();

        document2.setId(2L);
        document2.setTitle("Spring Boot");
        document2.setContent(
                "Spring Boot is used to build Java applications."
        );
        document2.setCategory("Spring");

        Page<KnowledgeDocumentResponse> page =
                new PageImpl<>(List.of(document1, document2));

        when(service.getAllDocuments(any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(
                        get("/api/documents")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Java Basics"))
                .andExpect(jsonPath("$.content[0].category")
                        .value("Java"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title")
                        .value("Spring Boot"))
                .andExpect(jsonPath("$.content[1].category")
                        .value("Spring"));

        // Verify
        verify(service).getAllDocuments(any(Pageable.class));
    }
    @Test
    void searchDocuments_shouldReturnMatchingDocuments() throws Exception {

        // Arrange
        KnowledgeDocumentResponse response =
                new KnowledgeDocumentResponse();

        response.setId(1L);
        response.setTitle("Java Basics");
        response.setContent(
                "Java is an object-oriented programming language."
        );
        response.setCategory("Java");

        Page<KnowledgeDocumentResponse> page =
                new PageImpl<>(List.of(response));

        when(service.searchByTitle(
                org.mockito.ArgumentMatchers.eq("Java"),
                any(Pageable.class)
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(
                        get("/api/documents/search")
                                .param("keyword", "Java")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Java Basics"))
                .andExpect(jsonPath("$.content[0].category")
                        .value("Java"));

        // Verify
        verify(service).searchByTitle(
                org.mockito.ArgumentMatchers.eq("Java"),
                any(Pageable.class)
        );
    }
    @Test
    void getDocumentsByCategory_shouldReturnMatchingDocuments() throws Exception {

        // Arrange
        KnowledgeDocumentResponse document1 =
                new KnowledgeDocumentResponse();

        document1.setId(1L);
        document1.setTitle("Java Basics");
        document1.setContent(
                "Java is an object-oriented programming language."
        );
        document1.setCategory("Java");

        KnowledgeDocumentResponse document2 =
                new KnowledgeDocumentResponse();

        document2.setId(2L);
        document2.setTitle("Advanced Java");
        document2.setContent(
                "Advanced concepts in Java."
        );
        document2.setCategory("Java");

        Page<KnowledgeDocumentResponse> page =
                new PageImpl<>(List.of(document1, document2));

        when(service.filterByCategory(
                any(String.class),
                any(Pageable.class)
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(
                        get("/api/documents/category/Java")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Java Basics"))
                .andExpect(jsonPath("$.content[0].category")
                        .value("Java"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title")
                        .value("Advanced Java"))
                .andExpect(jsonPath("$.content[1].category")
                        .value("Java"));

        // Verify
        verify(service).filterByCategory(
                any(String.class),
                any(Pageable.class)
        );
    }
    @Test
    void filterDocuments_shouldReturnMatchingDocuments() throws Exception {

        // Arrange
        KnowledgeDocumentResponse document1 =
                new KnowledgeDocumentResponse();

        document1.setId(1L);
        document1.setTitle("Java Basics");
        document1.setContent(
                "Java is an object-oriented programming language."
        );
        document1.setCategory("Java");

        KnowledgeDocumentResponse document2 =
                new KnowledgeDocumentResponse();

        document2.setId(2L);
        document2.setTitle("Advanced Java");
        document2.setContent(
                "Advanced concepts in Java."
        );
        document2.setCategory("Java");

        Page<KnowledgeDocumentResponse> page =
                new PageImpl<>(List.of(document1, document2));

        when(service.filterByKeywordAndCategory(
                any(String.class),
                any(String.class),
                any(Pageable.class)
        )).thenReturn(page);

        // Act & Assert
        mockMvc.perform(
                        get("/api/documents/filter")
                                .param("keyword", "Java")
                                .param("category", "Java")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Java Basics"))
                .andExpect(jsonPath("$.content[0].category")
                        .value("Java"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title")
                        .value("Advanced Java"))
                .andExpect(jsonPath("$.content[1].category")
                        .value("Java"));

        // Verify
        verify(service).filterByKeywordAndCategory(
                any(String.class),
                any(String.class),
                any(Pageable.class)
        );
    }
    @Test
    void updateDocument_shouldReturnUpdatedDocument() throws Exception {

        // Arrange
        KnowledgeDocumentRequest request =
                new KnowledgeDocumentRequest();

        request.setTitle("Updated Java Basics");
        request.setContent(
                "Updated content about Java."
        );
        request.setCategory("Java");

        KnowledgeDocumentResponse response =
                new KnowledgeDocumentResponse();

        response.setId(1L);
        response.setTitle("Updated Java Basics");
        response.setContent(
                "Updated content about Java."
        );
        response.setCategory("Java");

        when(service.updateDocument(
                any(Long.class),
                any(KnowledgeDocumentRequest.class)
        )).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                        put("/api/documents/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Updated Java Basics"))
                .andExpect(jsonPath("$.content")
                        .value("Updated content about Java."))
                .andExpect(jsonPath("$.category")
                        .value("Java"));

        // Verify
        verify(service).updateDocument(
                any(Long.class),
                any(KnowledgeDocumentRequest.class)
        );
    }
    @Test
    void deleteDocument_shouldReturnNoContent() throws Exception {

        // Arrange
        // No return value because deleteDocument() returns void
        doNothing().when(service).deleteDocument(1L);

        // Act & Assert
        mockMvc.perform(
                        delete("/api/documents/1")
                )
                .andExpect(status().isNoContent());

        // Verify
        verify(service).deleteDocument(1L);
    }
}