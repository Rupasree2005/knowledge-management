package com.example.knowledge_management.repo;

import com.example.knowledge_management.entity.KnowledgeDocument;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface KnowledgeDocumentRepository
        extends JpaRepository<KnowledgeDocument, Long> {
    Page<KnowledgeDocument> findByTitleContainingIgnoreCase(
            String keyword,
            Pageable pageable);
    Page<KnowledgeDocument> findByCategoryIgnoreCase(
            String category,
            Pageable pageable);
    Page<KnowledgeDocument> findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(
            String keyword,
            String category,
            Pageable pageable);
}
