package com.mdeditor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Editor Page Controller
 *
 * Handles Thymeleaf page rendering
 */
@Controller
@RequiredArgsConstructor
public class EditorController {

    /**
     * Editor homepage
     */
    @GetMapping("/")
    public String index() {
        return "pages/editor";
    }

    /**
     * Editor page
     */
    @GetMapping("/editor")
    public String editor() {
        return "pages/editor";
    }

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    /**
     * Register page
     */
    @GetMapping("/register")
    public String register() {
        return "pages/register";
    }

    /**
     * Documents list page
     */
    @GetMapping("/documents")
    public String documents() {
        return "pages/documents";
    }
}