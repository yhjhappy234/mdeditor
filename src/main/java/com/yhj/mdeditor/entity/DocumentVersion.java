package com.yhj.mdeditor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Document Version History Entity
 */
@Entity
@Table(name = "document_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "version_number")
    private Integer versionNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    private Document document;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}