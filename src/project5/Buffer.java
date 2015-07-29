package project5;

import java.util.* ;

/**
 * @author Rachael Birky
 * @version 05.09.2014
 * 
 * <p> Buffer.java
 * <p> This class represents a buffer (storage space) with a size limit 
 */
public class Buffer implements Runnable {

	private LinkedList<Integer> buff;
	int n;
	int limit;
	public boolean keepRunning;

	boolean debug = false;

	/**
	 * <p> Constructor
	 * <p> Description: Creates a new Buffer object with a limit of 2N^2
	 * @param n - the current value
	 */
	public Buffer(int n) {
		buff = new LinkedList<Integer>();
		this.n = n;
		limit = 2*n*n;
	}

	/**
	 * <p> Method: isEmpty()
	 * <p> Description: Checks if there are any items in the buffer
	 * @return true - no items in buffer
	 */
	synchronized public boolean isEmpty() {
		return buff.isEmpty() ;
	}

	/**
	* <p> Method: isFull()
	* <p> Description: Checks if the buffer has reached the size limit
	* @return true - there are 2N^2 items in the buffer
	*/
	synchronized public boolean isFull() {
		return buff.size() >= limit ;
	}

	/**
	* <p> Method: add
	* <p> Description: Adds an item to the buffer, if there is room;
	* 		The buffer notifies the calling object when there is room
	*/
	synchronized public void add(Integer data) {
		if (buff.size() >= limit) return;
		if (debug) System.out.println("Adding "+data);
		buff.addLast(data);
		notify();
	}

	/**
	* <p> Method: remove()  
	* <p> Description: Removes the first item form the buffer if there are any
	* @return buff.removeFirst()
	*/
	synchronized public Integer remove() {
		if (buff.isEmpty()) return null;
		notify();
		return buff.removeFirst();
	}

	/**
	* <p> Method: waitForData()
	* <p> Description: Waits until the buffer has N*N items
	*/
	synchronized public void waitForData() {
		try {
			while (buff.size() < (n*n)) wait();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());      
		}
	}

	/**
	* <p> Method: waitForSpace()
	* <p> Description: Waits until there is space in the buffer
	*/
	synchronized public void waitForSpace() {
		try {
			while (buff.size() >= limit) wait();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());      
		}
	}

	/**
	* <p> Method: getBuffSize  
	* <p> Description: Returns the current number of item in the buffer
	* @return buff.size()
	*/
	synchronized public int getBuffSize(){
		return limit;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			keepRunning = true;

			while (keepRunning) {
				//Do nothing
				Thread.sleep(1);
			}

		} catch (InterruptedException e) {
			System.out.println(e.getMessage()) ;
		}
	}

}