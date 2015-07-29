/* File: Main.java
   
   This class has the main program to get
   the producer-conusumer thing started.
*/

package driver ;

import buffer.Buffer ;
import buffer.Producer ;
import buffer.Consumer ;

public class Main {

   public static void main (String [] args) {


      // Set up thread to display the buffer
      Buffer buf ;

      // Specify buffer size and window position
      buf = new Buffer(10,0,500) ;

      Thread bufThread = new Thread(buf) ;
      bufThread.start() ;


      // Set up producer thread
      Producer prod ;

      // Pass on buffer reference. Set window position
      prod = new Producer(buf, 450, 0) ;
      Thread prodThread = new Thread(prod) ;
      prodThread.start() ;


      // Set up consuer thread
      Consumer cons ;

      // Pass on buffer reference. Set window position
      cons = new Consumer(buf, 0, 0) ;
      Thread consThread = new Thread(cons) ;
      consThread.start() ;


      try {

         // Wait for producer and consumer to finish
         prodThread.join() ;
         consThread.join() ;

         // tell buffer display to stop
         buf.keepRunning = false ;
         Thread.sleep(5000) ;

      } catch ( InterruptedException e ) {
         System.out.println("Oh look! an exception!") ;
      }

      // need to call exit() to close down Swing windows.
      System.exit(0) ;
   }

}
