package project5;

/**
 * @author Rachael Birky
 * @version 05.11.2014
 * 
 * <p> Project5.java
 * <p> This class simulates generation, collection and processing
 * 		of data like a satellite while implementing threads and task
 * 		recursion to collect, sort, normalize, and save data
 */
public class Project5 {

	public static void main (String [] args) {

		int runNum = 1;
		System.out.println("Available processors (cores): "+Runtime.getRuntime().availableProcessors());
		System.out.println("Available memory (bytes): "+Runtime.getRuntime().freeMemory());
		System.out.println();


		for(int i=8; i<9; i++){
			//for(int i=8; i<12; i++){
			int n = (int)Math.pow(2, i);

			Buffer buff = new Buffer(n);
			Thread buffThread = new Thread(buff);
			int buff1Size = buff.getBuffSize();
			buffThread.start();

			Satellite sat = new Satellite(buff, n);
			Thread satThread = new Thread(sat);
			satThread.start();

			for(int j=1; j<2; j++){
				//for(int j=1; j<6; j++){
				int t = (int) Math.pow(10, j);

				Receiver rec = new Receiver(buff, n, t);
				int buff2Size = rec.getBuffSize();
				System.out.printf("\nRun #%d, i=%d, j=%d, N=%d, B1=%d, B2=%d, T=%d\n", runNum, i, j, n, buff1Size, buff2Size, t);
				Thread recThread = new Thread(rec);
				recThread.start();

				try{
					recThread.join();
					rec.keepRunning = false;
				}
				catch(InterruptedException e){
					System.out.println(e.getMessage());
				}

				runNum++;

			}

			try{
				satThread.join();
				buffThread.join();
				sat.keepRunning = false;
				buff.keepRunning = false;
			}
			catch(InterruptedException e){
				System.out.println(e.getMessage());
			}

		}


	}

}
