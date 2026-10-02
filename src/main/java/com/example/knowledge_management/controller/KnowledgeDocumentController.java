package com.example.knowledge_management.controller;

import com.example.knowledge_management.dto.KnowledgeDocumentRequest;
import com.example.knowledge_management.dto.KnowledgeDocumentResponse;
import com.example.knowledge_management.service.KnowledgeDocumentService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@Tag(
        name = "Knowledge Documents",
        description = "APIs for managing knowledge documents"
)
public class KnowledgeDocumentController {

    private final KnowledgeDocumentService service;

    public KnowledgeDocumentController(KnowledgeDocumentService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create a knowledge document",
            description = "Creates a new knowledge document"
    )
    @PostMapping
    public ResponseEntity<KnowledgeDocumentResponse> createDocument(
            @Valid @RequestBody KnowledgeDocumentRequest request) {

        KnowledgeDocumentResponse response =
                service.createDocument(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all documents",
            description = "Returns paginated knowledge documents"
    )
    @GetMapping
    public Page<KnowledgeDocumentResponse> getAllDocuments(

            @Parameter(
                    description = "Page number (starts from 0)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of documents per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return service.getAllDocuments(pageable);
    }

    @Operation(
            summary = "Search documents by title",
            description = "Searches documents whose title contains the given keyword"
    )
    @GetMapping("/search")
    public Page<KnowledgeDocumentResponse> searchDocuments(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return service.searchByTitle(keyword, pageable);
    }

    @Operation(
            summary = "Filter documents by category",
            description = "Returns documents belonging to the specified category"
    )
    @GetMapping("/category/{category}")
    public Page<KnowledgeDocumentResponse> getDocumentsByCategory(

            @PathVariable String category,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return service.filterByCategory(category, pageable);
    }

    @Operation(
            summary = "Filter documents by keyword and category",
            description = "Searches by title and category with pagination"
    )
    @GetMapping("/filter")
    public Page<KnowledgeDocumentResponse> filterDocuments(

            @RequestParam String keyword,

            @RequestParam String category,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return service.filterByKeywordAndCategory(
                keyword,
                category,
                pageable
        );
    }

    @Operation(
            summary = "Get document by ID",
            description = "Returns a single knowledge document by its ID"
    )
    @GetMapping("/{id:\\d+}")
    public KnowledgeDocumentResponse getDocumentById(
            @PathVariable Long id) {

        return service.getDocumentById(id);
    }

    @Operation(
            summary = "Update a document",
            description = "Updates an existing knowledge document"
    )
    @PutMapping("/{id:\\d+}")
    public KnowledgeDocumentResponse updateDocument(

            @PathVariable Long id,

            @Valid @RequestBody KnowledgeDocumentRequest request) {

        return service.updateDocument(id, request);
    }

    @Operation(
            summary = "Delete a document",
            description = "Deletes an existing knowledge document by ID"
    )
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id) {

        service.deleteDocument(id);

        return ResponseEntity.noContent().build();
    }
}