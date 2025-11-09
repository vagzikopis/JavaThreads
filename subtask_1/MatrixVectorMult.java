package subtask_1;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MatrixVectorMult {

   // Experiment Parameters
   public static int MIN_N = 50000;
   public static int MAX_N = 200000;
   public static int N_STEP = 25000;

   public static int MIN_M = 1000;
   public static int MAX_M = 2000;
   public static int M_STEP = 1000;

   public static int MAX_VAL = 10;
   public static int[] THREADS = {1, 2, 4, 6, 8};

   public static int N_EXPERIMENTS = 3;

   // Worker thread class
   static class Worker extends Thread {
      private final int[][] A;
      private final int[] v;
      private final int[] result;
      private final int startRow;
      private final int endRow;

      public Worker(int[][] A, int[] v, int[] result, int startRow, int endRow) {
         this.A = A;
         this.v = v;
         this.result = result;
         this.startRow = startRow;
         this.endRow = endRow;
      }

      // Row * Column multiplication function
      @Override
      public void run() {
         for (int i = startRow; i < endRow; i++) {
            int sum = 0;
            for (int j = 0; j < v.length; j++) {
               sum += A[i][j] * v[j];
            }
            result[i] = sum;
         }
      }
   }

   // Multi-threaded multiplication wrapper
   public static int[] multiply(int[][] A, int[] v, int numThreads) throws InterruptedException {
      int n = A.length;
      int m = A[0].length;

      if (v.length != m)
         throw new IllegalArgumentException("Vector length must match matrix columns.");

      int[] result = new int[n];
      numThreads = Math.min(numThreads, n);

      // Initialize an array with threads
      Thread[] threads = new Thread[numThreads];
      int rowsPerThread = n / numThreads;
      int remainder = n % numThreads;
      int start = 0;

      // Assign row ranges to threads
      for (int t = 0; t < numThreads; t++) {
         int end = start + rowsPerThread + (t < remainder ? 1 : 0);
         threads[t] = new Worker(A, v, result, start, end);
         threads[t].start();
         start = end;
      }

      // Wait for all threads
      for (Thread thread : threads) {
         thread.join();
      }

      return result;
   }

   // Random matrix generator
   public static int[][] randomMatrix(int n, int m, int max_val) {
      Random rand = new Random();
      int[][] A = new int[n][m];
      for (int i = 0; i < n; i++)
         for (int j = 0; j < m; j++)
            A[i][j] = rand.nextInt(max_val + 1);
      return A;
   }

   // Random vector generator
   public static int[] randomVector(int m, int max_val) {
      Random rand = new Random();
      int[] v = new int[m];
      for (int i = 0; i < m; i++)
         v[i] = rand.nextInt(max_val + 1);
      return v;
   }

   public static void main(String[] args) throws InterruptedException {

      String outputFile = "./subtask_1/matrix_mult_results.csv";

      try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
         // Write CSV header
         writer.println("experiment,n,m,k,duration_ns,duration_ms");

         for (int experiment = 1; experiment <= N_EXPERIMENTS; experiment++)
               for (int n = MIN_N; n <= MAX_N; n=n+N_STEP)
                  for (int m = MIN_M; m <= MAX_M; m=m+M_STEP)
                     for (int k : THREADS) {

                           if (n <= k)
                              continue;

                           int[][] A = randomMatrix(n, m, MAX_VAL);
                           int[] v = randomVector(m, MAX_VAL);

                           long start = System.nanoTime();
                           int[] result = multiply(A, v, k);
                           long end = System.nanoTime();

                           double duration = end - start;

                           // Write one line per experiment
                           writer.printf("%d,%d,%d,%d,%d,%f%n", experiment, n, m, k, (end - start), (end - start) / 1_000_000.0);
                     }

         System.out.println("All experiments completed. Results written to " + outputFile);

      } catch (IOException e) {
         System.err.println("Error writing to file: " + e.getMessage());
      }
   }
}
