package subtask_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Port for producers
    private final int producerPort;
    // Port for consumers
    private final int consumerPort;
    // Internal storage variable
    private int storage = 0;

    public Server(int producerPort, int consumerPort) {
        this.producerPort = producerPort;
        this.consumerPort = consumerPort;
    }

    public void start() throws IOException {
        // Initialize a thread to listen for producer messages
        Thread producerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(producerPort)) {
                Log.info("Server started on port " + producerPort + " for producers");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    // Start a thread to handle and serve the connected producer
                    new Thread(() -> handleClient(clientSocket, "Producer"), "Producer-Handler-"+producerPort).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Producer-Listener-" + producerPort);
        // Start producer listener
        producerThread.start();

        // Initialize a thread to listen for consumer messages
        Thread consumerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(consumerPort)) {
                Log.info("Server started on port " + consumerPort + " for consumers");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    // Start a thread to handle and serve the connected consumer
                    new Thread(() -> handleClient(clientSocket, "Consumer"), "Consumer-Handler-"+consumerPort).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Consumer-Listener-" + consumerPort);
        // Start consumer listener
        consumerThread.start();
    }

    private void handleClient(Socket clientSocket, String role) {
        String clientName = "Unknown";

        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            // First line = client name
            clientName = in.readLine();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                int value = Integer.parseInt(inputLine);

                if ("Producer".equalsIgnoreCase(role)) {
                    increment(value, clientName);
                } else if ("Consumer".equalsIgnoreCase(role)) {
                    decrement(value, clientName);
                }

                // Send response back
                out.println("Server storage: " + getStorage());
            }

        } catch (IOException e) {
            System.err.println("Connection with " + clientName + " (" + role + ") closed: " + e.getMessage());
        }
    }

    public synchronized void increment(int x, String clientName) {
        if (this.storage + x <= 1000) {
            this.storage += x;
            Log.info(clientName + " increased storage to " + this.storage);
        } else {
            Log.info(clientName + " Upper limit violation! ");
        }
    }

    public synchronized void decrement(int x, String clientName) {
        if (this.storage - x >= 1) {
            this.storage -= x;
            Log.info(clientName + " decreased storage to " + this.storage);
        } else {
            Log.info(clientName + " Lower limit violation! ");
        }
    }

    public synchronized int getStorage() {
        return this.storage;
    }
}
