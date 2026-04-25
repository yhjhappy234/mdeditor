package com.yhj.mdeditor.service;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.yhj.mdeditor.dto.MarkdownParseRequest;
import com.yhj.mdeditor.dto.MarkdownParseResponse;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

/**
 * Markdown Parse Service
 *
 * Uses Flexmark-java for Markdown parsing with GFM extensions
 */
@Service
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        MutableDataHolder options = new MutableDataSet();

        // Configure GFM compatibility
        options.setFrom(ParserEmulationProfile.GITHUB);

        // Enable extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(
            TablesExtension.create(),
            StrikethroughExtension.create(),
            AutolinkExtension.create(),
            TocExtension.create()
        ));

        // Configure renderer
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        options.set(HtmlRenderer.HARD_BREAK, "<br />\n");

        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    /**
     * Parse Markdown to HTML
     */
    public MarkdownParseResponse parse(MarkdownParseRequest request) {
        String content = request.getContent();

        Node document = parser.parse(content);
        String html = renderer.render(document);

        // Process social format if needed
        String socialHtml = request.isForSocial() ? generateSocialHtml(html, request) : null;

        // Calculate statistics
        int charCount = content.length();
        int lineCount = content.split("\n").length;
        int wordCount = countWords(content);
        String readTime = estimateReadTime(wordCount);

        return MarkdownParseResponse.builder()
            .html(html)
            .socialHtml(socialHtml)
            .characterCount(charCount)
            .lineCount(lineCount)
            .wordCount(wordCount)
            .estimatedReadTime(readTime)
            .build();
    }

    /**
     * Parse Markdown to HTML string
     */
    public String parseToHtml(String content) {
        Node document = parser.parse(content);
        return renderer.render(document);
    }

    /**
     * Generate social platform compatible HTML with inline styles
     */
    private String generateSocialHtml(String html, MarkdownParseRequest request) {
        // Add inline styles for social platforms (WeChat, etc.)
        StringBuilder styledHtml = new StringBuilder();
        styledHtml.append("<div style=\"font-family: 'Microsoft YaHei', 'PingFang SC', Arial, sans-serif; font-size: 16px; line-height: 1.8; color: #333; padding: 20px;\">");
        styledHtml.append(processInlineStyles(html, request));
        styledHtml.append("</div>");
        return styledHtml.toString();
    }

    /**
     * Process inline styles for social platforms
     */
    private String processInlineStyles(String html, MarkdownParseRequest request) {
        String colorTheme = request.getColorTheme() != null ? request.getColorTheme() : "meihei";
        String primaryColor = getPrimaryColor(colorTheme);

        // Style headings
        html = html.replaceAll("<h1>", "<h1 style=\"font-size: 24px; font-weight: bold; margin: 20px 0 10px; color: " + primaryColor + ";\">");
        html = html.replaceAll("<h2>", "<h2 style=\"font-size: 20px; font-weight: bold; margin: 18px 0 8px; color: " + primaryColor + ";\">");
        html = html.replaceAll("<h3>", "<h3 style=\"font-size: 18px; font-weight: bold; margin: 16px 0 6px; color: " + primaryColor + ";\">");
        html = html.replaceAll("<h4>", "<h4 style=\"font-size: 16px; font-weight: bold; margin: 14px 0 4px; color: " + primaryColor + ";\">");

        // Style code blocks
        html = html.replaceAll("<pre><code", "<pre style=\"background: #f6f8fa; padding: 16px; border-radius: 8px; overflow-x: auto; margin: 16px 0;\"><code style=\"font-family: 'SF Mono', Monaco, Consolas, monospace; font-size: 14px;\"");
        html = html.replaceAll("<code>", "<code style=\"background: rgba(251, 146, 60, 0.08); color: #ea580c; padding: 2px 6px; border-radius: 4px; font-family: 'SF Mono', Monaco, Consolas, monospace;\">");

        // Style blockquotes
        html = html.replaceAll("<blockquote>", "<blockquote style=\"border-left: 4px solid " + primaryColor + "; background: rgba(0, 0, 0, 0.05); padding: 12px 16px; margin: 16px 0; font-style: italic;\">");

        // Style tables
        html = html.replaceAll("<table>", "<table style=\"border-collapse: collapse; width: 100%; margin: 16px 0;\">");
        html = html.replaceAll("<thead>", "<thead style=\"background: #f6f8fa;\">");
        html = html.replaceAll("<th>", "<th style=\"border: 1px solid #dfe2e5; padding: 8px 12px; font-weight: bold;\">");
        html = html.replaceAll("<td>", "<td style=\"border: 1px solid #dfe2e5; padding: 8px 12px;\">");

        // Style links
        html = html.replaceAll("<a ", "<a style=\"color: " + primaryColor + "; text-decoration: underline;\" ");

        // Style lists
        html = html.replaceAll("<ul>", "<ul style=\"list-style-type: disc; padding-left: 24px; margin: 12px 0;\">");
        html = html.replaceAll("<ol>", "<ol style=\"list-style-type: decimal; padding-left: 24px; margin: 12px 0;\">");
        html = html.replaceAll("<li>", "<li style=\"margin: 4px 0;\">");

        return html;
    }

    /**
     * Get primary color based on theme
     */
    private String getPrimaryColor(String theme) {
        return switch (theme) {
            case "meihei" -> "#333333";
            case "github" -> "#0366d6";
            case "wechat" -> "#07c160";
            case "vuepress" -> "#3eaf7c";
            case "cobalt" -> "#0888ff";
            case "marry" -> "#fa5151";
            default -> "#333333";
        };
    }

    /**
     * Count words in content
     */
    private int countWords(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        // Split by whitespace and count
        String[] words = content.trim().split("\\s+");
        return words.length;
    }

    /**
     * Estimate read time (words / 200 per minute)
     */
    private String estimateReadTime(int wordCount) {
        int minutes = Math.max(1, wordCount / 200);
        return minutes + " min";
    }
}