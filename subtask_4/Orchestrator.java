package subtask_4;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Orchestrator {

   public static void main(String[] args) throws IOException{
      List<Integer> consumer_ports = Arrays.asList(8881, 8882, 8883);
      List<Integer> producer_ports = Arrays.asList(9881, 9882, 9883);

      // Spawn servers
      int servers = Math.min(producer_ports.size(), consumer_ports.size());
      for (int i = 0; i < servers; i++) {
         final int idx = i;
         Server server = new Server(producer_ports.get(idx), consumer_ports.get(idx));
         Thread s = new Thread(() -> {
            try {
               server.start();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }, "Server-" + (idx + 1));
         s.start();
      }

      System.out.println("Waiting servers to start");
      try { Thread.sleep(5000); } catch (InterruptedException ignored) {}

      // Spawn producers and consumers
      int producerCount = 10;
      int consumerCount = 10;

      for (int i = 0; i < producerCount; i++) {
         final int idx = i;
         Thread p_thread = new Thread(() -> {
            Producer p = new Producer("127.0.0.1", producer_ports);
            p.startProducer();
         }, "Producer-" + (idx + 1));
         p_thread.start();
      }

      for (int i = 0; i < consumerCount; i++) {
         final int idx = i;
         Thread c_thread = new Thread(() -> {
            Consumer c = new Consumer("127.0.0.1", consumer_ports);
            c.startConsumer();
         }, "Consumer-" + (idx + 1));
         c_thread.start();
      }

   }
}
