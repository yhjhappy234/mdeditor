package com.yhj.mdeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Markdown Parse Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkdownParseResponse {

    private String html;
    private String socialHtml;
    private int characterCount;
    private int lineCount;
    private int wordCount;
    private String estimatedReadTime;
}