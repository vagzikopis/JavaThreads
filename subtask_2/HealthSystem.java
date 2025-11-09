package subtask_2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HealthSystem {
    // Diseased queue
    public final BlockingQueue<Patient> diseased;
    // Beds queue
    public final BlockingQueue<Patient> beds;

    public HealthSystem(int beds) {
        this.beds = new LinkedBlockingQueue<>(beds);
        this.diseased = new LinkedBlockingQueue<>();
    }

    // Function called by Disease threads to insert new patients to the system
    public void insert(Patient patient) {
        try {
            // Insert new patient to diseased
            this.diseased.put(patient);
            // Move patients to free beds
            int move_to_beds = this.beds.remainingCapacity();
            for (int i = 0; i < move_to_beds; i++) {
                patient = this.diseased.poll();
                if (patient != null) {
                    this.beds.put(patient);
                }
            }
            printDiseased();
            printBeds();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function called by Hospital threads to insert new patients to the system
    public void remove() {
        try {
            // Heal patient
            Patient p = this.beds.poll();
            if (p != null) {
                // Log.info("Healed Patient: " + p.name);
            }
            // Fill beds with patients from diseased queue
            while (this.beds.remainingCapacity() > 0 && !this.diseased.isEmpty()) {
                p = this.diseased.poll();
                if (p == null)
                    break;
                this.beds.put(p);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printDiseased();
        printBeds();
    }

    public void printBeds() {
        StringBuilder sb = new StringBuilder("Beds: [");
        int index = 0;
        for (Patient p : this.beds) {
            sb.append(p.name);
            if (index < this.beds.size() - 1)
                sb.append(", ");
            index++;
        }
        sb.append("]");
        Log.info(sb.toString());
    }

    public void printDiseased() {
        StringBuilder sb = new StringBuilder("Diseased: [");
        int index = 0;
        for (Patient p : this.diseased) {
            sb.append(p.name);
            if (index < this.diseased.size() - 1)
                sb.append(", ");
            index++;
        }
        sb.append("]");
        Log.info(sb.toString());
    }

}
