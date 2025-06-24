package crypto;

import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class RSAKeyManager {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024); // Smaller key for demo

        return keyGen.generateKeyPair();
    }

    public static String encrypt(String plaintext, PublicKey publicKey) throws Exception {
        System.out.println("\nRSA ENCRYPTION:");
        System.out.println("   • Plaintext: " + plaintext);
        System.out.println("   • Public Key: " + publicKeyToString(publicKey));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        String ciphertext = Base64.getEncoder().encodeToString(encrypted);

        System.out.println("   • Ciphertext: " + ciphertext);

        return ciphertext;
    }

    public static String decrypt(String ciphertext, PrivateKey privateKey) throws Exception {
        System.out.println("\nRSA DECRYPTION:");
        System.out.println("   • Ciphertext: " + ciphertext);
        System.out.println("   • Private Key: " + privateKeyToString(privateKey));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);
        byte[] decrypted = cipher.doFinal(decoded);
        String plaintext = new String(decrypted);

        System.out.println("   • Decrypted text : " + plaintext);

        return plaintext;
    }

    public static String publicKeyToString(PublicKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String privateKeyToString(PrivateKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}