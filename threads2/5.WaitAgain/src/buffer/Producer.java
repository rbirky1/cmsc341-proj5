package buffer ;

import java.util.* ;
import javax.swing.*;
import java.awt.Font ;


public class Producer implements Runnable {

   Buffer buf ;         // reference to FIFO queue
   int xpos = 0 ;       // window position
   int ypos = 0 ;       // window position

   public boolean keepRunning ;  // so parent thread can say "stop"


   public Producer(Buffer b, int x, int y) {
      buf = b ;
      xpos = x ;
      ypos = y ;
   }


   public void run() {
      try {

          // Set up Swing window as usual
          //
         JFrame frame = new JFrame( "The Producer");
         JLabel label = new JLabel("Hello Java!", JLabel.CENTER );
         label.setVerticalTextPosition(JLabel.TOP);
         label.setFont( new Font("Serif", Font.BOLD, 24) ) ;
         frame.getContentPane().add( label );
         frame.setSize( 300, 300 );
         frame.setLocation( xpos, ypos );
         frame.setVisible( true );


         // add somethings to the buffer 
         keepRunning = true ;
         int n ;
         Random prg = new Random ( System.currentTimeMillis() ) ;

         Thread.sleep(2000) ;

         label.setText("Starting quick phase") ;
         Thread.sleep(1500) ;

         // quick phase
         //
         for (int i = 1 ; i <= 30 ; i++) {
            n = prg.nextInt(100) ; // random number < 100
            buf.waitForSpace() ;
            buf.add(n) ;
            label.setText("#" + i + ": Added " + n) ;
            Thread.sleep(500) ;
         }

         label.setText("Entering slow phase") ;
         Thread.sleep(1500) ;

         // slow phase
         //
         for (int i = 1 ; i <= 30 ; i++) {

            n = prg.nextInt(100) ; // random number < 100
            buf.waitForSpace() ;
            buf.add(n) ;
            label.setText("#" + i + ": Added " + n) ;
            Thread.sleep(3000) ;
         }
  
      } catch ( InterruptedException e ) {
         System.out.println("Oh look! an exception!") ;
      }
   }

}
