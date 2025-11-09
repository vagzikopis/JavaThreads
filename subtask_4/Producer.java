package subtask_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class Producer extends Thread {

    private final String hostname;
    private final List<Integer> ports;

    public Producer(String hostname, List<Integer> ports) {
        this.hostname = hostname;
        this.ports = ports;
    }

    public void startProducer() {
        Random rand = new Random();
        Log.info(" Starting Producer");
        while (true) {
            int port = ports.get(rand.nextInt(ports.size()));

            try (
                    Socket socket = new Socket(hostname, port);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out.println(Thread.currentThread().getName());

                int next = rand.nextInt(10, 101);
                Log.info(" Sent: " + next);
                // Send value to server
                out.println(next);
                String response = in.readLine();
                Log.info("Server " + hostname + ":" + port + " Response: " + response);
                try { Thread.sleep(rand.nextInt(1000,10000)); } catch (InterruptedException e) { break; }

                } catch (IOException e) {
                    System.err.println(Thread.currentThread().getName() + " error: " + e.getMessage());
                }
        }
    }
}
