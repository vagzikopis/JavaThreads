package subtask_2;

import java.util.Random;

public class Hospital extends Thread {
    private HealthSystem system;
    private final int maxHeal;
    private final int iterations;
    private final int sleep_ms;
    private final Random rand = new Random();

    public Hospital(HealthSystem system, int maxHeal, int iterations, int sleep_ms) {
        this.system = system;
        this.maxHeal = maxHeal;
        this.iterations = iterations;
        this.sleep_ms = sleep_ms;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            int healedPatients = rand.nextInt(maxHeal);
            // Remove patients from system
            for (int j=0; j<healedPatients; j++) {
                this.system.remove();
            }

            try { Thread.sleep(sleep_ms); } catch (InterruptedException e) { break; }
        }

    }
}
