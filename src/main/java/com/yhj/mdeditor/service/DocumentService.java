package com.yhj.mdeditor.service;

import com.yhj.mdeditor.dto.DocumentRequest;
import com.yhj.mdeditor.dto.DocumentResponse;
import com.yhj.mdeditor.entity.Document;
import com.yhj.mdeditor.entity.DocumentVersion;
import com.yhj.mdeditor.repository.DocumentRepository;
import com.yhj.mdeditor.repository.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Document Service
 *
 * Handles document CRUD operations and version management
 */
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;

    /**
     * Create a new document
     */
    @Transactional
    public DocumentResponse createDocument(Long userId, DocumentRequest request) {
        Document document = Document.builder()
            .userId(userId)
            .title(request.getTitle())
            .content(request.getContent())
            .version(1)
            .build();

        document = documentRepository.save(document);

        // Create initial version history
        createVersionHistory(document);

        return convertToResponse(document);
    }

    /**
     * Get all documents for a user
     */
    public List<DocumentResponse> getUserDocuments(Long userId) {
        return documentRepository.findByUserIdOrderByUpdatedAtDesc(userId)
            .stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get a specific document
     */
    public Optional<DocumentResponse> getDocument(Long userId, Long documentId) {
        return documentRepository.findByIdAndUserId(documentId, userId)
            .map(this::convertToResponse);
    }

    /**
     * Update a document
     */
    @Transactional
    public DocumentResponse updateDocument(Long userId, Long documentId, DocumentRequest request) {
        Optional<Document> existing = documentRepository.findByIdAndUserId(documentId, userId);

        if (existing.isEmpty()) {
            throw new RuntimeException("Document not found");
        }

        Document document = existing.get();
        document.setTitle(request.getTitle());
        document.setContent(request.getContent());
        document.setVersion(document.getVersion() + 1);

        document = documentRepository.save(document);

        // Create new version history
        createVersionHistory(document);

        return convertToResponse(document);
    }

    /**
     * Delete a document
     */
    @Transactional
    public void deleteDocument(Long userId, Long documentId) {
        documentRepository.deleteByIdAndUserId(documentId, userId);
    }

    /**
     * Get document version history
     */
    public List<DocumentVersion> getVersionHistory(Long documentId) {
        return documentVersionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId);
    }

    /**
     * Get specific version of a document
     */
    public Optional<DocumentVersion> getSpecificVersion(Long documentId, Integer versionNumber) {
        return documentVersionRepository.findByDocumentIdAndVersionNumber(documentId, versionNumber);
    }

    /**
     * Create version history entry
     */
    private void createVersionHistory(Document document) {
        DocumentVersion version = DocumentVersion.builder()
            .documentId(document.getId())
            .content(document.getContent())
            .versionNumber(document.getVersion())
            .build();

        documentVersionRepository.save(version);
    }

    /**
     * Convert Document entity to response DTO
     */
    private DocumentResponse convertToResponse(Document document) {
        return DocumentResponse.builder()
            .id(document.getId())
            .userId(document.getUserId())
            .title(document.getTitle())
            .content(document.getContent())
            .createdAt(document.getCreatedAt())
            .updatedAt(document.getUpdatedAt())
            .version(document.getVersion())
            .build();
    }
}