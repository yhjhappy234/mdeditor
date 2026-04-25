package com.mdeditor.controller;

import com.mdeditor.dto.DocumentRequest;
import com.mdeditor.dto.DocumentResponse;
import com.mdeditor.entity.DocumentVersion;
import com.mdeditor.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Document REST Controller
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * Get all documents for current user
     */
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getUserDocuments(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<DocumentResponse> documents = documentService.getUserDocuments(userId);
        return ResponseEntity.ok(documents);
    }

    /**
     * Create a new document
     */
    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
            Authentication authentication,
            @Valid @RequestBody DocumentRequest request) {
        Long userId = getUserId(authentication);
        DocumentResponse document = documentService.createDocument(userId, request);
        return ResponseEntity.ok(document);
    }

    /**
     * Get a specific document
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = getUserId(authentication);
        return documentService.getDocument(userId, id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a document
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> updateDocument(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody DocumentRequest request) {
        Long userId = getUserId(authentication);
        DocumentResponse document = documentService.updateDocument(userId, id, request);
        return ResponseEntity.ok(document);
    }

    /**
     * Delete a document
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = getUserId(authentication);
        documentService.deleteDocument(userId, id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get document version history
     */
    @GetMapping("/{id}/versions")
    public ResponseEntity<List<DocumentVersion>> getVersionHistory(
            Authentication authentication,
            @PathVariable Long id) {
        List<DocumentVersion> versions = documentService.getVersionHistory(id);
        return ResponseEntity.ok(versions);
    }

    /**
     * Get specific version of a document
     */
    @GetMapping("/{id}/versions/{version}")
    public ResponseEntity<DocumentVersion> getSpecificVersion(
            Authentication authentication,
            @PathVariable Long id,
            @PathVariable Integer version) {
        return documentService.getSpecificVersion(id, version)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Extract user ID from authentication
     */
    private Long getUserId(Authentication authentication) {
        return (Long) authentication.getCredentials();
    }
}