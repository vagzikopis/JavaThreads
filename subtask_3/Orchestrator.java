package subtask_3;

import java.util.Scanner;

public class Orchestrator {
    // Total number of clients
    private final static int numClients = 4;
    // Server port
    private final static int port = 8888;

    public static void main(String[] args) {
        // Initialize scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Array to store client threads
        Client[] clients = new Client[numClients];

        // Initialize clients
        for (int i = 0; i < numClients; i++) {
            clients[i] = new Client("127.0.0.1", port, "Client-" + (i + 1));
            clients[i].start();
        }

        System.out.println("Enter commands in the format:");
        System.out.println("[client-number] command");
        System.out.println("Example: 1 (1,1,100)");
        System.out.println("Use (0,0) to stop a client.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            // Validation check
            String[] parts = line.split("\\s+", 2);
            if (parts.length != 2) {
                System.out.println("Invalid format. Use: client-number command");
                continue;
            }

            // Parse client number
            int clientNum;
            try {
                clientNum = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid client number.");
                continue;
            }

            if (clientNum < 1 || clientNum > numClients) {
                System.out.println("Client number must be between 1 and " + numClients);
                continue;
            }

            // Send command to the requested client
            String command = parts[1];
            clients[clientNum - 1].addCommand(command);
        }
    }
}
