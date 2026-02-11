package com.fawry.adapter_template.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final ObjectMapper objectMapper;

    public RestTemplateLoggingInterceptor() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logExternal("Request", request.getURI().toString(),
                request.getMethod().name(),
                request.getHeaders().toString(),
                new String(body, StandardCharsets.UTF_8));

        ClientHttpResponse response = execution.execute(request, body);

        // Read and buffer the response body
        String responseBody = readBody(response);
        
        logExternal("Response",
                response.getStatusCode() + " " + response.getStatusText(),
                null,
                response.getHeaders().toString(),
                responseBody);

        // Return a new response with the buffered body
        return new BufferedClientHttpResponse(response, responseBody);
    }

    private void logExternal(String type, String uriOrStatus, String method, String headers, String body) {
        log.info("External {} ->", type);
        if (method != null) {
            log.info("Method: {}", method);
            log.info("URI: {}", uriOrStatus);
        } else {
            log.info("Status: {}", uriOrStatus);
        }
        log.info("Headers: {}", headers);

        if (body != null && !body.isBlank()) {
            log.info("Body:\n{}", formatJson(body));
        }
    }

    private String readBody(ClientHttpResponse response) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private String formatJson(String json) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            return json; // if not valid JSON, return as is
        }
    }

    // Custom response wrapper that provides the buffered body
    private static class BufferedClientHttpResponse implements ClientHttpResponse {
        private final ClientHttpResponse originalResponse;
        private final String body;
        private final byte[] bodyBytes;

        public BufferedClientHttpResponse(ClientHttpResponse originalResponse, String body) {
            this.originalResponse = originalResponse;
            this.body = body;
            this.bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(bodyBytes);
        }

        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            return originalResponse.getHeaders();
        }

        @Override
        public org.springframework.http.HttpStatusCode getStatusCode() throws IOException {
            return originalResponse.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return originalResponse.getStatusText();
        }

        @Override
        public void close() {
            originalResponse.close();
        }
    }
}
