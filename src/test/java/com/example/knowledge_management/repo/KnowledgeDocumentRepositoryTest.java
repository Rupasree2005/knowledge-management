package com.example.knowledge_management.repo;

import com.example.knowledge_management.entity.KnowledgeDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class KnowledgeDocumentRepositoryTest {

    @Autowired
    private KnowledgeDocumentRepository repository;

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnMatchingDocuments() {

        // Arrange
        KnowledgeDocument document1 =
                new KnowledgeDocument();

        document1.setTitle("Java Basics");
        document1.setContent("Java programming");
        document1.setCategory("Java");

        KnowledgeDocument document2 =
                new KnowledgeDocument();

        document2.setTitle("Spring Boot");
        document2.setContent("Spring Boot programming");
        document2.setCategory("Spring");

        repository.save(document1);
        repository.save(document2);

        // Act
        Page<KnowledgeDocument> result =
                repository.findByTitleContainingIgnoreCase(
                        "java",
                        PageRequest.of(0, 10)
                );

        // Assert
        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Java Basics",
                result.getContent().get(0).getTitle()
        );
    }
    @Test
    void findByCategoryIgnoreCase_shouldReturnMatchingDocuments() {

        KnowledgeDocument doc1 = new KnowledgeDocument();
        doc1.setTitle("Java Basics");
        doc1.setContent("Learning Java");
        doc1.setCategory("Java");

        KnowledgeDocument doc2 = new KnowledgeDocument();
        doc2.setTitle("Spring Boot");
        doc2.setContent("Learning Spring Boot");
        doc2.setCategory("Spring");

        repository.save(doc1);
        repository.save(doc2);

        Page<KnowledgeDocument> result =
                repository.findByCategoryIgnoreCase(
                        "java",
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());
        assertEquals("Java Basics", result.getContent().get(0).getTitle());
    }
    @Test
    void findByTitleContainingIgnoreCaseAndCategoryIgnoreCase_shouldReturnMatchingDocuments() {

        KnowledgeDocument doc1 = new KnowledgeDocument();
        doc1.setTitle("Java Basics");
        doc1.setContent("Learning Java");
        doc1.setCategory("Programming");

        KnowledgeDocument doc2 = new KnowledgeDocument();
        doc2.setTitle("Java Spring Boot");
        doc2.setContent("Learning Spring Boot");
        doc2.setCategory("Programming");

        KnowledgeDocument doc3 = new KnowledgeDocument();
        doc3.setTitle("JavaScript Basics");
        doc3.setContent("Learning JavaScript");
        doc3.setCategory("Web");

        repository.save(doc1);
        repository.save(doc2);
        repository.save(doc3);

        Page<KnowledgeDocument> result =
                repository.findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(
                        "java",
                        "programming",
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());
    }
    @Test
    void findByTitleContainingIgnoreCase_shouldSupportPagination() {

        KnowledgeDocument doc1 = new KnowledgeDocument();
        doc1.setTitle("Java Basics");
        doc1.setContent("Java");
        doc1.setCategory("Programming");

        KnowledgeDocument doc2 = new KnowledgeDocument();
        doc2.setTitle("Java Advanced");
        doc2.setContent("Advanced Java");
        doc2.setCategory("Programming");

        KnowledgeDocument doc3 = new KnowledgeDocument();
        doc3.setTitle("Java Spring Boot");
        doc3.setContent("Spring Boot with Java");
        doc3.setCategory("Programming");

        repository.save(doc1);
        repository.save(doc2);
        repository.save(doc3);

        Page<KnowledgeDocument> result =
                repository.findByTitleContainingIgnoreCase(
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
    void findByTitleContainingIgnoreCase_shouldSupportSorting() {

        KnowledgeDocument doc1 = new KnowledgeDocument();
        doc1.setTitle("Java Basics");
        doc1.setContent("Basic Java");
        doc1.setCategory("Programming");

        KnowledgeDocument doc2 = new KnowledgeDocument();
        doc2.setTitle("Java Advanced");
        doc2.setContent("Advanced Java");
        doc2.setCategory("Programming");

        KnowledgeDocument doc3 = new KnowledgeDocument();
        doc3.setTitle("Java Spring Boot");
        doc3.setContent("Spring Boot with Java");
        doc3.setCategory("Programming");

        repository.save(doc1);
        repository.save(doc2);
        repository.save(doc3);

        Page<KnowledgeDocument> result =
                repository.findByTitleContainingIgnoreCase(
                        "java",
                        PageRequest.of(
                                0,
                                10,
                                Sort.by(Sort.Direction.DESC, "title")
                        )
                );

        assertEquals(3, result.getTotalElements());

        assertEquals("Java Spring Boot", result.getContent().get(0).getTitle());
        assertEquals("Java Basics", result.getContent().get(1).getTitle());
        assertEquals("Java Advanced", result.getContent().get(2).getTitle());
    }
}