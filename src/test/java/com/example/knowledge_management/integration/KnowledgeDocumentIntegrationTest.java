package com.example.knowledge_management.integration;

import com.example.knowledge_management.dto.KnowledgeDocumentRequest;
import com.example.knowledge_management.dto.KnowledgeDocumentResponse;
import com.example.knowledge_management.repo.KnowledgeDocumentRepository;
import com.example.knowledge_management.service.KnowledgeDocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional

class KnowledgeDocumentIntegrationTest {

    @Autowired
    private KnowledgeDocumentService service;

    @Autowired
    private KnowledgeDocumentRepository repository;

    @Test
    void createDocument_shouldSaveDocumentToDatabase() {

        KnowledgeDocumentRequest request = new KnowledgeDocumentRequest();

        request.setTitle("Integration Test Document");
        request.setContent("Testing service and repository together");
        request.setCategory("Testing");

        KnowledgeDocumentResponse response =
                service.createDocument(request);

        assertNotNull(response.getId());
        assertEquals("Integration Test Document", response.getTitle());
        assertEquals("Testing", response.getCategory());

        var savedDocument =
                repository.findById(response.getId());

        assertEquals(true, savedDocument.isPresent());
        assertEquals(
                "Integration Test Document",
                savedDocument.get().getTitle()
        );
    }
    @Test
    void getDocumentById_shouldReturnSavedDocument() {

        KnowledgeDocumentRequest request = new KnowledgeDocumentRequest();

        request.setTitle("Get Integration Test");
        request.setContent("Testing document retrieval");
        request.setCategory("Testing");

        KnowledgeDocumentResponse created =
                service.createDocument(request);

        KnowledgeDocumentResponse result =
                service.getDocumentById(created.getId());

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals("Get Integration Test", result.getTitle());
        assertEquals("Testing", result.getCategory());
    }
    @Test
    void updateDocument_shouldUpdateSavedDocument() {

        KnowledgeDocumentRequest createRequest =
                new KnowledgeDocumentRequest();

        createRequest.setTitle("Original Title");
        createRequest.setContent("Original Content");
        createRequest.setCategory("Testing");

        KnowledgeDocumentResponse created =
                service.createDocument(createRequest);

        KnowledgeDocumentRequest updateRequest =
                new KnowledgeDocumentRequest();

        updateRequest.setTitle("Updated Title");
        updateRequest.setContent("Updated Content");
        updateRequest.setCategory("Updated Category");

        KnowledgeDocumentResponse updated =
                service.updateDocument(
                        created.getId(),
                        updateRequest
                );

        assertEquals(created.getId(), updated.getId());
        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated Content", updated.getContent());
        assertEquals("Updated Category", updated.getCategory());

        var savedDocument =
                repository.findById(created.getId());

        assertTrue(savedDocument.isPresent());
        assertEquals(
                "Updated Title",
                savedDocument.get().getTitle()
        );
    }
    @Test
    void deleteDocument_shouldRemoveSavedDocument() {

        KnowledgeDocumentRequest request =
                new KnowledgeDocumentRequest();

        request.setTitle("Delete Integration Test");
        request.setContent("Testing document deletion");
        request.setCategory("Testing");

        KnowledgeDocumentResponse created =
                service.createDocument(request);

        Long documentId = created.getId();

        // Verify it exists before deletion
        assertTrue(repository.findById(documentId).isPresent());

        // Delete through the real service
        service.deleteDocument(documentId);

        // Verify it no longer exists
        assertTrue(repository.findById(documentId).isEmpty());
    }
    @Test
    void searchByTitle_shouldReturnMatchingDocuments() {

        KnowledgeDocumentRequest request1 =
                new KnowledgeDocumentRequest();

        request1.setTitle("Java Basics");
        request1.setContent("Learn Java fundamentals");
        request1.setCategory("Programming");

        KnowledgeDocumentRequest request2 =
                new KnowledgeDocumentRequest();

        request2.setTitle("Spring Boot Guide");
        request2.setContent("Learn Spring Boot");
        request2.setCategory("Programming");

        service.createDocument(request1);
        service.createDocument(request2);

        Page<KnowledgeDocumentResponse> result =
                service.searchByTitle(
                        "java",
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());
        assertEquals("Java Basics", result.getContent().get(0).getTitle());
    }
    @Test
    void filterByCategory_shouldReturnMatchingDocuments() {

        KnowledgeDocumentRequest request1 =
                new KnowledgeDocumentRequest();

        request1.setTitle("Java Basics");
        request1.setContent("Learn Java");
        request1.setCategory("Programming");

        KnowledgeDocumentRequest request2 =
                new KnowledgeDocumentRequest();

        request2.setTitle("Spring Guide");
        request2.setContent("Learn Spring");
        request2.setCategory("Framework");

        service.createDocument(request1);
        service.createDocument(request2);

        Page<KnowledgeDocumentResponse> result =
                service.filterByCategory(
                        "programming",
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());
        assertEquals("Java Basics", result.getContent().get(0).getTitle());
    }
    @Test
    void filterByKeywordAndCategory_shouldReturnMatchingDocuments() {

        KnowledgeDocumentRequest request1 =
                new KnowledgeDocumentRequest();

        request1.setTitle("Java Basics");
        request1.setContent("Learn Java fundamentals");
        request1.setCategory("Programming");

        KnowledgeDocumentRequest request2 =
                new KnowledgeDocumentRequest();

        request2.setTitle("Java Spring Boot");
        request2.setContent("Learn Spring Boot");
        request2.setCategory("Programming");

        KnowledgeDocumentRequest request3 =
                new KnowledgeDocumentRequest();

        request3.setTitle("JavaScript Basics");
        request3.setContent("Learn JavaScript");
        request3.setCategory("Web");

        service.createDocument(request1);
        service.createDocument(request2);
        service.createDocument(request3);

        Page<KnowledgeDocumentResponse> result =
                service.filterByKeywordAndCategory(
                        "java",
                        "programming",
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());
        assertEquals("Java Basics", result.getContent().get(0).getTitle());
        assertEquals("Java Spring Boot", result.getContent().get(1).getTitle());
    }
    @Test
    void searchByTitle_shouldSupportPagination() {

        KnowledgeDocumentRequest request1 =
                new KnowledgeDocumentRequest();
        request1.setTitle("Java Basics");
        request1.setContent("Basic Java");
        request1.setCategory("Programming");

        KnowledgeDocumentRequest request2 =
                new KnowledgeDocumentRequest();
        request2.setTitle("Java Advanced");
        request2.setContent("Advanced Java");
        request2.setCategory("Programming");

        KnowledgeDocumentRequest request3 =
                new KnowledgeDocumentRequest();
        request3.setTitle("Java Spring Boot");
        request3.setContent("Spring Boot with Java");
        request3.setCategory("Programming");

        service.createDocument(request1);
        service.createDocument(request2);
        service.createDocument(request3);

        Page<KnowledgeDocumentResponse> result =
                service.searchByTitle(
                        "java",
                        PageRequest.of(0, 2)
                );

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());
    }
    @Test
    void searchByTitle_shouldSupportSorting() {

        KnowledgeDocumentRequest request1 =
                new KnowledgeDocumentRequest();
        request1.setTitle("Java Basics");
        request1.setContent("Basic Java");
        request1.setCategory("Programming");

        KnowledgeDocumentRequest request2 =
                new KnowledgeDocumentRequest();
        request2.setTitle("Java Advanced");
        request2.setContent("Advanced Java");
        request2.setCategory("Programming");

        KnowledgeDocumentRequest request3 =
                new KnowledgeDocumentRequest();
        request3.setTitle("Java Spring Boot");
        request3.setContent("Spring Boot with Java");
        request3.setCategory("Programming");

        service.createDocument(request1);
        service.createDocument(request2);
        service.createDocument(request3);

        Page<KnowledgeDocumentResponse> result =
                service.searchByTitle(
                        "java",
                        PageRequest.of(
                                0,
                                10,
                                Sort.by(Sort.Direction.DESC, "title")
                        )
                );

        assertEquals(3, result.getTotalElements());

        assertEquals(
                "Java Spring Boot",
                result.getContent().get(0).getTitle()
        );

        assertEquals(
                "Java Basics",
                result.getContent().get(1).getTitle()
        );

        assertEquals(
                "Java Advanced",
                result.getContent().get(2).getTitle()
        );
    }
}
