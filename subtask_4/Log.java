package subtask_4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Logger class
public class Log {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static PrintWriter writer;

    public static synchronized void open(String filename) {
        try {
            writer = new PrintWriter(new FileWriter(filename, true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String thread = Thread.currentThread().getName();
        String log = String.format("[%s] [%s] %s%n", timestamp, thread, message);
        String fileLog = String.format("%s,%s,'%s'%n", timestamp, thread, message);

        System.out.printf(log);
        if (writer != null) {
            writer.printf(fileLog);
            writer.flush();
        }
    }

    public static void close() {
        if (writer != null) writer.close();
    }
}
