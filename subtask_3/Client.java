package subtask_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client extends Thread {
    // Server hostname
    private final String hostname;
    // Server port
    private final int port;
    // Initialize queue for multi-threaded command handling
    private final BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();
    // Regex for user inputs
    private static final Pattern INPUT_PATTERN =
            Pattern.compile("\\((\\d)(?:,(-?\\d+))?(?:,(-?\\d+))?\\)");

    public Client(String hostname, int port, String name) {
        super(name);
        this.hostname = hostname;
        this.port = port;
    }

    public void addCommand(String cmd) {
        // Add new command to queue
        commandQueue.add(cmd);
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
                // Read commands from queue
                String input = commandQueue.take();

                // Validation check
                if (!inputValidation(input)) {
                    System.out.println(getName() + " Invalid input: " + input);
                    continue;
                }

                // Communicate with server
                out.println(input);
                String response = in.readLine();
                System.out.println(getName() + " - Server Response: " + response);
                System.out.print("> ");

                if (input.equals("(0,0)")) break;
            }

        } catch (IOException | InterruptedException e) {
            System.err.println(getName() + " error: " + e.getMessage());
        }
    }

    private boolean inputValidation(String input) {
        Matcher matcher = INPUT_PATTERN.matcher(input.trim());
        if (!matcher.matches()) return false;

        int a = Integer.parseInt(matcher.group(1));
        String b = matcher.group(2);
        String c = matcher.group(3);

        return switch (a) {
            case 1 -> b != null && c != null;
            case 0, 2, 3 -> b != null && c == null;
            default -> false;
        };
    }
}
