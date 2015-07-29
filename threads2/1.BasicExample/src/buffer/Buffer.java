package buffer ;

import java.util.* ;
import javax.swing.*;
import java.awt.Font ;


public class Buffer implements Runnable {

   private LinkedList<Integer> q ;      // use LinkedList for FIFO queue
   int limit ;                          // bound on buffer size

   int xpos = 0 ;                       // window position
   int ypos = 0 ;                       // window poistion

   public boolean keepRunning ;         // so parent thread can say stop


   public Buffer(int n, int x, int y) {
      q = new LinkedList<Integer>() ;
      limit = n ;
      xpos = x ;
      ypos = y ;
   }

   synchronized public boolean isEmpty() {
      return q.isEmpty() ;
   }

   synchronized public boolean isFull() {
      return q.size() >= limit ;
   }

   synchronized public void add(Integer data) {
      if (q.size() >= limit) return ;   // do nothing
      q.addLast(data) ;
   }

   synchronized public Integer remove() {
      if (q.isEmpty()) return null ;   // do nothing
      return q.removeFirst() ;
   }

   // turn FIFO queue contents into formatted string
   //
   synchronized public String toString() {

      ListIterator itr = q.listIterator(0) ;
      String qStr = "" ;

      while ( itr.hasNext() ) {
         qStr += String.format("[%2d]", itr.next()) ;
      }

      for (int i = q.size() ; i < limit ; i++) {
         qStr += "[xx]" ;
      }

      return qStr ;
   }


   // For Thread object to start
   //
   public void run() {
      try {

         // Set up a frame in Swing as usual
         JFrame frame = new JFrame( "The Buffer");
         JLabel label = new JLabel(toString(), JLabel.CENTER );
         label.setVerticalTextPosition(JLabel.TOP);
         label.setFont( new Font("Monospaced", Font.BOLD, 24) ) ;
         frame.getContentPane().add( label );
         frame.setSize( 800, 150 );
         frame.setLocation( xpos, ypos );
         frame.setVisible( true );

         keepRunning = true ;

         // keep showing contents of the FIFO queue until
         // someone says stop.
         //
         while (keepRunning) {
            label.setText( toString() ) ;  // toString() is synched
            Thread.sleep(50) ;
             
         }
  
      } catch ( InterruptedException e ) {
         System.out.println("Oh look! an exception!") ;
      }
   }

}
