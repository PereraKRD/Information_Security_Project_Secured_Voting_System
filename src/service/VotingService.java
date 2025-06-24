package service;

import crypto.*;
import model.*;
import java.security.KeyPair;
import javax.crypto.SecretKey;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

public class VotingService {
    private KeyPair votingServerKeyPair;
    private DataStorage storage;
    private ElectionAuthority authority;

    public VotingService(ElectionAuthority authority) throws Exception {
        this.votingServerKeyPair = RSAKeyManager.generateKeyPair();
        this.storage = DataStorage.getInstance();
        this.authority = authority;
        System.out.println("Voting Service initialized with RSA keys");
        System.out.println("   • Voting Server Public Key: " +
                getVotingServerPublicKey());
        System.out.println("   • Voting Server Private Key: " +
                getVotingServerPrivateKey());
    }

    public String submitVote(String candidateName, String tokenData,
                             String voterStudentId) throws Exception {

        System.out.println("\nV O T E   S U B M I S S I O N   P R O C E S S");
        System.out.println("=================================================");
        System.out.println("   • Candidate: " + candidateName);
        System.out.println("   • Voter: " + voterStudentId);

        // Parse token data
        String[] parts = tokenData.split("\\|");
        if (parts.length != 2) {
            throw new Exception("Invalid token format!");
        }

        String tokenHash = parts[0];
        String signedToken = parts[1];

        System.out.println("   • Token Hash: " + tokenHash);
        System.out.println("   • Signed Token: " + signedToken);

        // Verify token with Election Authority
        if (!authority.verifyToken(tokenHash, signedToken)) {
            throw new Exception("Invalid token signature!");
        }

        // Check if token already used
        VoterToken token = storage.getToken(tokenHash);
        if (token == null) {
            throw new Exception("Token not found!");
        }

        if (token.isUsed()) {
            throw new Exception("Vote already cast! Double voting not allowed.");
        }

        // Validate candidate
        if (!storage.getCandidates().contains(candidateName)) {
            throw new Exception("Invalid candidate!");
        }

        System.out.println("\n V O T E   E N C R Y P T I O N   P R O C E S S");
        System.out.println("==================================================");

        // Encrypt vote
        SecretKey aesKey = AESUtil.generateKey();
        String encryptedVote = AESUtil.encrypt(candidateName, aesKey);

        // Encrypt AES key with voting server's public key
        String aesKeyStr = AESUtil.keyToString(aesKey);
        String encryptedAESKey = RSAKeyManager.encrypt(aesKeyStr,
                votingServerKeyPair.getPublic());

        // Create vote hash for integrity
        String voteHash = HashUtil.sha256(candidateName);

        // Generate confirmation ID
        String confirmationId = UUID.randomUUID().toString();

        System.out.println("\nVOTE PACKAGE CREATION:");
        System.out.println("   • Vote ID: " + UUID.randomUUID().toString());
        System.out.println("   • Encrypted Vote: " + encryptedVote);
        System.out.println("   • Encrypted AES Key: " + encryptedAESKey);
        System.out.println("   • Vote Hash: " + voteHash);
        System.out.println("   • Confirmation ID: " + confirmationId);

        // Create and store vote
        Vote vote = new Vote(UUID.randomUUID().toString(), encryptedVote,
                encryptedAESKey, voteHash, confirmationId);
        storage.addVote(vote);

        // Mark token as used
        token.setUsed(true);

        System.out.println("\nVote submitted successfully!");
        System.out.println("   • Confirmation ID: " + confirmationId);

        return confirmationId;
    }

    public Map<String, Integer> countingVotes() throws Exception {
        System.out.println("\nV O T E   C O U N T I N G   P R O C E S S");
        System.out.println("=============================================");

        Map<String, Integer> results = new HashMap<>();

        // Initialize candidates with 0 votes
        for (String candidate : storage.getCandidates()) {
            results.put(candidate, 0);
        }

        System.out.println("Decrypting and counting votes...");

        // Decrypt and count votes
        int voteNumber = 1;
        for (Vote vote : storage.getAllVotes()) {
            try {
                System.out.println("\n--- Processing Vote #" + voteNumber + " ---");
                System.out.println("Vote ID: " + vote.getVoteId());

                // Decrypt AES key
                String aesKeyStr = RSAKeyManager.decrypt(vote.getEncryptedAESKey(),
                        votingServerKeyPair.getPrivate());
                SecretKey aesKey = AESUtil.stringToKey(aesKeyStr);

                // Decrypt vote
                String candidateName = AESUtil.decrypt(vote.getEncryptedVote(), aesKey);

                // Verify integrity
                String computedHash = HashUtil.sha256(candidateName);
                System.out.println("\n CHECKING INTEGRITY :");
                System.out.println("   • Stored Hash: " + vote.getVoteHash());
                System.out.println("   • Computed Hash: " + computedHash);
                System.out.println("   • Match: " + (computedHash.equals(vote.getVoteHash()) ? "YES" : "NO"));

                if (!computedHash.equals(vote.getVoteHash())) {
                    System.out.println("Vote integrity check failed for vote: " + vote.getVoteId());
                    continue;
                }

                // Count vote
                results.put(candidateName, results.get(candidateName) + 1);
                System.out.println("   • Vote counted for: " + candidateName);

                voteNumber++;

            } catch (Exception e) {
                System.out.println("Error processing vote: " + vote.getVoteId());
                System.out.println("   Error: " + e.getMessage());
                voteNumber++;
            }
        }

        System.out.println("Total votes processed: " + (voteNumber - 1));

        return results;
    }

    public String getVotingServerPublicKey() {
        return RSAKeyManager.publicKeyToString(votingServerKeyPair.getPublic());
    }

    public String getVotingServerPrivateKey() {
        return RSAKeyManager.privateKeyToString(votingServerKeyPair.getPrivate());
    }
}