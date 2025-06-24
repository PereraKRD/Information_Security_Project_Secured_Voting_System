package crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Smaller key for demo
        SecretKey key = keyGen.generateKey();

        System.out.println("AES Key Generated:");
        System.out.println("   • Key (Base64): " + keyToString(key));

        return key;
    }

    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        System.out.println("\nAES ENCRYPTION:");
        System.out.println("   • Plaintext: " + plaintext);
        System.out.println("   • AES Key: " + keyToString(key));

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        String ciphertext = Base64.getEncoder().encodeToString(encrypted);

        System.out.println("   • Ciphertext: " + ciphertext);

        return ciphertext;
    }

    public static String decrypt(String ciphertext, SecretKey key) throws Exception {
        System.out.println("\nAES DECRYPTION:");
        System.out.println("   • Ciphertext: " + ciphertext);
        System.out.println("   • AES Key: " + keyToString(key));

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        String plaintext = new String(decrypted);

        System.out.println("   • Decrypted text : " + plaintext);

        return plaintext;
    }

    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static SecretKey stringToKey(String keyStr) {
        byte[] decoded = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(decoded, "AES");
    }
}
