package io.kiwi.security.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AESCipherTest {
    @BeforeAll
    public static void setup() {
        String key = "1234567890123456"; // 16 bytes key for AES-128
        System.setProperty(AESCipher.PROPERTY_AES_KEY, key);
    }

    @Test
    public void testEncrypt() throws Exception {
        String plainText = "Hello, World!";
        String ciphertext = AESCipher.getInstance().encrypt(plainText);

        Assertions.assertNotNull(ciphertext);
        Assertions.assertEquals(48, ciphertext.length());
        Assertions.assertEquals("733161695230714841617978673131437954445831513d3d",
                ciphertext);
    }

    @Test
    public void testDecrypt() throws Exception {
        String ciphertext = "733161695230714841617978673131437954445831513d3d";
        String plaintext = AESCipher.getInstance().decrypt(ciphertext);
        Assertions.assertEquals("Hello, World!", plaintext);
    }
}
