package project5;

public class Project5 {

   public static void main (String [] args) {

      Buffer buff = new Buffer(n);
      Thread buffThread = new Thread(buff);
      buffThread.start();

      Satellite sat = new Satellite(buff, n);
      Thread satThread = new Thread(sat) ;
      satThread.start() ;


      // Set up consuer thread
      Receiver rec = new Receiver(buff, n);
      Thread recThread = new Thread(rec) ;
      recThread.start() ;


      try {

         // tell threads to stop
         buff.keepRunning = false ;
         sat.keepRunning = false ;
         rec.keepRunning = false ;

      } catch (Exception e ) {
         System.out.println(e.getMessage()) ;
      }

   }

}
