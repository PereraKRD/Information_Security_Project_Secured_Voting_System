package model;

public class Vote {
    private String voteId;
    private String encryptedVote;
    private String encryptedAESKey;
    private String voteHash;
    private String confirmationId;
    private long timestamp;

    public Vote(String voteId, String encryptedVote, String encryptedAESKey,
                String voteHash, String confirmationId) {
        this.voteId = voteId;
        this.encryptedVote = encryptedVote;
        this.encryptedAESKey = encryptedAESKey;
        this.voteHash = voteHash;
        this.confirmationId = confirmationId;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public String getVoteId() { return voteId; }
    public String getEncryptedVote() { return encryptedVote; }
    public String getEncryptedAESKey() { return encryptedAESKey; }
    public String getVoteHash() { return voteHash; }
    public String getConfirmationId() { return confirmationId; }
    public long getTimestamp() { return timestamp; }
}
