package com.mdeditor.service;

import com.mdeditor.entity.UserSettings;
import com.mdeditor.repository.UserSettingsRepository;
import com.mdeditor.dto.UserSettingsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Theme Service
 *
 * Manages user theme preferences and CSS variable generation
 */
@Service
@RequiredArgsConstructor
public class ThemeService {

    private final UserSettingsRepository userSettingsRepository;

    /**
     * Get user settings by user ID
     */
    public UserSettingsDto getUserSettings(Long userId) {
        Optional<UserSettings> settings = userSettingsRepository.findByUserId(userId);
        if (settings.isPresent()) {
            return convertToDto(settings.get());
        }
        return getDefaultSettings();
    }

    /**
     * Update user settings
     */
    @Transactional
    public UserSettingsDto updateUserSettings(Long userId, UserSettingsDto dto) {
        Optional<UserSettings> existing = userSettingsRepository.findByUserId(userId);
        UserSettings settings;

        if (existing.isPresent()) {
            settings = existing.get();
            updateFromDto(settings, dto);
        } else {
            settings = UserSettings.builder()
                .userId(userId)
                .colorTheme(dto.getColorTheme() != null ? dto.getColorTheme() : "meihei")
                .codeStyle(dto.getCodeStyle() != null ? dto.getCodeStyle() : "mac")
                .themeSystem(dto.getThemeSystem() != null ? dto.getThemeSystem() : "default")
                .fontFamily(dto.getFontFamily() != null ? dto.getFontFamily() : "system-default")
                .fontSize(dto.getFontSize() != null ? dto.getFontSize() : 16)
                .lineHeight(dto.getLineHeight() != null ? dto.getLineHeight() : BigDecimal.valueOf(1.6))
                .letterSpacing(dto.getLetterSpacing() != null ? dto.getLetterSpacing() : BigDecimal.ZERO)
                .build();
        }

        settings = userSettingsRepository.save(settings);
        return convertToDto(settings);
    }

    /**
     * Get CSS variables for a theme
     */
    public String generateCssVariables(UserSettingsDto settings) {
        String colorTheme = settings.getColorTheme() != null ? settings.getColorTheme() : "meihei";
        String primaryColor = getPrimaryColor(colorTheme);
        String primaryLight = getPrimaryLight(colorTheme);
        String primaryDark = getPrimaryDark(colorTheme);

        StringBuilder css = new StringBuilder();
        css.append(":root {\n");
        css.append("  --theme-primary: ").append(primaryColor).append(";\n");
        css.append("  --theme-primary-light: ").append(primaryLight).append(";\n");
        css.append("  --theme-primary-dark: ").append(primaryDark).append(";\n");
        css.append("  --theme-text-primary: #1f2328;\n");
        css.append("  --theme-text-secondary: #656d76;\n");
        css.append("  --theme-bg-primary: #ffffff;\n");
        css.append("  --theme-bg-secondary: #f6f8fa;\n");
        css.append("  --theme-border-light: #d0d7de;\n");
        css.append("  --theme-inline-code-bg: rgba(251, 146, 60, 0.08);\n");
        css.append("  --theme-inline-code-text: #ea580c;\n");
        css.append("  --theme-blockquote-border: ").append(primaryColor).append(";\n");
        css.append("  --theme-blockquote-bg: ").append(primaryLight).append(";\n");

        // Font settings
        css.append("  --font-size: ").append(settings.getFontSize() != null ? settings.getFontSize() : 16).append("px;\n");
        css.append("  --line-height: ").append(settings.getLineHeight() != null ? settings.getLineHeight() : BigDecimal.valueOf(1.6)).append(";\n");
        css.append("  --letter-spacing: ").append(settings.getLetterSpacing() != null ? settings.getLetterSpacing() : BigDecimal.ZERO).append("px;\n");

        css.append("}\n");
        return css.toString();
    }

    /**
     * Get available color themes
     */
    public Object[] getAvailableThemes() {
        return new Object[]{
            new ThemeInfo("meihei", "煤黑", "#333333", false),
            new ThemeInfo("github", "GitHub", "#0366d6", false),
            new ThemeInfo("wechat", "微信绿", "#07c160", false),
            new ThemeInfo("vuepress", "VuePress", "#3eaf7c", false),
            new ThemeInfo("cobalt", "钴蓝", "#0888ff", false),
            new ThemeInfo("marry", "喜庆红", "#fa5151", false),
            new ThemeInfo("purple", "紫罗兰", "#7b1fa2", false),
            new ThemeInfo("ocean", "海洋蓝", "#00695c", false),
        };
    }

