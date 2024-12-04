package pl.smyk.gympassbackend.member.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AESUtils {
    private static final String ALGORITHM = "AES";
    @Value("${AES_SECRET}")
    private String secret;

    private static String staticSecret;

    @PostConstruct
    public void init() {
        staticSecret = this.secret;
    }

    private static SecretKeySpec getSecretKey() {
        byte[] key = staticSecret.getBytes(StandardCharsets.UTF_8);
        if (key.length < 16) {
            byte[] newKey = new byte[16];
            System.arraycopy(key, 0, newKey, 0, key.length);
            key = newKey;
        } else if (key.length > 16) {
            byte[] newKey = new byte[16];
            System.arraycopy(key, 0, newKey, 0, 16);
            key = newKey;
        }
        return new SecretKeySpec(key, ALGORITHM);
    }

    public static String encrypt(String data) throws Exception {
        SecretKeySpec key = getSecretKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = getSecretKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}
