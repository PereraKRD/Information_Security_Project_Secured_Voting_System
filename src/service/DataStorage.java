package service;

import model.*;
import java.util.*;

public class DataStorage {
    private static DataStorage instance;

    private Map<String, Voter> voters; // studentId -> Voter
    private Map<String, VoterToken> tokens; // tokenHash -> VoterToken
    private List<Vote> votes;
    private List<String> candidates;

    private DataStorage() {
        voters = new HashMap<>();
        tokens = new HashMap<>();
        votes = new ArrayList<>();
        candidates = Arrays.asList("Ryan Perera", "Lasith Perera", "Amanatha Suraweera", "Nithil Sheshan");
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    // Voter operations
    public void addVoter(Voter voter) {
        voters.put(voter.getStudentId(), voter);
    }

    public Voter getVoter(String studentId) {
        return voters.get(studentId);
    }

    public boolean voterExists(String studentId) {
        return voters.containsKey(studentId);
    }

    // Token operations
    public void addToken(VoterToken token) {
        tokens.put(token.getTokenHash(), token);
    }

    public VoterToken getToken(String tokenHash) {
        return tokens.get(tokenHash);
    }

    // Vote operations
    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public List<Vote> getAllVotes() {
        return new ArrayList<>(votes);
    }

    // Candidates
    public List<String> getCandidates() {
        return new ArrayList<>(candidates);
    }

    // Utility methods
    public void printStatistics() {
        System.out.println("\n  S Y S T E M   S T A T I S T I C S");
        System.out.println("=======================================\n");
        System.out.println("   • Registered Voters: " + voters.size());
        System.out.println("   • Generated Tokens: " + tokens.size());
        System.out.println("   • Votes Cast: " + votes.size());
        System.out.println("   • Available Candidates: " + candidates);

        if (!votes.isEmpty()) {
            System.out.println("\nVote Details:");
            for (int i = 0; i < votes.size(); i++) {
                Vote vote = votes.get(i);
                System.out.println("   Vote #" + (i+1) + ":");
                System.out.println("     • Vote ID: " + vote.getVoteId());
                System.out.println("     • Confirmation: " + vote.getConfirmationId());
                System.out.println("     • Timestamp: " + new java.util.Date(vote.getTimestamp()));
                System.out.println("     • Encrypted Vote: " + vote.getEncryptedVote());
                System.out.println("     • Vote Hash: " + vote.getVoteHash());
            }
        }

        if (!tokens.isEmpty()) {
            System.out.println("\nToken Status:");
            for (VoterToken token : tokens.values()) {
                System.out.println("   • Student: " + token.getStudentId() +
                        " | Used: " + (token.isUsed() ? "YES" : "NO"));
            }
        }
    }
}
