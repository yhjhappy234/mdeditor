package com.yhj.mdeditor.service;

import com.yhj.mdeditor.dto.MarkdownParseRequest;
import com.yhj.mdeditor.dto.MarkdownParseResponse;
import com.yhj.mdeditor.exception.ExportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Export Service
 *
 * Handles PDF and image export using Flying Saucer for PDF generation
 */
@Service
public class ExportService {

    @Value("${export.pdf.margin:10}")
    private int pdfMargin;

    @Value("${export.image.scale:2}")
    private int imageScale;

    private final MarkdownService markdownService;

    public ExportService(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    /**
     * Export Markdown content as PDF
     */
    public byte[] exportAsPdf(String content, String colorTheme) throws ExportException {
        MarkdownParseRequest request = MarkdownParseRequest.builder()
            .content(content)
            .colorTheme(colorTheme)
            .forSocial(true)
            .build();

        MarkdownParseResponse response = markdownService.parse(request);
        String html = wrapHtmlForPdf(response.getSocialHtml());

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ExportException("Failed to export PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Export Markdown content as image (returns placeholder - requires browser rendering)
     */
    public byte[] exportAsImage(String content, String colorTheme) throws ExportException {
        // Note: True image export requires browser-based rendering (html2canvas)
        // This implementation returns PDF as fallback
        return exportAsPdf(content, colorTheme);
    }

    /**
     * Generate filename for export
     */
    public String generateFilename(String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
        return "markdown-export-" + now.format(formatter) + "." + extension;
    }

    /**
     * Wrap HTML content for PDF rendering
     */
    private String wrapHtmlForPdf(String htmlContent) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8"/>
                <style>
                    body {
                        font-family: 'Microsoft YaHei', 'PingFang SC', Arial, sans-serif;
                        font-size: 14px;
                        line-height: 1.8;
                        color: #333;
                        padding: 40px;
                    }
                    h1 { font-size: 24px; font-weight: bold; margin: 20px 0 10px; }
                    h2 { font-size: 20px; font-weight: bold; margin: 18px 0 8px; }
                    h3 { font-size: 18px; font-weight: bold; margin: 16px 0 6px; }
                    h4 { font-size: 16px; font-weight: bold; margin: 14px 0 4px; }
                    p { margin: 10px 0; }
                    code {
                        font-family: 'SF Mono', Monaco, Consolas, monospace;
                        background: #f6f8fa;
                        padding: 2px 6px;
                        border-radius: 4px;
                    }
                    pre {
                        background: #f6f8fa;
                        padding: 16px;
                        border-radius: 8px;
                        overflow-x: auto;
                    }
                    blockquote {
                        border-left: 4px solid #333;
                        background: rgba(0,0,0,0.05);
                        padding: 12px 16px;
                        margin: 16px 0;
                    }
                    table {
                        border-collapse: collapse;
                        width: 100%;
                    }
                    th, td {
                        border: 1px solid #dfe2e5;
                        padding: 8px 12px;
                    }
                    th { background: #f6f8fa; }
                    img { max-width: 100%; }
                </style>
            </head>
            <body>
                %s
            </body>
            </html>
            """.formatted(htmlContent);
    }
}