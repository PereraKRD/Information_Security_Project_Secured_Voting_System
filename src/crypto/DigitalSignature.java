package crypto;

import crypto.*;
import java.security.*;
import java.util.Base64;

public class DigitalSignature {

    public static String sign(String message, PrivateKey privateKey) throws Exception {
        System.out.println("\nDIGITAL SIGNATURE CREATION :");
        System.out.println("   • Message: " + message);
        System.out.println("   • Private Key: " + getPrivateKey(privateKey));

        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedBytes = signature.sign();
        String signatureStr = Base64.getEncoder().encodeToString(signedBytes);

        System.out.println("   • Digital Signature: " + signatureStr);

        return signatureStr;
    }

    public static boolean verify(String message, String signatureStr, PublicKey publicKey) throws Exception {
        System.out.println("\nDIGITAL SIGNATURE VERIFICATION:");
        System.out.println("   • Message: " + message);
        System.out.println("   • Signature: " + signatureStr);
        System.out.println("   • Public Key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));

        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        boolean isValid = signature.verify(signatureBytes);

        System.out.println("   • Verification Result: " + (isValid ? "VALID" : "INVALID"));

        return isValid;
    }

    public String getPublicKey(PublicKey publicKey) {
        return RSAKeyManager.publicKeyToString(publicKey);
    }

    public static String getPrivateKey(PrivateKey privateKey) {
        return RSAKeyManager.privateKeyToString(privateKey);
    }
}