    /**
     * Get available code styles
     */
    public Object[] getAvailableCodeStyles() {
        return new Object[]{
            new CodeStyleInfo("mac", "Mac风格", "mac-terminal"),
            new CodeStyleInfo("github", "GitHub", "github-style"),
            new CodeStyleInfo("atom", "Atom", "atom-dark"),
            new CodeStyleInfo("vscode", "VS Code", "vscode-dark"),
        };
    }

    /**
     * Get available theme systems
     */
    public Object[] getAvailableThemeSystems() {
        return new Object[]{
            new ThemeSystemInfo("default", "默认", "Standard layout"),
            new ThemeSystemInfo("breeze", "清风", "Clean and minimal"),
        };
    }

    private String getPrimaryColor(String theme) {
        return switch (theme) {
            case "meihei" -> "#333333";
            case "github" -> "#0366d6";
            case "wechat" -> "#07c160";
            case "vuepress" -> "#3eaf7c";
            case "cobalt" -> "#0888ff";
            case "marry" -> "#fa5151";
            case "purple" -> "#7b1fa2";
            case "ocean" -> "#00695c";
            default -> "#333333";
        };
    }

    private String getPrimaryLight(String theme) {
        return switch (theme) {
            case "meihei" -> "#666666";
            case "github" -> "#c8e1ff";
            case "wechat" -> "#c3f7c4";
            case "vuepress" -> "#c8f0d4";
            case "cobalt" -> "#b8d4ff";
            case "marry" -> "#ffc1c1";
            case "purple" -> "#e1bee7";
            case "ocean" -> "#b2dfdb";
            default -> "#666666";
        };
    }

    private String getPrimaryDark(String theme) {
        return switch (theme) {
            case "meihei" -> "#1a1a1a";
            case "github" -> "#0056b3";
            case "wechat" -> "#05a050";
            case "vuepress" -> "#2d8f60";
            case "cobalt" -> "#0666cc";
            case "marry" -> "#d32f2f";
            case "purple" -> "#4a148c";
            case "ocean" -> "#004d40";
            default -> "#1a1a1a";
        };
    }

    private UserSettingsDto getDefaultSettings() {
        return UserSettingsDto.builder()
            .colorTheme("meihei")
            .codeStyle("mac")
            .themeSystem("default")
            .fontFamily("system-default")
            .fontSize(16)
            .lineHeight(BigDecimal.valueOf(1.6))
            .letterSpacing(BigDecimal.ZERO)
            .build();
    }

    private UserSettingsDto convertToDto(UserSettings settings) {
        return UserSettingsDto.builder()
            .colorTheme(settings.getColorTheme())
            .codeStyle(settings.getCodeStyle())
            .themeSystem(settings.getThemeSystem())
            .fontFamily(settings.getFontFamily())
            .fontSize(settings.getFontSize())
            .lineHeight(settings.getLineHeight())
            .letterSpacing(settings.getLetterSpacing())
            .build();
    }

    private void updateFromDto(UserSettings settings, UserSettingsDto dto) {
        if (dto.getColorTheme() != null) settings.setColorTheme(dto.getColorTheme());
        if (dto.getCodeStyle() != null) settings.setCodeStyle(dto.getCodeStyle());
        if (dto.getThemeSystem() != null) settings.setThemeSystem(dto.getThemeSystem());
        if (dto.getFontFamily() != null) settings.setFontFamily(dto.getFontFamily());
        if (dto.getFontSize() != null) settings.setFontSize(dto.getFontSize());
        if (dto.getLineHeight() != null) settings.setLineHeight(dto.getLineHeight());
        if (dto.getLetterSpacing() != null) settings.setLetterSpacing(dto.getLetterSpacing());
    }

    // Inner classes for theme info
    public record ThemeInfo(String id, String name, String primaryColor, boolean isDark) {}
    public record CodeStyleInfo(String id, String name, String styleClass) {}
    public record ThemeSystemInfo(String id, String name, String description) {}
}