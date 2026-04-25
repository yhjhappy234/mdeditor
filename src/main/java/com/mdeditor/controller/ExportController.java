package com.mdeditor.controller;

import com.mdeditor.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Export REST Controller
 */
@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    /**
     * Export Markdown as PDF
     */
    @PostMapping("/pdf")
    public ResponseEntity<byte[]> exportPdf(@RequestBody ExportRequest request) {
        try {
            byte[] pdfContent = exportService.exportAsPdf(request.getContent(), request.getColorTheme());
            String filename = exportService.generateFilename("pdf");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Export Markdown as Image
     */
    @PostMapping("/image")
    public ResponseEntity<byte[]> exportImage(@RequestBody ExportRequest request) {
        try {
            byte[] imageContent = exportService.exportAsImage(request.getContent(), request.getColorTheme());
            String filename = exportService.generateFilename("png");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                .headers(headers)
                .body(imageContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Export Request DTO
     */
    public record ExportRequest(String content, String colorTheme) {}
}