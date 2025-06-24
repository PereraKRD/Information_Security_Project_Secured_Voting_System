package model;

import java.security.PublicKey;

public class Voter {
    private String studentId;
    private PublicKey publicKey;
    private boolean isRegistered;

    public Voter(String studentId, PublicKey publicKey) {
        this.studentId = studentId;
        this.publicKey = publicKey;
        this.isRegistered = false;
    }

    // Getters and setters
    public String getStudentId() { return studentId; }
    public PublicKey getPublicKey() { return publicKey; }
    public boolean isRegistered() { return isRegistered; }
    public void setRegistered(boolean registered) { isRegistered = registered; }
}
