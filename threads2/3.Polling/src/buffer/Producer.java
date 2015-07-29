package buffer ;

import java.util.* ;


public class Satellite implements Runnable {

   Buffer buf ;
   public boolean keepRunning ;

   public Producer(Buffer b, int x, int y) {
      buf = b ;

   }


   public void run() {
      try {

         keepRunning = true ;
         int n ;
         Random prg = new Random ( System.currentTimeMillis() ) ;

            while (buf.isFull()) {
               Thread.sleep(1) ;
            }

            n = prg.nextInt(4097) ;
            buf.add(n) ;
            Thread.sleep(1) ;
         }
      } catch ( InterruptedException e ) {
         System.out.println(e.getMessage()) ;
      }
   }

}
