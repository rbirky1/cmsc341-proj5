package project5;

import java.util.* ;


public class Buffer implements Runnable {

   private LinkedList<Integer> buff;
   int limit;
   public boolean keepRunning;


   public Buffer(int n) {
      buff = new LinkedList<Integer>();
      limit = 2*n*n;
   }

   synchronized public boolean isEmpty() {
      return buff.isEmpty() ;
   }

   synchronized public boolean isFull() {
      return buff.size() >= limit ;
   }

   synchronized public void add(Integer data) {
      if (buff.size() >= limit) return;
      buff.addLast(data);
   }

   synchronized public Integer remove() {
      if (buff.isEmpty()) return null;
      return buff.removeFirst();
   }

   synchronized public void waitForData() {
      try {
         while (buff.size() < (n*n)) wait()  ;
      } catch (InterruptedException e) {
         System.out.println(e.getMessage()) ;      
      }
   }

   synchronized public void waitForSpace() {
      try {
         while (q.size() >= limit) wait() ;
      } catch (InterruptedException e) {
         System.out.println(e.getMessage()) ;      
      }
   }

   public void run() {
      try {
         keepRunning = true;

         while (keepRunning) {
           //Do nothing
         }
  
      } catch (InterruptedException e) {
         System.out.println(e.getMessage()) ;
      }
   }

}
