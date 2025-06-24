import crypto.*;
import model.*;
import service.*;
import java.security.KeyPair;
import java.util.*;

public class VotingSystemMain {
    private static Scanner scanner = new Scanner(System.in);
    private static ElectionAuthority authority;
    private static VotingService votingService;
    private static DataStorage storage;

    // Simulate voter key pairs (in real system, voters would generate these)
    private static Map<String, KeyPair> voterKeyPairs = new HashMap<>();

    public static void main(String[] args) {
        try {
            System.out.println("   S E C U R E   D I G I T A L   V O T I N G   S Y S T E M");
            System.out.println("=============================================================");

            // Initialize system
            authority = new ElectionAuthority();
            votingService = new VotingService(authority);
            storage = DataStorage.getInstance();

            // Main menu loop
            while (true) {
                showMainMenu();
                int choice = getIntInput();

                switch (choice) {
                    case 1:
                        registerVoter();
                        break;
                    case 2:
                        castVote();
                        break;
                    case 3:
                        viewResults();
                        break;
                    case 4:
                        storage.printStatistics();
                        break;
                    case 5:
                        demonstrateSecurityFeatures();
                        break;
                    case 6:
                        System.out.println("Thank you for using the System!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            System.out.println("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showMainMenu() {
        System.out.println("\n M A I N   M E N U \n");
        System.out.println("1. Register Voter");
        System.out.println("2. Cast Vote");
        System.out.println("3. View Results");
        System.out.println("4. Voters");
        System.out.println("5. System Guide");
        System.out.println("6. Exit");
        System.out.print("Choose option (1-6): ");
    }

    private static void registerVoter() {
        try {
            System.out.println("\n V O T E R   R E G I S T R A T I O N");
            System.out.println("=========================================");
            System.out.print("Enter Student ID (e.g., STU001234): ");
            String studentId = scanner.nextLine().trim();

            System.out.println("\nGenerating voter's RSA key pair...");
            // Generate key pair for voter
            KeyPair voterKeyPair = RSAKeyManager.generateKeyPair();
            voterKeyPairs.put(studentId, voterKeyPair);

            System.out.println("\nSending registration request to Election Authority...");
            // Register with Election Authority
            String tokenData = authority.registerVoter(studentId, voterKeyPair.getPublic());

            System.out.println("\n REGISTRATION COMPLETE!");
            System.out.println("===============================");
            System.out.println("   • Your voting token: " + tokenData);
            System.out.println("N O T E :  Use this token when you're casting your vote!");

        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void castVote() {
        try {
            System.out.println("\n C A S T   V O T E");
            System.out.println("=========================");

            // Get voter info
            System.out.print("Enter your Student ID: ");
            String studentId = scanner.nextLine().trim();

            if (!storage.voterExists(studentId)) {
                System.out.println("Voter not found! Please register first.");
                return;
            }

            Voter voter = storage.getVoter(studentId);
            if (!voter.isRegistered()) {
                System.out.println("Voter registration incomplete!");
                return;
            }

            System.out.print("Enter your voting token: ");
            String tokenData = scanner.nextLine().trim();

            // VALIDATION 2: Basic token format check
            String[] tokenParts = tokenData.split("\\|");
            if (tokenParts.length != 2) {
                System.out.println("Invalid token format!");
                return;
            }

            String tokenHash = tokenParts[0];
            String signedToken = tokenParts[1];

            // VALIDATION 3: Check if token exists in system
            VoterToken voterToken = storage.getToken(tokenHash);
            if (voterToken == null) {
                System.out.println("Token not found in system!");
                return;
            }

            // VALIDATION 4: Verify token belongs to this voter
            if (!voterToken.getStudentId().equals(studentId)) {
                System.out.println("Token does not belong to this voter!");
                return;
            }

            // VALIDATION 5: Check if token already used
            if (voterToken.isUsed()) {
                System.out.println("Token already used! You have already voted.");
                return;
            }

            // VALIDATION 6: Verify token signature
            authority.verifyToken(tokenHash, signedToken);

            // Show candidates
            System.out.println("\nAvailable Candidates:");
            List<String> candidates = storage.getCandidates();
            for (int i = 0; i < candidates.size(); i++) {
                System.out.println((i + 1) + ". " + candidates.get(i));
            }

            System.out.print("Select candidate (1-" + candidates.size() + "): ");
            int choice = getIntInput() - 1;

            if (choice < 0 || choice >= candidates.size()) {
                System.out.println("Invalid selection!");
                return;
            }

            String selectedCandidate = candidates.get(choice);

            System.out.println("\nSelected Candidate: " + selectedCandidate);
            System.out.println("Submitting vote...");

            // Submit vote
            String confirmationId = votingService.submitVote(selectedCandidate, tokenData, studentId);

            System.out.println("\n VOTE CASTED SUCCESSFULLY!");
            System.out.println("=====================================");
            System.out.println("   • Your confirmation ID: " + confirmationId);
            System.out.println("NOTE : You cannot vote again with this token");

        } catch (Exception e) {
            System.out.println("Voting failed: " + e.getMessage());
        }
    }

    private static void viewResults() {
        try {
            System.out.println("\nE L E C T I O N   R E S U L T S");
            System.out.println("====================================");

            Map<String, Integer> results = votingService.countingVotes();
            int totalVotes = results.values().stream().mapToInt(Integer::intValue).sum();

            System.out.println("\nFINAL RESULTS:");
            System.out.println("   • Total Votes Cast: " + totalVotes);
            System.out.println();

            // Sort results by vote count
            results.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        String candidate = entry.getKey();
                        int votes = entry.getValue();
                        double percentage = totalVotes > 0 ? (votes * 100.0 / totalVotes) : 0;

                        System.out.printf("%-15s: %d votes (%.1f%%)", candidate, votes, percentage);
                        if (votes > 0) {
                            // Add visual bar
                            int barLength = Math.min(20, (int)(percentage / 5));
                            System.out.print(" ");
                            for (int i = 0; i < barLength; i++) {
                                System.out.print("█");
                            }
                        }
                        System.out.println();
                    });

            if (totalVotes > 0) {
                String winner = results.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .get().getKey();
                System.out.println("\n   • Winner: " + winner);
            }

        } catch (Exception e) {
            System.out.println("Error calculating results: " + e.getMessage());
        }
    }

    private static void demonstrateSecurityFeatures() {
        System.out.println("\nN E W   U S E R   G U I D E");
        System.out.println("================================ ");


    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}