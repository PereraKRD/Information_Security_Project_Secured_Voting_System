package model;

public class VoterToken {
    private String tokenHash;
    private String signedToken;
    private String studentId;
    private boolean isUsed;

    public VoterToken(String tokenHash, String signedToken, String studentId) {
        this.tokenHash = tokenHash;
        this.signedToken = signedToken;
        this.studentId = studentId;
        this.isUsed = false;
    }

    // Getters and setters
    public String getTokenHash() { return tokenHash; }
    public String getSignedToken() { return signedToken; }
    public String getStudentId() { return studentId; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
}
