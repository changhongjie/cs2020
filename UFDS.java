package sg.edu.nus.cs2020;

public class UFDS {
	private int[] parent;
	private int[] rank;
	private int setTotal;
	
	// Initialisation given to you for free. Woo hoo! :)
	public UFDS(int N) {
		parent = new int[N];
		rank = new int[N];
		setTotal = N;
		
		for (int i = 0; i < N; i++) {
			parent[i] = i;
			rank[i] = 0;
		}
	}
	
	/**
	 * Returns the set identifier for element i
	 * @param i
	 * @return
	 */
	public int findSet(int i) {
		if (parent[i] == i) {
			return i;
			
		} else {
			int root = findSet(parent[i]);
			parent[i] = root;
			return root;
		}
	}
	
	/**
	 * Returns whether element i and j are from the same set
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isSameSet(int i, int j) {
		return findSet(i) == findSet(j);
	}
	
	/**
	 * Unions elements i and j
	 * @param i
	 * @param j
	 */
	public void unionSet(int i, int j) {
		int iRoot = findSet(i);
		int jRoot = findSet(j);
		
		if (iRoot == jRoot) {
			return;
			
		} else if (rank[iRoot] <= rank[jRoot]) {
			parent[iRoot] = jRoot;
			rank[jRoot] += rank[iRoot];
			
		} else {
			parent[jRoot] = iRoot;
			rank[iRoot] += rank[jRoot];
		}
		setTotal -= 1;
	}
	
	/**
	 * Returns number of elements in set i
	 * @param i
	 * @return
	 */
	public int setSize(int i) {
		// Your code here
		// One easy way: Look at every element and see how many belong to set i
		// This runs in O(N)
		
		// Push yourself harder (Bonus):
		// Can we keep track of something extra *cheaply* in UFDS to perform this in O(1)?
		// Reminder: Don't forget to maintain it as you perform the other operations
		return rank[findSet(i)];
	}
	
	/**
	 * Returns number of sets
	 * @return
	 */
	public int numSet() {
		// Your code here
		// One easy way: Look at every element and see how many distinct sets are there
		// This runs in at least O(N)

		// Push yourself harder (Bonus):
		// Can we keep track of something extra *cheaply* in UFDS to perform
		// this in O(1)?
		// Reminder: Don't forget to maintain it as you perform the other operations
		return setTotal;
	}
}
