
package project5;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
	int THRESHOLD;

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

	public static void merge(int[] left, int[] right, int[] original) {
		int i1 = 0;   // index into left array
		int i2 = 0;   // index into right array

		for (int i = 0; i < original.length; i++) {
			if (i2 >= right.length || (i1 < left.length && left[i1] <= right[i2])) {
				original[i] = left[i1];    // take from left
				i1++;
			} else {
				original[i] = right[i2];   // take from right
				i2++;
			}
		}
	}

	//NEW EDITED
	public void mergeSort(){
		if(buff2.length<=THRESHOLD){
			insertionSort(buff2); return;
		}
		threadPool.invoke(new MergeSort(buff2, THRESHOLD));
	}

	@SuppressWarnings("serial")
	class MergeSort extends RecursiveAction {

		protected int THRESHOLD;
		int[] array;

		/**
		 * <p> Constructor
		 * <p> Description: creates a new mergesort task object
		 * @param array - data to be processed
		 * @param first - first index of current array
		 * @param last - end index of current array
		 * @param T - threshold for task recursion
		 */
		public MergeSort(int[] array, int T) {
			this.THRESHOLD = T;
			this.array = array;
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.RecursiveAction#compute()
		 */
		protected void compute() {
			if (array.length <= this.THRESHOLD)
				//Compute directly
				insertionSort(array);
			else {
				//Split Task
				int center = (this.array.length)/2;
				int[] leftArray = Arrays.copyOfRange(array, 0, center);
				int[] rightArray = Arrays.copyOfRange(array, center, array.length);
				MergeSort sortLeft = new MergeSort(rightArray, this.THRESHOLD); //sort right
				MergeSort sortRight = new MergeSort(leftArray, this.THRESHOLD); //sort left
				invokeAll(sortLeft, sortRight);
				merge(leftArray, rightArray, array);
			}
		}
	}


	/**
	 * <p> Method: insertionSort()
	 * <p> Description: Sorts by inserting the item at the current index
	 * 		so that all items before it are lesser in value
	 * @param subArray - list to be sorted
	 */
	public void insertionSort(int[] subArray){
		for (int i=1; i<subArray.length; i++){
			int toCompare = subArray[i];
			int j = i;
			while (j>0 && (subArray[j-1] > toCompare)){
				subArray[j] = subArray[j-1];
				j--;
			}
			subArray[j] = toCompare;
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

	//Debug method
	public void printBuffScaled(){
		for(int i : buff2Scaled){
			System.out.print(i+" ");
		}
		System.out.println();
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
			String filename = String.format("./images/output_image_N%d_T%d.jpg", N, THRESHOLD);
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
		this.mergeSort();
		long t2 = System.currentTimeMillis();
		System.out.println("MergeSort time: "+(t2-t1)+"ms");
		normalize();
		saveImage();
	}

	//Unit Testing
//	public static void main(String[] args){
//		int[] l = new int[10000];
//		int n = 100;
//		int t = 4;
//		Random generator = new Random();
//		for (int i=0; i<10000; i++){
//			l[i]= generator.nextInt(4097);
//		}
//		Processor p = new Processor(l,n,t);
//		p.printBuff();
//		long t1 = System.currentTimeMillis();
//		p.mergeSort();
//		long t2 = System.currentTimeMillis();
//		System.out.println("MergeSort time: " +(t2-t1)+"ms");
//		p.printBuff();
//		p.normalize();
//		p.printBuffScaled();
//		p.saveImage();
//	}
}