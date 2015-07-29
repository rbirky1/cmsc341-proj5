
package project5;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import javax.imageio.ImageIO;

/**
 * @author Rachael Birky
 * @version 05.09.2014
 * 
 * <p> Processor.java
 * <p> This class processes data using threads and ForkJoin
 * 		It 1. sorts 2. normalizes 3. saves an image
 */
public class Processor implements Runnable{

	ForkJoinPool threadPool = new ForkJoinPool();

	int[] buff2;
	int[] buff2Scaled;
	int N;
	protected int THRESHOLD;

	/**
	 * <p> Constructor
	 * <p> Description: creates a new processing object thread
	 * 		that can sort, normalize and save graphic representations of data
	 * @param aBuffer - integer array of data to be processed
	 * @param N - current size
	 * @param - T threshold
	 */
	public Processor(int[] aBuffer, int N, int T){
		buff2 = aBuffer;
		buff2Scaled = new int[buff2.length];
		this.N = N;
		this.THRESHOLD = T;
	}

	//Debug method
	public void printBuff(){
		for(int i : buff2){
			System.out.print(i+" ");
		}
		System.out.println();
	}

	//Debug method
	public void printBuffScaled(){
		for(int i : buff2Scaled){
			System.out.print(i+" ");
		}
		System.out.println();
	}
	
	/**
	* <p> Method: mergeSort()
	* <p> Description: Boot-strap method that gives data to
	* 		mergesort that handles task recursion object creation
	*/
	public void mergeSortRec(){
		mergeSortRec(buff2, 0, buff2.length-1);
	}

	/**
	* <p> Method: mergeSort
	* <p> Description: Given data, creates and invokes new objects
	* 		to manage task recursion
	* @param array - data to be processed
	* @param first - first index
	* @param last - last index
	*/
	public void mergeSortRec(int[] array, int first, int last) {
		if (last-first < THRESHOLD) {
			MergeSort.insertionSort(array, first, last);
			return;
		}
		int[] temp = new int[array.length];
		threadPool.invoke(new SortTask(array, temp, first, last, this.THRESHOLD));
	}
	@SuppressWarnings("serial")
	public class SortTask extends RecursiveAction {

		protected int THRESHOLD;
		int[] array;
		int[] temp;
		int start, end;

		/**
		 * <p> Constructor
		 * <p> Description: creates a new mergesort task object
		 * @param array - data to be processed
		 * @param temp - blank array of same size
		 * @param start - start index
		 * @param end - end index
		 */
		public SortTask(int[] array, int[] temp, int start, int end, int thresh) {
			this.array = array;
			this.temp = temp;
			this.start = start;
			this.end = end;
			this.THRESHOLD = thresh;
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.RecursiveAction#compute()
		 */
		protected void compute() {
			if (end-start < THRESHOLD)
				MergeSort.insertionSort(array, start, end);
			else {
				int mid = (start+end)/2;
				SortTask task1 = new SortTask(array, temp, start, mid, THRESHOLD);
				SortTask task2 = new SortTask(array, temp, mid+1, end, THRESHOLD);
				invokeAll(task1, task2);
				MergeSort.merge(array, temp, start, mid, end);
			}
		}
	}

	/**
	 * @author Rachael Birky
	 * @version 05.13.14
	 * 
	 * <p> MergeSort.java
	 * <p> This class has methods insertionSort and merge
	 */
	public static class MergeSort{
		
		/**
		 * <p> Method: insertionSort()
		 * <p> Description: Sorts by inserting the item at the current index
		 * 		so that all items before it are lesser in value
		 * @param array - list to be sorted
		 * @param first - index of first element
		 * @param last - index of last element
		 */
		public static void insertionSort(int[] array, int first, int last){
			for (int i=first; i<=last; i++){
				int toCompare = array[i];
				int j = i;
				while (j>first && (array[j-1] > toCompare)){
					array[j] = array[j-1];
					j--;
				}
				array[j] = toCompare;
			}
		}
		
		/**
		 * <p> Method: merge()  
		 * <p> Description: Combines two sorted lists into one
		 * @param array - array of data
		 * @param first - beginning index of data
		 * @param mid - size of first "array"
		 * @param last - size of second "array" 
		 */
		public static void merge(int[] array, int[]temp, int first, int mid, int last){
			int li = first;
			int ri = mid+1;
			
			for(int j=0; j<=last; j++){
				temp[j] = array[j];
			}
			
			for (int i=first; i<=last; i++) {
	            if(li>mid){
	            	array[i] = temp[ri];
	            	ri++;
	            }
	            else if (ri>last){
	            	array[i] = temp[li];
	            	li++;
	            }
	            else if (temp[ri]<temp[i]){
	            	array[i] = temp[ri];
	            	ri++;
	            }
	            else{
	            	array[i] = temp[li];
	            	li++;
	            }
			}
		}
	}


	/**
	 * <p> Method: normalize()
	 * <p> Description: Adjusts the range of data to the range 0-255
	 */
	public void normalize(){
		int i=0;
		for(Integer x : buff2){
			int min = 0;
			int max = 4096;
			int a = 0;
			int b = 255;
			int newX = ((((b-a)*(x-min))/(max-min))+a);
			buff2Scaled[i] = newX;
			i++;
		}		
	}

	/**
	 * <p> Method: saveImage()
	 * <p> Description: Saves a gradient image representing the sorted
	 * 		and normalized data to the ./images folder
	 */
	public void saveImage(){
		int width = N;
		int height = N;

		if (buff2Scaled.length != N*N){
			System.out.println("ERROR");
			return;
		}

		try{
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			int index = 0;
			int value = 0;
			while (index<buff2Scaled.length) {
				value = buff2Scaled[index];
				bufferedImage.setRGB((int)index%width, (int)index/width, (value << 16) + (value << 8) + value);
				index++;
			}
			String truefilename = String.format("/afs/umbc.edu/users/s/l/slupoli/pub/cs341/rbirky1/proj5/images/output_image_N%d_T%d.jpg", N, THRESHOLD);
			String filename = String.format("./images/output_image2_N%d_T%d.jpg", N, THRESHOLD);
			System.out.println("Saving image: " + filename);
			ImageIO.write(bufferedImage, "jpg", new File(truefilename));
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		long t1 = System.currentTimeMillis();
		this.mergeSortRec();
		long t2 = System.currentTimeMillis();
		System.out.println("MergeSort time: "+(t2-t1)+"ms");
		normalize();
		//saveImage();
	}

	//Unit Testing
	public static void main(String[] args){
		Random generator = new Random();
		int[] ints = new int[14];
		for(int i=0; i<ints.length; i++){
			ints[i] = generator.nextInt(15);
		}
		Processor p = new Processor(ints, 4, 5);
		p.mergeSortRec();
		p.printBuff();
	}
}