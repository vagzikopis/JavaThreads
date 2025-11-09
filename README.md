# JavaThreads

This repository contains several small Java examples and exercises focused on threads, concurrency primitives, and socket examples.

### subtask_1
- `MatrixVectorMult.java`: Java implementation for matrix-vector multiplication. Used for performance/parallelization experiments.

### subtask_2
Small demo simulating the operation of health system with multiple threads used for healing and spreading a disease:
- `Disease.java`: Disease threads that spreads the virues.
- `HealthSystem.java`: Orchestrates simulated health system logic.
- `Hospital.java`: Represents a hospital thread responsible for patient healing.
- `Log.java`: Simple logging helper for simulation events.
- `Orchestrator.java`: Entry point / controller for the simulation.
- `Patient.java`: Represents a patient in the simulation.

### subtask_3
Simple multithreaded server and client communication:
- `Client.java`: Client-side of a small networked example.
- `Server.java`: Server-side counterpart.
- `Orchestrator.java`: Coordinator of the demo.

### subtask_4
Simple client-server communication, with multiple threads attempting to increase or decrease a common variable in the server:
- `Producer.java` / `Consumer.java`: Producer and consumer components for the task.
- `Server.java`: Server/runner for the system. Maintains the variable accessed and modified by Producer/Consumer.
- `Log.java`: Logging helper.
- `Orchestrator.java`: Main coordinator of the demo.
