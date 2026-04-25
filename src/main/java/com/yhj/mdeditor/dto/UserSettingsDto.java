package com.yhj.mdeditor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * User Settings Request/Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettingsDto {

    private String colorTheme;
    private String codeStyle;
    private String themeSystem;
    private String fontFamily;
    private Integer fontSize;
    private BigDecimal lineHeight;
    private BigDecimal letterSpacing;
}