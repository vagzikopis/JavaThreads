package subtask_2;

import java.util.Random;

public class Disease extends Thread {
    // Max new patients per iteration
    private final int maxNewPatients;
    // Number of total iterations
    private final int iterations;
    // Sleep between iterations
    private final int sleep_ms;
    // Random generator
    private final Random rand = new Random();
    // Health system
    private HealthSystem system;

    public Disease(HealthSystem system, int maxNewPatients, int iterations, int sleep_ms) {
        this.maxNewPatients = maxNewPatients;
        this.iterations = iterations;
        this.system = system;
        this.sleep_ms = sleep_ms;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            int newPatients = rand.nextInt(maxNewPatients);
            // Insert new patients to the system
            for (int j = 0; j < newPatients; j++) {
                this.system.insert(new Patient());
            }

            try { Thread.sleep(sleep_ms); } catch (InterruptedException e) { break; }
        }
    }
}
