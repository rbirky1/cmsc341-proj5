package buffer ;

import java.util.* ;
import javax.swing.*;
import java.awt.Font ;


public class Consumer implements Runnable {

   Buffer buf ;         // reference to FIFO queue
   int xpos = 0 ;       // window position
   int ypos = 0 ;       // window position
   
   public boolean keepRunning ;  // so parent thread can say stop

   public Consumer(Buffer b, int x, int y) {
      buf = b ;
      xpos = x ;
      ypos = y ;
   }


   // Check if a number is prime the slow way.
   //
   private boolean isPrime(int n) {
      if ( n <= 1 ) return false ;

      int sqr = (int) Math.sqrt(n) ;
      int t = 2 ;

      while (t <= sqr) {
         if (n % t == 0) return false ;
         t++ ;
      }

      return true ;
   }


   public void run() {
      try {

         // Set up Swing window as usual
         //
         JFrame frame = new JFrame( "The Consumer");
         JLabel label = new JLabel("Hello Java!", JLabel.CENTER );
         label.setVerticalTextPosition(JLabel.TOP);
         label.setFont( new Font("Serif", Font.BOLD, 24) ) ;
         frame.getContentPane().add( label );
         frame.setSize( 300, 300 );
         frame.setLocation( xpos, ypos );
         frame.setVisible( true );

         Integer data ;

         keepRunning = true ;

         while (keepRunning) {

            // poll the buffer
            while ( buf.isEmpty() ) {
               label.setText("Empty Buffer!") ;
               Thread.sleep(200) ;
            }
             
            data = buf.remove() ;
            label.setText("Got " + data) ;
            Thread.sleep(1000) ;

            if ( isPrime(data) ) {
               label.setText("Yes, " + data + " is prime") ;
            } else {
               label.setText("No, " + data + " is not prime") ;
            }

            // wait a while
            Thread.sleep(800) ;
         }

         label.setText("Done.") ;

  
      } catch ( InterruptedException e ) {
         System.out.println("Oh look! an exception!") ;
      }
   }

}
