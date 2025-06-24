package crypto;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {

    public static String sha256(String input) throws Exception {
        System.out.println("\nSHA-256 HASHING:");
        System.out.println("   • Input: " + input);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        String hashStr = Base64.getEncoder().encodeToString(hash);

        System.out.println("   • SHA-256 Hash: " + hashStr);

        return hashStr;
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        String saltStr = Base64.getEncoder().encodeToString(salt);

        System.out.println("\nSALT GENERATION:");
        System.out.println("   • Generated Salt: " + saltStr);

        return saltStr;
    }
}
