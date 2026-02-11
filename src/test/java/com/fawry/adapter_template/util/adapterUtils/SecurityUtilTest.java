package com.fawry.adapter_template.util.adapterUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityUtilTest {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private PublicKeyLoader publicKeyLoader;

    private String publicKeyPath;
    private String privateKeyBase64;

    @BeforeEach
    void setUp() {
        // Set the public key path from your configuration
        publicKeyPath = "classpath:adapter/rsa-keys/btc/publicKey55520.pem";

        // Private key in PEM format (with headers) - rsaDecrypt will clean it automatically
        privateKeyBase64 = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAHd21pxRhMkVG5sT\n" +
                "W3F/+RzlfUdorK5OmqSyF1bCcM3Oh16VcdF3yZD9Ee0mKAglKrQuMpko5NftUiCo\n" +
                "jlt0WDIOj38H+urLogjOiDIdEvU2wYvep3meLOXq8o8zcNlG6OtPWNIthSla6V+5\n" +
                "MDvBKSXXlz/BRCucp8O53xosUMwIDiKnxvDwfeC2t4R/D5AnZZXqMW8P4Z9YZ9TH\n" +
                "ESU0qz4/cEGDh4Lijxa+apLQ7OItbR1qcHeEvmd+qxtRaQWiZwOVOHzgAWA2Oawi\n" +
                "x6akxVCmmgzADMxsDAFmPlwxcai0JZi+SqPH3ZThLCL480OiVdeJrpRhmi41AGfA\n" +
                "WwuL86sCAwEAAQKCAQAukQoywKvr3eh22GLKqUGne+ULksfM4iLxk+H5Fa3260xl\n" +
                "GAfXyMOvWpZERJ1SH3q35VIf1YruiDl9NXbYO0+to3NX5vptz5aHrEn2jtK29vMW\n" +
                "3GGXzIpfzOdj3n9ckoF0R+etdcT0S9WwVHVytWaes5znYyR5g4T5jFhk0WVlQi2g\n" +
                "1rBpQhxlc4ufzdzRJv6DYODscvY/UiDYqi7I4ZNBvXfu5/KysltcNMoBYTVVF+MR\n" +
                "0KwnqgQC0qopN97YA21c6ZWo8pXeMCrRnLMhn06gTpDZh+rt4F0eXo1XKrR9l7Bh\n" +
                "U3D520AvuUo/Crp9il1eXEOB1xl1TI/6kTdJD88RAoGBAMAQLj+yEIvf3RIGCD4Y\n" +
                "ysb/ZKLApSQiXC4q7vMzo4h3j0dqbGWZ6uj9/9PnhrWGnGPaTnSb2ZgRD5kriEzf\n" +
                "XcTGPeffiAr1JX4j5ulubYqdBkfbJTCh8WtRrZBp2yCRC1JmhKhpwi07l9SnpqsO\n" +
                "rFtMVgwSGKJbkaZcfGIJxOX9AoGBAJ87stEP1JCVCxma/UuxpYlhJ7yGVWNgFJ1y\n" +
                "LS6Ra6fIlbofdtaaz91EdIe8ZNNDdqiucDOnBApeyOPCq4wG0a1wmYdihF46vjp7\n" +
                "boHm0dU7QqwFtGOnXpk+TjKhGvma2rsD2V4cKC5ih/6Cv1EVObFDDZ2tfXc/YyS9\n" +
                "gnIo45zHAoGABYAxIufhq2/k8AA2OjkRjbOA2Vx1HlzRkQzvvQ1skJsEAoKcyign\n" +
                "XzbvHc2SwR4y2nVFK/fOupk6fiOOCs2W7OzUlxCC3/V3dm5mzaa5AnBO3r37CbKJ\n" +
                "rI4xT2KIwR0GWvzOrdghlJQRXCjVEC8iZpcAG+ZkyDmhKUea1F/9QK0CgYEAg1/m\n" +
                "1dQgxbUidlrItwX3lNQCy+ltU9M2zvwfyPcUAlddKTXCf6c07tMG7eKCsUdNBDj/\n" +
                "u64FiGTRjBDWJm6N2nI3SVr/6RrATlH+R9DfuPTuuizuuvWg+ABO1jjTn7Ki2n9/\n" +
                "fMTwZezHVTYckNJZSiYPetNj29oBe5Obp4KS4IsCgYAiGYPRPnEb3N6lYAAEeqbo\n" +
                "/L/BKrUUBKyh77XovwXNKmK8eHzPkE05HzCwGJgNmuqTIgoJ4qM6rrJgJn+rM0Le\n" +
                "CZV4zUnBw39UrtHpt7FG3nj+3zlVWqXcYXo9nMyloVE/XKy+phE9B1sigamzGRMO\n" +
                "NLmV5G7njoV0sN/JkfK7JA==\n" +
                "-----END PRIVATE KEY-----\n";
    }

    @Test
    void testEncryptDecryptCycle() {
        // Arrange
        String originalPlainText = "Hallan_bay-cf-your-server-key-1234567890";
        
        // Act - Encrypt with public key
        String publicKeyBase64 = publicKeyLoader.loadPublicKeyFromPem(publicKeyPath);
        String encrypted = securityUtil.rsaEncrypt(originalPlainText, publicKeyBase64);
        
        // Assert - Verify encryption produced something
        assertNotNull(encrypted, "Encrypted value should not be null");
        assertFalse(encrypted.isEmpty(), "Encrypted value should not be empty");
        assertNotEquals(originalPlainText, encrypted, "Encrypted value should be different from original");
        
        // Act - Decrypt with private key
        String decrypted = securityUtil.rsaDecrypt(encrypted, privateKeyBase64);
        
        // Assert - Verify decryption matches original
        assertEquals(originalPlainText, decrypted, 
            "Decrypted text should match original plain text");
        
        System.out.println("✓ Encryption/Decryption test passed!");
        System.out.println("Original: " + originalPlainText);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }

    @Test
    void testGenerateHalanSignatureAndDecrypt() {
        // Arrange
        String integratorName = "Hallan_bay";
        String serverKey = "your-server-key";
        long unixTime = System.currentTimeMillis() / 1000;
        
        // Act - Generate signature (encrypts with public key)
        String signature = securityUtil.generateHalanSignature(
            integratorName, serverKey, unixTime, publicKeyPath);
        
        // Assert - Verify signature was generated
        assertNotNull(signature, "Signature should not be null");
        assertFalse(signature.isEmpty(), "Signature should not be empty");
        System.out.println("Signature: " + signature);
        // Act - Decrypt signature with private key to verify
        String decryptedSignature = securityUtil.rsaDecrypt(signature, privateKeyBase64);
        String expectedPlainText = integratorName + "-cf-" + serverKey + "-" + unixTime;
        
        // Assert - Verify decrypted signature matches expected format
        assertEquals(expectedPlainText, decryptedSignature,
            "Decrypted signature should match expected plain text format");
        
        System.out.println("✓ Halan Signature test passed!");
        System.out.println("Expected: " + expectedPlainText);
        System.out.println("Signature: " + signature);
        System.out.println("Decrypted: " + decryptedSignature);
    }

    @Test
    void testEncryptWithPublicKey() {
        // Arrange
        String originalPlainText = "Test message for encryption";
        
        // Act - Load public key and encrypt
        String publicKeyBase64 = publicKeyLoader.loadPublicKeyFromPem(publicKeyPath);
        String encrypted = securityUtil.rsaEncrypt(originalPlainText, publicKeyBase64);
        
        // Assert - Verify encryption works
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
        assertNotEquals(originalPlainText, encrypted);
        
        System.out.println("✓ Encryption test passed!");
        System.out.println("Original: " + originalPlainText);
        System.out.println("Encrypted: " + encrypted);
    }
}

