package com.example.knowledge_management.service;

import com.example.knowledge_management.dto.KnowledgeDocumentRequest;
import com.example.knowledge_management.dto.KnowledgeDocumentResponse;
import com.example.knowledge_management.entity.KnowledgeDocument;
import com.example.knowledge_management.exception.DocumentNotFoundException;
import com.example.knowledge_management.repo.KnowledgeDocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class KnowledgeDocumentServiceTest {

    @Mock
    private KnowledgeDocumentRepository repository;

    @InjectMocks
    private KnowledgeDocumentService service;

    @Test
    void shouldCreateDocument() {

        // Arrange
        KnowledgeDocumentRequest request =
                new KnowledgeDocumentRequest();

        request.setTitle("Java Basics");
        request.setContent(
                "Java is an object-oriented programming language."
        );
        request.setCategory("Java");

        KnowledgeDocument savedDocument =
                new KnowledgeDocument();

        savedDocument.setId(1L);
        savedDocument.setTitle("Java Basics");
        savedDocument.setContent(
                "Java is an object-oriented programming language."
        );
        savedDocument.setCategory("Java");

        when(repository.save(any(KnowledgeDocument.class)))
                .thenReturn(savedDocument);

        // Act
        KnowledgeDocumentResponse response =
                service.createDocument(request);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Java Basics", response.getTitle());
        assertEquals(
                "Java is an object-oriented programming language.",
                response.getContent()
        );
        assertEquals("Java", response.getCategory());

        verify(repository).save(any(KnowledgeDocument.class));
    }
    @Test
    void getDocumentById_shouldReturnDocument_whenDocumentExists() {

        // Arrange
        Long id = 1L;

        KnowledgeDocument document = new KnowledgeDocument();
        document.setId(id);
        document.setTitle("Java Basics");
        document.setContent("Java is an object-oriented programming language.");
        document.setCategory("Java");

        when(repository.findById(id))
                .thenReturn(Optional.of(document));

        // Act
        KnowledgeDocumentResponse response =
                service.getDocumentById(id);

        // Assert
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Java Basics", response.getTitle());
        assertEquals(
                "Java is an object-oriented programming language.",
                response.getContent()
        );
        assertEquals("Java", response.getCategory());

        verify(repository).findById(id);
    }
        @Test
        void getDocumentById_shouldThrowException_whenDocumentDoesNotExist() {

            // Arrange
            Long id = 99L;

            when(repository.findById(id))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                    DocumentNotFoundException.class,
                    () -> service.getDocumentById(id)
            );

            verify(repository).findById(id);
        }
    @Test
    void updateDocument_shouldUpdateDocument_whenDocumentExists() {

        // Arrange
        Long id = 1L;

        KnowledgeDocumentRequest request =
                new KnowledgeDocumentRequest();

        request.setTitle("Advanced Java");
        request.setContent("Advanced Java concepts");
        request.setCategory("Java");

        KnowledgeDocument existingDocument =
                new KnowledgeDocument();

        existingDocument.setId(id);
        existingDocument.setTitle("Java Basics");
        existingDocument.setContent(
                "Java is an object-oriented programming language."
        );
        existingDocument.setCategory("Java");

        when(repository.findById(id))
                .thenReturn(Optional.of(existingDocument));

        when(repository.save(any(KnowledgeDocument.class)))
                .thenReturn(existingDocument);

        // Act
        KnowledgeDocumentResponse response =
                service.updateDocument(id, request);

        // Assert
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("Advanced Java", response.getTitle());
        assertEquals("Advanced Java concepts", response.getContent());
        assertEquals("Java", response.getCategory());

        verify(repository).findById(id);
        verify(repository).save(existingDocument);
    }
    @Test
    void updateDocument_shouldThrowException_whenDocumentDoesNotExist() {

        // Arrange
        Long id = 999L;

        KnowledgeDocumentRequest request =
                new KnowledgeDocumentRequest();

        request.setTitle("Advanced Java");
        request.setContent("Advanced Java concepts");
        request.setCategory("Java");

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                DocumentNotFoundException.class,
                () -> service.updateDocument(id, request)
        );

        verify(repository).findById(id);
    }
    @Test
    void deleteDocument_shouldDeleteDocument_whenDocumentExists() {

        // Arrange
        Long id = 1L;

        KnowledgeDocument document = new KnowledgeDocument();
        document.setId(id);
        document.setTitle("Java Basics");
        document.setContent("Java is an object-oriented programming language.");
        document.setCategory("Java");

        when(repository.findById(id))
                .thenReturn(Optional.of(document));

        // Act
        service.deleteDocument(id);

        // Assert
        verify(repository).findById(id);
        verify(repository).delete(document);
    }
    @Test
    void deleteDocument_shouldThrowException_whenDocumentDoesNotExist() {

        // Arrange
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                DocumentNotFoundException.class,
                () -> service.deleteDocument(id)
        );

        verify(repository).findById(id);
    }
    @Test
    void searchByTitle_shouldReturnMatchingDocuments() {

        // Arrange
        String keyword = "Java";

        Pageable pageable = PageRequest.of(0, 10);

        KnowledgeDocument document = new KnowledgeDocument();
        document.setId(1L);
        document.setTitle("Java Basics");
        document.setContent("Java is an object-oriented programming language.");
        document.setCategory("Java");

        Page<KnowledgeDocument> page =
                new PageImpl<>(List.of(document));

        when(repository.findByTitleContainingIgnoreCase(keyword, pageable))
                .thenReturn(page);

        // Act
        Page<KnowledgeDocumentResponse> response =
                service.searchByTitle(keyword, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Java Basics",
                response.getContent().get(0).getTitle());

        verify(repository)
                .findByTitleContainingIgnoreCase(keyword, pageable);
    }
    @Test
    void filterByCategory_shouldReturnMatchingDocuments() {

        // Arrange
        String category = "Java";

        Pageable pageable = PageRequest.of(0, 10);

        KnowledgeDocument document = new KnowledgeDocument();
        document.setId(1L);
        document.setTitle("Java Basics");
        document.setContent(
                "Java is an object-oriented programming language."
        );
        document.setCategory("Java");

        Page<KnowledgeDocument> page =
                new PageImpl<>(List.of(document));

        when(repository.findByCategoryIgnoreCase(category, pageable))
                .thenReturn(page);

        // Act
        Page<KnowledgeDocumentResponse> response =
                service.filterByCategory(category, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(
                "Java",
                response.getContent().get(0).getCategory()
        );

        verify(repository)
                .findByCategoryIgnoreCase(category, pageable);
    }
    @Test
    void filterByKeywordAndCategory_shouldReturnMatchingDocuments() {

        // Arrange
        String keyword = "Java";
        String category = "Java";

        Pageable pageable = PageRequest.of(0, 10);

        KnowledgeDocument document = new KnowledgeDocument();
        document.setId(1L);
        document.setTitle("Java Basics");
        document.setContent(
                "Java is an object-oriented programming language."
        );
        document.setCategory("Java");

        Page<KnowledgeDocument> page =
                new PageImpl<>(List.of(document));

        when(repository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(
                keyword,
                category,
                pageable
        )).thenReturn(page);

        // Act
        Page<KnowledgeDocumentResponse> response =
                service.filterByKeywordAndCategory(
                        keyword,
                        category,
                        pageable
                );

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        KnowledgeDocumentResponse result =
                response.getContent().get(0);

        assertEquals(1L, result.getId());
        assertEquals("Java Basics", result.getTitle());
        assertEquals(
                "Java is an object-oriented programming language.",
                result.getContent()
        );
        assertEquals("Java", result.getCategory());

        verify(repository)
                .findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(
                        keyword,
                        category,
                        pageable
                );
    }
    @Test
    void getAllDocuments_shouldReturnDocuments() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        KnowledgeDocument document1 =
                new KnowledgeDocument();

        document1.setId(1L);
        document1.setTitle("Java Basics");
        document1.setContent(
                "Java is an object-oriented programming language."
        );
        document1.setCategory("Java");

        KnowledgeDocument document2 =
                new KnowledgeDocument();

        document2.setId(2L);
        document2.setTitle("Spring Boot");
        document2.setContent(
                "Spring Boot is used to build Java applications."
        );
        document2.setCategory("Spring");

        Page<KnowledgeDocument> page =
                new PageImpl<>(List.of(document1, document2));

        when(repository.findAll(pageable))
                .thenReturn(page);

        // Act
        Page<KnowledgeDocumentResponse> response =
                service.getAllDocuments(pageable);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getTotalElements());

        assertEquals(
                "Java Basics",
                response.getContent().get(0).getTitle()
        );

        assertEquals(
                "Spring Boot",
                response.getContent().get(1).getTitle()
        );

        assertEquals(
                "Java",
                response.getContent().get(0).getCategory()
        );

        assertEquals(
                "Spring",
                response.getContent().get(1).getCategory()
        );

        // Verify
        verify(repository).findAll(pageable);
    }
    }

