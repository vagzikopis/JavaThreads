package subtask_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    // Server port
    private final int port;
    // Hashtable
    private final Hashtable<Integer, Integer> h;
    // Server socket
    private ServerSocket serverSocket;
    // Input pattern
    private static final Pattern INPUT_PATTERN =
            Pattern.compile("\\((\\d)(?:,(-?\\d+))?(?:,(-?\\d+))?\\)");

    public Server(int port) throws IOException {
        // Initialize values
        this.port = port;
        this.h = new Hashtable<>(1 << 20);
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        listen();
    }

    public void listen() throws IOException {
        System.out.println("Waiting for clients...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            // Start a thread to serve the connected client
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // Read clients message
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();

                // Termination message
                if (inputLine.equals("(0,0)")) {
                    out.println("Goodbye!");
                    break;
                }

                // Validate message format
                Matcher matcher = INPUT_PATTERN.matcher(inputLine);
                if (!matcher.matches()) {
                    out.println("Invalid format");
                    continue;
                }

                // Parse a,b,c
                int a = Integer.parseInt(matcher.group(1));
                String bStr = matcher.group(2);
                String cStr = matcher.group(3);

                try {
                    switch (a) {
                        // Insert scenario
                        case 1 -> {
                            if (bStr != null && cStr != null) {
                                int b = Integer.parseInt(bStr);
                                int c = Integer.parseInt(cStr);
                                h.put(b, c);
                                out.println(1);
                            } else out.println(0);
                        }
                        // Remove scenario
                        case 2 -> {
                            if (bStr != null) {
                                int b = Integer.parseInt(bStr);
                                Integer removed = h.remove(b);
                                out.println(removed != null ? 1 : 0);
                            } else out.println(0);
                        }
                        // Search scenario
                        case 3 -> {
                            if (bStr != null) {
                                int b = Integer.parseInt(bStr);
                                Integer result = h.get(b);
                                out.println(result != null ? result : 0);
                            } else out.println(0);
                        }
                        // Unknown command
                        default -> out.println(0);
                    }
                } catch (NumberFormatException ex) {
                    out.println("Invalid integer value");
                }

                // Print updated hashtable
                System.out.println("Hashtable: " + h);
            }

        } catch (IOException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        new Server(8888);
    }
}
