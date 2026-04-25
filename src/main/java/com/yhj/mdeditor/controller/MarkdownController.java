package com.yhj.mdeditor.controller;

import com.yhj.mdeditor.dto.MarkdownParseRequest;
import com.yhj.mdeditor.dto.MarkdownParseResponse;
import com.yhj.mdeditor.service.MarkdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Markdown REST Controller
 */
@RestController
@RequestMapping("/api/markdown")
@RequiredArgsConstructor
public class MarkdownController {

    private final MarkdownService markdownService;

    /**
     * Parse Markdown to HTML
     */
    @PostMapping("/parse")
    public ResponseEntity<MarkdownParseResponse> parse(@RequestBody MarkdownParseRequest request) {
        MarkdownParseResponse response = markdownService.parse(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Preview Markdown (returns HTML for preview)
     */
    @PostMapping("/preview")
    public ResponseEntity<MarkdownParseResponse> preview(@RequestBody MarkdownParseRequest request) {
        MarkdownParseResponse response = markdownService.parse(request);
        return ResponseEntity.ok(response);
    }
}