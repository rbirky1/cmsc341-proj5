package project5;

import java.util.Arrays;

public class MergeSort implements Runnable{

	int T;
	int[] buff2;
	public boolean keepRunning;

	public MergeSort(int[] aBuffer, int T){
		this.T = T;
		buff2 = aBuffer;
	}

	synchronized public void mergeSort(int [] array){
		if(array.length>2){
			int center = array.length/2;
			int[] left = Arrays.copyOfRange(array, 0, center);
			int[] right = Arrays.copyOfRange(array, center, array.length);
			
			System.out.println();
			System.out.println("Left List");
			for (int i = 0; i<left.length; i++){
				System.out.print(left[i]+" ");
			}
			System.out.println();
			System.out.println("Right List");
			for (int i = 0; i<right.length; i++){
				System.out.print(right[i]+" ");
			}

			//Call recursively unless at Threshold for Insertion Sort
			if(left.length > T){
				//mergeSort(left);
				Thread mergeL = new Thread(new MergeSort(left, T));
				mergeL.start();
			}
			else{
				insertionSort(left);
			}

			if(right.length > T){
				//mergeSort(right);
				Thread mergeR = new Thread(new MergeSort(right, T));
				mergeR.start();
			}
			else{
				insertionSort(right);
			}
			
			try{
				mergeL.join();
				mergeR.join();
			} catch(Exception e){
				System.out.println(e.getMessage());
			}
			
			merge(left, right, array);
		}
	}

	synchronized public void merge(int[] left, int[] right, int[] original){

		int li = 0;
		int ri = 0;
		int i = 0;

		//loop until one list is empty
		while((li<left.length) && (ri<right.length)){
			//choose smaller value from current index of each list
			if(left[li] <= right[ri]){
				original[i] = left[li];
				li++; i++;
			}
			else{
				original[i] = right[ri];
				ri++; i++;
			}
		}
		//if left is longer, fill in the rest
		while(li<=left.length-li){
			original[i] = left[li];
			li++; i++;
		}
		//if right is longer, fill in the rest
		while(ri<=right.length-ri){
			original[i] = right[ri];
			ri++; i++;
		}
	}

	synchronized public void insertionSort(int[] subArray){
		//compare each item
		for (int i = 1; i<subArray.length; i++){
			int toCompare = subArray[i];
			int j = i;
			//to every other other item before it, until all items to left are smaller
			while (j>0 && (subArray[j-1] > toCompare)){
				subArray[j] = subArray[j-1];
				j--;
			}
			subArray[j] = toCompare;
		}
	}

	public void run() {
		
		try {
			keepRunning = true;
			
			while(keepRunning){
				this.mergeSort(buff2);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
