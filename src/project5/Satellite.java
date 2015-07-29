package project5;

import java.util.* ;


/**
 * @author Rachael Birky
 * @version 05.09.2014
 * 
 * <p> Satellite.java
 * <p> This class represents a satellite that generates data
 * 		and adds it to a shared buffer
 */
public class Satellite implements Runnable {

	Buffer buff;
	int n;

	public boolean keepRunning;

	boolean debug = false;

	/**
	* <p> Constructor
	* <p> Description: Creates a new Satellite object with a
	* 		reference to a shared buffer and a number used to 
	* 		calculate the 
	*/
	public Satellite(Buffer b, int n) {
		buff = b;
		this.n = n;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			keepRunning = true;
			int data;
			Random generator = new Random(System.currentTimeMillis());
			while(keepRunning){
				data = generator.nextInt(4097);
				buff.waitForSpace();
				buff.add(data) ;
			}
		} catch (Exception e ) {
			System.out.println(e.getMessage()) ;
		}
	}

}
