package buffer ;

import java.util.* ;


public class Buffer implements Runnable {

   private LinkedList<Integer> q ;
   int limit ;

   public boolean keepRunning ;

   public Buffer(int n {
      q = new LinkedList<Integer>() ;
      limit = n ;
   }

   synchronized public boolean isEmpty() {
      return q.isEmpty() ;
   }

   synchronized public boolean isFull() {
      return q.size() >= limit ;
   }

   synchronized public void add(Integer data) {
      if (q.size() >= limit) return ;
      q.addLast(data) ;
   }

   synchronized public Integer remove() {
      if (q.isEmpty()) return null ;
      return q.removeFirst() ;
   }


   public void run() {
      try {

         keepRunning = true ;

         while (keepRunning) {
            Thread.sleep(1) ;
         }
  
      } catch ( InterruptedException e ) {
         System.out.println(e.getMessage()) ;
      }
   }

}
