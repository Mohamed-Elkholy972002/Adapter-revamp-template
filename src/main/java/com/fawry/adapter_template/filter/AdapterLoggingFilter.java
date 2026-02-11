package com.fawry.adapter_template.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class AdapterLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Create a custom wrapper that caches the request body
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);

        // Log request body BEFORE processing with nice formatting
        String requestBody = cachedRequest.getCachedBody();
        String formattedRequestBody = formatContent(requestBody);
        log.info("REQUEST BODY:\n{}", formattedRequestBody);

        // Wrap response for response logging
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // Process the request with the cached request
        filterChain.doFilter(cachedRequest, wrappedResponse);

        // Log response body AFTER processing with nice formatting
        String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
        String formattedResponseBody = formatContent(responseBody);
        log.info("RESPONSE BODY:\n{}", formattedResponseBody);

        // Copy the response body back to the original response
        wrappedResponse.copyBodyToResponse();
    }

    /**
     * Format content based on its type (XML or JSON)
     */
    private String formatContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return content;
        }

        String trimmedContent = content.trim();

        // Check if it's XML
        if (trimmedContent.startsWith("<") && trimmedContent.endsWith(">")) {
            // Check if XML is already well-formatted (has proper line breaks and indentation)
            if (isAlreadyFormatted(trimmedContent)) {
                return trimmedContent; // Return as-is if already formatted
            }
            return formatXml(trimmedContent);
        }

        // Check if it's JSON
        if ((trimmedContent.startsWith("{") && trimmedContent.endsWith("}")) ||
                (trimmedContent.startsWith("[") && trimmedContent.endsWith("]"))) {
            return formatJson(trimmedContent);
        }

        // Return as-is if not XML or JSON
        return content;
    }

    /**
     * Check if XML is already properly formatted
     */
    private boolean isAlreadyFormatted(String xml) {
        // Count the number of lines and check if it has reasonable indentation
        String[] lines = xml.split("\n");
        if (lines.length < 3) {
            return false; // Too few lines, probably not formatted
        }
        
        // Check if it has reasonable indentation (not excessive spaces)
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            int leadingSpaces = line.length() - line.trim().length();
            if (leadingSpaces > 20) { // If more than 20 leading spaces, it's probably over-indented
                return false;
            }
        }
        
        return true;
    }

    /**
     * Format XML with proper indentation
     */
    private String formatXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            log.warn("Failed to format XML, returning original content: {}", e.getMessage());
            return xml;
        }
    }

    /**
     * Format JSON with proper indentation
     */
    private String formatJson(String json) {
        try {
            // Simple JSON formatting - you can use Jackson ObjectMapper for more robust formatting
            StringBuilder formatted = new StringBuilder();
            int indentLevel = 0;
            boolean inString = false;
            boolean escapeNext = false;

            for (char c : json.toCharArray()) {
                if (escapeNext) {
                    formatted.append(c);
                    escapeNext = false;
                    continue;
                }

                if (c == '\\') {
                    formatted.append(c);
                    escapeNext = true;
                    continue;
                }

                if (c == '"') {
                    inString = !inString;
                    formatted.append(c);
                    continue;
                }

                if (!inString) {
                    if (c == '{' || c == '[') {
                        formatted.append(c).append('\n');
                        indentLevel++;
                        addIndent(formatted, indentLevel);
                    } else if (c == '}' || c == ']') {
                        formatted.append('\n');
                        indentLevel--;
                        addIndent(formatted, indentLevel);
                        formatted.append(c);
                    } else if (c == ',') {
                        formatted.append(c).append('\n');
                        addIndent(formatted, indentLevel);
                    } else if (c == ':') {
                        formatted.append(c).append(' ');
                    } else if (c != ' ') {
                        formatted.append(c);
                    }
                } else {
                    formatted.append(c);
                }
            }

            return formatted.toString();
        } catch (Exception e) {
            log.warn("Failed to format JSON, returning original content: {}", e.getMessage());
            return json;
        }
    }

    /**
     * Add indentation to the formatted string
     */
    private void addIndent(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
    }

    // Custom request wrapper that caches the request body
    public static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final String cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.cachedBody = readRequestBody(request);
        }

        private String readRequestBody(HttpServletRequest request) throws IOException {
            try (InputStream inputStream = request.getInputStream();
                 ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                return result.toString(StandardCharsets.UTF_8);
            }
        }

        public String getCachedBody() {
            return cachedBody;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            // Return a new input stream with the cached body
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // Not needed for this implementation
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
