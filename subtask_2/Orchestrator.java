package subtask_2;

import java.util.ArrayList;
import java.util.List;

public class Orchestrator {
    // Healing Limit
    public static int H=12;
    // Contagion Limit
    public static int K=30;
    // Total beds
    public static int E=20;
    // Total Iterations
    public static int ITERATIONS=150;
    // Sleeps
    public static int disease_sleep_ms=100;
    public static int hospital_sleep_ms=150;
    public static int number_of_hospitals = 10;


    public static void main(String[] args) throws InterruptedException {
        // Initialize logger
        String logFile = String.format("subtask_2/hospital_%d.log", number_of_hospitals);
        Log.open(logFile);

        // Initialize health system
        HealthSystem system = new HealthSystem(E);

        // Initialize disase thread
        Disease spread_disease = new Disease(system, K, ITERATIONS, disease_sleep_ms);
        spread_disease.setName("Thread-Disease");

        // Initialize multiple hospital threads
        List<Hospital> hospitals = new ArrayList<>(number_of_hospitals);
        for (int i=0; i<number_of_hospitals; i++) {
            Hospital h = new Hospital(system, H, ITERATIONS, hospital_sleep_ms);
            h.setName("Thread-Hospital-" + i);
            hospitals.add(h);
        }

        // Start disease thread
        spread_disease.start();

        // Minor delay of threads for startup
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted during startup delay.");
        }

        // Start hospital threads
        for (Hospital h : hospitals) {
            h.start();
        }

        // Wait thread completion
        spread_disease.join();

        // Wait hospital threads
        for (Hospital h : hospitals) {
            h.join();
        }

        // Close logger
        Log.close();
    }
}
