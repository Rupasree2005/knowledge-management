package com.example.knowledge_management.service;

import com.example.knowledge_management.dto.KnowledgeDocumentRequest;
import com.example.knowledge_management.dto.KnowledgeDocumentResponse;
import com.example.knowledge_management.entity.KnowledgeDocument;
import com.example.knowledge_management.exception.DocumentNotFoundException;
import com.example.knowledge_management.repo.KnowledgeDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class KnowledgeDocumentService {

    private final KnowledgeDocumentRepository repository;
    private static final Logger logger =
            LoggerFactory.getLogger(KnowledgeDocumentService.class);

    public KnowledgeDocumentService(KnowledgeDocumentRepository repository) {
        this.repository = repository;
    }

    public KnowledgeDocumentResponse createDocument(
            KnowledgeDocumentRequest request) {
        logger.info("Creating document with title: {}",
                request.getTitle());
        KnowledgeDocument document = new KnowledgeDocument();

        document.setTitle(request.getTitle());
        document.setContent(request.getContent());
        document.setCategory(request.getCategory());

        KnowledgeDocument savedDocument = repository.save(document);
        logger.info("Document created successfully with id: {}",
                savedDocument.getId());
        return convertToResponse(savedDocument);
    }

    public Page<KnowledgeDocumentResponse> getAllDocuments(Pageable pageable) {

        return repository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public KnowledgeDocumentResponse getDocumentById(Long id) {

        KnowledgeDocument document = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Document not found with id: {}", id);

                    return new DocumentNotFoundException(
                            "Document not found with id: " + id
                    );
                });

        return convertToResponse(document);
    }
    public KnowledgeDocumentResponse updateDocument(
            Long id,
            KnowledgeDocumentRequest request) {
        logger.info("Updating document with id: {}", id);
        KnowledgeDocument existingDocument = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update document. Document not found with id: {}", id);

                    return new DocumentNotFoundException(
                            "Document not found with id: " + id
                    );
                });

        existingDocument.setTitle(request.getTitle());
        existingDocument.setContent(request.getContent());
        existingDocument.setCategory(request.getCategory());

        KnowledgeDocument updatedDocument =
                repository.save(existingDocument);
        logger.info("Document updated successfully with id: {}", id);

        return convertToResponse(updatedDocument);
    }

    public void deleteDocument(Long id) {
        logger.info("Deleting document with id: {}", id);

        KnowledgeDocument document = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot delete document. Document not found with id: {}", id);

                    return new DocumentNotFoundException(
                            "Document not found with id: " + id
                    );
                });

        repository.delete(document);
        logger.info("Document deleted successfully with id: {}", id);
    }

    private KnowledgeDocumentResponse convertToResponse(
            KnowledgeDocument document) {

        KnowledgeDocumentResponse response =
                new KnowledgeDocumentResponse();

        response.setId(document.getId());
        response.setTitle(document.getTitle());
        response.setContent(document.getContent());
        response.setCategory(document.getCategory());
        response.setCreatedAt(document.getCreatedAt());
        response.setUpdatedAt(document.getUpdatedAt());

        return response;
    }
    public Page<KnowledgeDocumentResponse> searchByTitle(
            String keyword,
            Pageable pageable) {
        logger.info("Searching documents by title with keyword: {}", keyword);

        return repository.findByTitleContainingIgnoreCase(keyword, pageable)
                .map(this::convertToResponse);
    }
    public Page<KnowledgeDocumentResponse> filterByCategory(
            String category,
            Pageable pageable) {

        return repository.findByCategoryIgnoreCase(category, pageable)
                .map(this::convertToResponse);
    }
    public Page<KnowledgeDocumentResponse> filterByKeywordAndCategory(
            String keyword,
            String category,
            Pageable pageable) {

        return repository
                .findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(
                        keyword,
                        category,
                        pageable)
                .map(this::convertToResponse);
    }
}