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
         int n = 3 ;

         while ( keepRunning ) {

            buf.add(n) ;
            label.setText("Added " + n) ;
            Thread.sleep(500) ;
            n++ ;
            if (n > 99) n = 3 ;
         }

  
      } catch ( InterruptedException e ) {
         System.out.println("Oh look! an exception!") ;
      }
   }

}
