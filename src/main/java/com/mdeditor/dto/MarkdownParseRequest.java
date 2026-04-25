package com.mdeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Markdown Parse Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkdownParseRequest {

    private String content;
    private String colorTheme;
    private String codeStyle;
    private String themeSystem;
    private boolean forSocial;
}