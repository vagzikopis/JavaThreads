package subtask_2;

import java.util.Random;
import java.util.UUID;

public class Patient {
   public final UUID uuid;
   public final String name;
   private static final String[] FIRST_NAMES = {
         "John", "Jane", "Alice", "Bob", "Eve", "Tom", "Emma", "Liam", "Olivia", "Noah",
         "Costas", "Yiorgos", "Eleni", "Maria", "Vangelis", "Nikos", "Orestis", "Zoe", "Thanasis", "Manos"
   };
   private static final Random rand = new Random();

   public Patient() {
      this.uuid = UUID.randomUUID();
      this.name = generateRandomUniqueName();
   }

   private static String generateRandomUniqueName() {
      return FIRST_NAMES[rand.nextInt(FIRST_NAMES.length)];
   }

}
