package com.mdeditor.controller;

import com.mdeditor.dto.UserSettingsDto;
import com.mdeditor.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Theme REST Controller
 */
@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    /**
     * Get available color themes
     */
    @GetMapping
    public ResponseEntity<Object[]> getAvailableThemes() {
        return ResponseEntity.ok(themeService.getAvailableThemes());
    }

    /**
     * Get available code styles
     */
    @GetMapping("/code-styles")
    public ResponseEntity<Object[]> getAvailableCodeStyles() {
        return ResponseEntity.ok(themeService.getAvailableCodeStyles());
    }

    /**
     * Get available theme systems
     */
    @GetMapping("/systems")
    public ResponseEntity<Object[]> getAvailableThemeSystems() {
        return ResponseEntity.ok(themeService.getAvailableThemeSystems());
    }

    /**
     * Get default settings
     */
    @GetMapping("/defaults")
    public ResponseEntity<UserSettingsDto> getDefaultSettings() {
        return ResponseEntity.ok(themeService.getUserSettings(null));
    }
}