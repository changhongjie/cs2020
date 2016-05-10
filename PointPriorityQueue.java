package sg.edu.nus.cs2020;

import java.util.HashMap;

/**
 * Priority Queue implementation for TSPGraph
 * @author Hong Jie
 * Stores the integer points of the TSPMap in an array, and the distance between points is the priority value stored in a HashMap
 */
public class PointPriorityQueue {
	
	// Store priority value of point
	private HashMap<Integer, Double> priority;		
	// Map points to their index in array
	private HashMap<Integer, Integer> indices;		
	// Store points in order of priority
	private int[] arr;								
	// Pointer of last element in array
	private int last;								
	
	// Constructor
	public PointPriorityQueue(int size) {
		priority = new HashMap<>(size);
		indices = new HashMap<>(size);
		arr = new int[size];
		last = -1;
	}
	
	// Insert point and priority into priority queue
	public void insert(int point, double value) {
		if (point > arr.length) {
			throw new IllegalArgumentException("invalid point");
		}
		last++;
		arr[last] = point;
		indices.put(point, last);
		priority.put(point, value);
		bubbleUp(last);
	}
	
	private void bubbleUp(int index) {
		int point = arr[index];
		
		while (priority.get(arr[index]) < priority.get(arr[parent(index)])) {
			swap(index, parent(index));
			index = parent(index);
		}
		indices.put(point, index);
	}
	
	private void bubbleDown(int index) {
		int point = arr[index];

		while ((left(index) <= last && right(index) <= last) && 
				(priority.get(point) > priority.get(arr[left(index)]) || priority.get(point) > priority.get(arr[right(index)]))) {

			if (priority.get(arr[left(index)]) < priority.get(arr[right(index)])) {
				swap(index, left(index));
				index = left(index);

			} else {
				swap(index, right(index));
				index = right(index);
			}
		}
		indices.put(point, index);
	}

	private int parent(int index) {
		return Math.max(0, (index - 1) / 2);
	}
	
	private int left(int index) {
		return 2 * index + 1;
	}
	
	private int right(int index) {
		return 2 * index + 2;
	}
	
	private void swap(int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
		
		indices.put(arr[a], a);
		indices.put(arr[b], b);
	}
	
	// Decrease priority of a point as per Kruskal's algorithm, return true if priority is decreased
	public boolean decreaseKey(int point, double value) {
		int index = indices.get(point);
		boolean flag = false;
		// Decrease if new value is lower
		if (priority.get(point) > value) {
			priority.put(point, value);
			bubbleUp(index);
			flag = true;
		}
		return flag;
	}
	
	// Return point with lowest priority in queue
	public int extractMin() {
		int res = arr[0];
		swap(0, last);
		arr[last] = -1;
		last--;
		bubbleDown(0);
		return res;
	}
	
	// Check if priority queue is empty
	public boolean isEmpty() {
		return last == -1;
	}
	
}
