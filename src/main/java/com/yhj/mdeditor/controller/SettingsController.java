package com.yhj.mdeditor.controller;

import com.yhj.mdeditor.dto.UserSettingsDto;
import com.yhj.mdeditor.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Settings REST Controller
 */
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final ThemeService themeService;

    /**
     * Get user settings
     */
    @GetMapping
    public ResponseEntity<UserSettingsDto> getUserSettings(Authentication authentication) {
        Long userId = getUserId(authentication);
        UserSettingsDto settings = themeService.getUserSettings(userId);
        return ResponseEntity.ok(settings);
    }

    /**
     * Update user settings
     */
    @PutMapping
    public ResponseEntity<UserSettingsDto> updateUserSettings(
            Authentication authentication,
            @RequestBody UserSettingsDto request) {
        Long userId = getUserId(authentication);
        UserSettingsDto settings = themeService.updateUserSettings(userId, request);
        return ResponseEntity.ok(settings);
    }

    /**
     * Get CSS variables for current theme
     */
    @GetMapping("/css-variables")
    public ResponseEntity<String> getCssVariables(Authentication authentication) {
        Long userId = getUserId(authentication);
        UserSettingsDto settings = themeService.getUserSettings(userId);
        String cssVariables = themeService.generateCssVariables(settings);
        return ResponseEntity.ok(cssVariables);
    }

    private Long getUserId(Authentication authentication) {
        return (Long) authentication.getCredentials();
    }
}