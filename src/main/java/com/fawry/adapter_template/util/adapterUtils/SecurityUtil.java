package com.fawry.adapter_template.util.adapterUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final PublicKeyLoader publicKeyLoader;

    /**
     * Encrypts a string using RSA encryption with the provided public key
     * @param plainText The text to encrypt
     * @param publicKeyBase64 The public key in clean Base64 format (no PEM headers, already cleaned)
     * @return Base64 encoded encrypted string
     */
    public String rsaEncrypt(String plainText, String publicKeyBase64) {
        try {
            // Basic validation (key is already cleaned by PublicKeyLoader)
            if (publicKeyBase64 == null || publicKeyBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Public key cannot be null or empty");
            }
            
            // Decode the public key from Base64
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            log.debug("Using public key for encryption");
            
            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Encrypt the plain text
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            log.debug("Encryption completed successfully");
            
            // Return Base64 encoded encrypted string
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Error during RSA encryption: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to encrypt with RSA", e);
        }
    }

    /**
     * Decrypts a Base64 encoded encrypted string using RSA with the provided private key
     * Supports PKCS#8 format (-----BEGIN PRIVATE KEY-----)
     * @param encryptedBase64 The Base64 encoded encrypted string
     * @param privateKeyBase64 The private key in PEM format or clean Base64 format (PKCS#8)
     * @return Decrypted plain text string
     */
    public String rsaDecrypt(String encryptedBase64, String privateKeyBase64) {
        try {
            // Basic validation
            if (encryptedBase64 == null || encryptedBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Encrypted data cannot be null or empty");
            }
            if (privateKeyBase64 == null || privateKeyBase64.trim().isEmpty()) {
                throw new IllegalArgumentException("Private key cannot be null or empty");
            }

            // Clean private key (remove PEM headers if present)
            String cleanedKey = privateKeyBase64
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            // Decode the private key from Base64
            byte[] privateKeyBytes = Base64.getDecoder().decode(cleanedKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            log.debug("Using private key for decryption");

            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Decode encrypted data from Base64
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);

            // Decrypt the data
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            log.debug("Decryption completed successfully");

            // Return decrypted plain text
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error during RSA decryption: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to decrypt with RSA", e);
        }
    }

    /**
     * Generates RSA signature for Halan API authorization
     * Format: RSA-Encryption(integratorName + "-cf-" + serverKey + "-" + unixTime)
     * @param integratorName The integrator name
     * @param serverKey The server key
     * @param unixTime Unix timestamp
     * @param publicKeyPath The path to the public key PEM file
     * @return Base64 encoded encrypted signature
     */
    public String generateHalanSignature(String integratorName, String serverKey, long unixTime, String publicKeyPath) {
        // Load public key from PEM file (handles all PEM parsing and validation)
        String publicKeyBase64 = publicKeyLoader.loadPublicKeyFromPem(publicKeyPath);
        
        String plainText = integratorName + "-cf-" + serverKey + "-" + unixTime;
//        log.info("Halan signature plain text: {}", plainText);
        return rsaEncrypt(plainText, publicKeyBase64);
    }
}
