package com.fawry.adapter_template.util.adapterUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PublicKeyLoader {
    private final ConcurrentHashMap<String, String> keyCache = new ConcurrentHashMap<>();

    /**
     * Loads public key from PEM file and returns it as clean Base64 string (no PEM headers)
     * @param publicKeyPath Path to the PEM file (supports classpath: prefix)
     * @return Clean Base64 encoded public key string (PEM headers removed)
     */
    public String loadPublicKeyFromPem(String publicKeyPath) {
        if (publicKeyPath == null || publicKeyPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Public key path cannot be null or empty");
        }

        // Check cache first
        String cachedKey = keyCache.get(publicKeyPath);
        if (cachedKey != null) {
            return cachedKey;
        }

        try {
            String pemContent;
            
            // Handle classpath resources
            if (publicKeyPath.startsWith("classpath:")) {
                String resourcePath = publicKeyPath.substring("classpath:".length());
                ClassPathResource resource = new ClassPathResource(resourcePath);
                pemContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            } else {
                // Handle file system paths
                pemContent = new String(Files.readAllBytes(Paths.get(publicKeyPath)), StandardCharsets.UTF_8);
            }

            // Extract Base64 content from PEM format
            String cleanedKey = pemContent
                    .replace("\uFEFF", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                    .replace("-----END RSA PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            if (cleanedKey.isEmpty()) {
                throw new IllegalArgumentException("Invalid PEM file: no key content found in " + publicKeyPath);
            }

            // Validate key length (RSA 2048-bit X.509 key should be ~392 chars in Base64)
            if (cleanedKey.length() < 100) {
                throw new IllegalArgumentException("Public key appears to be truncated. Expected ~392 chars for RSA 2048-bit, got: " + cleanedKey.length() + " in file: " + publicKeyPath);
            }

            // Validate the key by parsing it
            try {
                byte[] publicKeyBytes = Base64.getDecoder().decode(cleanedKey);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                keyFactory.generatePublic(keySpec); // Validate key format
                log.debug("Successfully loaded and validated public key from: {}", publicKeyPath);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid public key format in file: " + publicKeyPath, e);
            }

            // Cache the cleaned key (no PEM headers)
            keyCache.put(publicKeyPath, cleanedKey);
            return cleanedKey;

        } catch (IOException e) {
            log.error("Error loading public key from file: {}", publicKeyPath, e);
            throw new RuntimeException("Failed to load public key from file: " + publicKeyPath, e);
        }
    }

    /**
     * Clears the key cache (useful for testing or key rotation)
     */
    public void clearCache() {
        keyCache.clear();
    }
}

