package service;

import crypto.*;
import model.*;
import java.security.KeyPair;
import java.security.PublicKey;

public class ElectionAuthority {
    private KeyPair authorityKeyPair;
    private DataStorage storage;

    public ElectionAuthority() throws Exception {
        this.authorityKeyPair = RSAKeyManager.generateKeyPair();
        this.storage = DataStorage.getInstance();
        System.out.println("Election Authority initialized with RSA keys...");
        System.out.println("   • Authority Public Key: " +
                getAuthorityPublicKey());
        System.out.println("   • Authority Private Key: " +
                getAuthorityPrivateKey());
    }

    public String registerVoter(String studentId, PublicKey voterPublicKey) throws Exception {
        System.out.println("\n  V O T E R   R E G I S T R A T I O N   P R O C E S S");
        System.out.println("==========================================================");

        // Check if already registered
        if (storage.voterExists(studentId)) {
            throw new Exception("Voter already registered!");
        }

        // Verify eligibility (simple check)
        if (!isEligibleVoter(studentId)) {
            throw new Exception("Voter not eligible!");
        }

        // Create voter
        Voter voter = new Voter(studentId, voterPublicKey);
        voter.setRegistered(true);
        storage.addVoter(voter);

        System.out.println("Creating voter token...");

        // Generate token
        String salt = HashUtil.generateSalt();
        String tokenData = studentId + "|" + salt + "|" + System.currentTimeMillis();
        System.out.println("\nTOKEN CREATION:");
        System.out.println("   • Student ID: " + studentId);
        System.out.println("   • Salt: " + salt);
        System.out.println("   • Timestamp: " + System.currentTimeMillis());
        System.out.println("   • Token Data: " + tokenData);

        String tokenHash = HashUtil.sha256(tokenData);

        // Sign token with authority's private key
        String signedToken = DigitalSignature.sign(tokenHash, authorityKeyPair.getPrivate());

        // Store token
        VoterToken voterToken = new VoterToken(tokenHash, signedToken, studentId);
        storage.addToken(voterToken);

        System.out.println("\nVoter " + studentId + " registered successfully!");
        System.out.println("   • Token Hash: " + tokenHash);
        System.out.println("   • Signed Token: " + signedToken);

        return tokenHash + "|" + signedToken;
    }

    private boolean isEligibleVoter(String studentId) {
        boolean eligible = studentId != null &&
                studentId.startsWith("EG/20") &&
                studentId.length() >= 6 && studentId.length() <= 12;

        System.out.println("Eligibility Check:");
        System.out.println("   • Student ID: " + studentId);
        System.out.println("   • Eligible: " + (eligible ? "YES" : "NO"));

        return eligible;
    }

    public String getAuthorityPublicKey() {
        return RSAKeyManager.publicKeyToString(authorityKeyPair.getPublic());
    }

    public String getAuthorityPrivateKey() {
        return RSAKeyManager.privateKeyToString(authorityKeyPair.getPrivate());
    }

    public boolean verifyToken(String tokenHash, String signedToken) throws Exception {
        return DigitalSignature.verify(tokenHash, signedToken, authorityKeyPair.getPublic());
    }
}
