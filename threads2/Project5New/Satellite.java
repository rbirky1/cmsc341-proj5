package project5;

import java.util.* ;


public class Satellite implements Runnable {

   Buffer buff;
   int n;

   public boolean keepRunning;

   public Satellite(Buffer b, int n) {
      buff = b;
      this.n = n;
   }


   public void run() {
      try {

         keepRunning = true ;
         while(keepRunning){}
         Random generator = new Random();
         int n = generator.nextInt(4097);
         buff.add(n) ;
      } catch (Exception e ) {
         System.out.println(e.getMessage()) ;
      }
   }

}
