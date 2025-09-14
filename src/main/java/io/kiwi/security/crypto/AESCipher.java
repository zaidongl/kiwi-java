package io.kiwi.security.crypto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Use AES-128-ECB for encrypting and decrypting secrets.
 */
public class AESCipher {
    private static final Logger logger = LogManager.getLogger(AESCipher.class);
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    public static final String ENV_VAR_AES_KEY = "KIWI_AES_KEY";
    public static final String PROPERTY_AES_KEY = "aes.key";

    private static AESCipher instance = null;
    private Cipher encryptCipher = null;
    private Cipher descriptCipher = null;

    // private constructor to prevent instantiation
    private AESCipher() throws Exception {
        String sKey = System.getProperty(PROPERTY_AES_KEY, System.getenv(ENV_VAR_AES_KEY));
        if (sKey == null || sKey.isEmpty()) {
            throw new IllegalArgumentException("AES key is not set. Please set it via system property 'aes.key' or environment variable 'KIWI_AES_KEY'");
        }
        encryptCipher = getCipher(Cipher.ENCRYPT_MODE, sKey);
        descriptCipher = getCipher(Cipher.DECRYPT_MODE, sKey);
    }

    public static AESCipher getInstance() throws Exception {
        if (instance == null) {
            instance = new AESCipher();
        }
        return instance;
    }

    private Cipher getCipher(int mode, String sKey) throws Exception {
        if (sKey == null) {
            throw new IllegalArgumentException("The key is null");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new IllegalArgumentException("The length of key is not 16 characters");
        }
        byte[] raw = sKey.getBytes(CHARSET);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);//"算法/模式/补码方式"
        cipher.init(mode, skeySpec);
        return cipher;
    }

    /**
     * Encrypts the given string using AES-128-ECB and returns a hex-encoded string.
     * @param sSrc - the string to be encrypted     *
     * @return the encrypted string in hex format
     */
    public String encrypt(String sSrc)
            throws Exception {
        byte[] encrypted = encryptCipher.doFinal(sSrc.getBytes(CHARSET));
        byte[] base64Encoded = Base64.getEncoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.formatHex(base64Encoded);//转换成16进制字符串
    }

    public String decrypt(String ciphertext) {
        try {
            HexFormat hexFormat = HexFormat.of();
            byte[] base64Decoded = hexFormat.parseHex(ciphertext);
            byte[] encrypted1 = Base64.getDecoder().decode(base64Decoded);//先用base64解密
            byte[] original = descriptCipher.doFinal(encrypted1);
            return new String(original, CHARSET);
        } catch (Exception ex) {
            logger.error("Error while decrypting {}, {}", ciphertext, ex.getMessage());
            return null;
        }
    }
}
