package project5;

import java.util.* ;


public class Receiver implements Runnable {

   Buffer buff;
   int n;
   public boolean keepRunning;
   private LinkedList<Integer> buff2 = new LinkedList<Integer>();

   public Receiver(Buffer b, int n) {
      buff = b ;
      this.n = n;
   }

   //This will become the merge
   private void mergeSort() {

   }

   public void run() {
      try {

         Integer data;

         keepRunning = true;

         while (keepRunning) {
            buff.waitForData();
            for(int i=0; i< (n*n); i++){
               data = buff.remove();
               buff2.add(data);
            }

         }
  
      } catch (Exception e) {
         System.out.println(e.getMessage()) ;
      }
   }

}