package com.yhj.mdeditor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * User Settings Entity
 */
@Entity
@Table(name = "user_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "color_theme", length = 50)
    @Builder.Default
    private String colorTheme = "meihei";

    @Column(name = "code_style", length = 50)
    @Builder.Default
    private String codeStyle = "mac";

    @Column(name = "theme_system", length = 50)
    @Builder.Default
    private String themeSystem = "default";

    @Column(name = "font_family", length = 50)
    @Builder.Default
    private String fontFamily = "system-default";

    @Column(name = "font_size")
    @Builder.Default
    private Integer fontSize = 16;

    @Column(name = "line_height", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal lineHeight = BigDecimal.valueOf(1.6);

    @Column(name = "letter_spacing", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal letterSpacing = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}