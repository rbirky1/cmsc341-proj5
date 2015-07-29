package project5;

import java.util.* ;


/**
 * @author Rachael Birky
 * @version 05.09.2014
 * 
 * <p> Receiver.java
 * <p> This class represents a machine that collects data fom a satellite
 * 		through a shared buffer 
 */
public class Receiver implements Runnable {

	Buffer buff;
	int n;
	int t;
	int dataSize;
	public boolean keepRunning;
	private LinkedList<Integer> buff2 = new LinkedList<Integer>();

	boolean debug = false;

	/**
	* <p> Constructor
	* <p> Description: Creates a new receiver given a shared buffer,
	* 		and numbers used to calculate data and threshold sizes
	* @param b - a shared buffer
	* @param n - number used to calculate data size
	* @param t - the threshold used during processing
	*/
	public Receiver(Buffer b, int n, int t) {
		buff = b ;
		this.n = n;
		this.t = t;
		this.dataSize = n*n;
	}

	/**
	 * <p> Method: getBuffSize()
	 * <p> Description: Returns the size of this object's new temporary buffer
	 * @return dataSize - size of temporary buffer
	 */
	synchronized public int getBuffSize(){
		return dataSize;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {

			Integer data;
			keepRunning = true;

			while (keepRunning) {
				buff.waitForData();

				for(int i=0; i< dataSize; i++){
					data = buff.remove();
					if (data!=null) {
						buff2.add(data);
						if (debug) System.out.println("Buff2 size "+buff2.size());
					}
				}

				if (debug) System.out.println("Buff2 size "+buff2.size());
				//Make LinkedList<Integer> into int[]
				int[] buff2Primitive = new int[buff2.size()];
				for (int i=0; i<buff2Primitive.length; i++){
					buff2Primitive[i] = (int) buff2.get(i);
					if (debug) System.out.println(buff2Primitive[i]);
				}

				//Make processor "thread"
				Processor p = new Processor(buff2Primitive, n, t);
				Thread pThread = new Thread(p);
				pThread.start();

				try{
					pThread.join();
				}
				catch(InterruptedException e){
					System.out.println(e.getMessage());
				}
				
				keepRunning=false;
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage()) ;
		}
	}

}