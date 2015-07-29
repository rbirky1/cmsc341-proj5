package buffer ;

import java.util.* ;
import javax.swing.*;
import java.awt.Font ;


public class Consumer implements Runnable {

   Buffer buf ;         // reference to FIFO queue
   int xpos = 0 ;       // window position
   int ypos = 0 ;       // window position

   public Consumer(Buffer b, int x, int y) {
      buf = b ;
      xpos = x ;
      ypos = y ;
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

         // wait a while
         Thread.sleep(5000) ;

         // retrieve some data from buffer
         data = buf.remove() ;
         label.setText("Got " + data) ;
         Thread.sleep(1000) ;

         data = buf.remove() ;
         label.setText("Got " + data) ;
         Thread.sleep(3000) ;

         label.setText("Done.") ;

  
      } catch ( InterruptedException e ) {
         System.out.println("Oh look! an exception!") ;
      }
   }

}
