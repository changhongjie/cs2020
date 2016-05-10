package sg.edu.nus.cs2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * TSPGraph implementation 
 * @author Hong Jie
 * Description: Takes in a TSPMap and performs MST and 2-approximation TSP on given map of vertices.
 * 				Uses Prim's algorithm for finding MST and DFS tree-walk for TSP.
 */
public class TSPGraph implements IApproximateTSP {

	private TSPMap map;
	private boolean foundMST;
	// Store states for TSP
	private ArrayList<Integer> treeWalk;
	private boolean[] visited;
	private ArrayList<ArrayList<Integer>> nbrList;
	
	@Override
	public void initialize(TSPMap map) {
		this.map = map;
		foundMST = false;
		
		// Pre-processing for TSP
		visited = new boolean[map.getCount()];
		Arrays.fill(visited, false);
	}
	
	/**
	 * Find MST of given map
	 */
	@Override
	public void MST() {		
		if (map == null) {
			throw new UnsupportedOperationException("No map found");
		}
		// Run Prim's because Kruskal's was too painful to order the parents
		PointPriorityQueue pq = new PointPriorityQueue(map.getCount());
		HashSet<Integer> mst = new HashSet<>(map.getCount());
		// Pre-process for TSP too
		nbrList = new ArrayList<>(map.getCount());

		for (int v=0; v < map.getCount(); v++) {
			
			pq.insert(v, Integer.MAX_VALUE);
			// Maintain adjacency list of all forward-linking neighbors
			ArrayList<Integer> nbrs = new ArrayList<>();
			nbrList.add(nbrs);
		}
		
		pq.decreaseKey(0, 0);
		while (!pq.isEmpty()) {

			int u = pq.extractMin();
			mst.add(u);
			// Add reverse link, i.e. outgoing neighbors from start point
			if (u != 0) {
				nbrList.get(map.getLink(u)).add(u);
			}
			
			for (int v=0; v < map.getCount(); v++) {							
				if (!mst.contains(v)) {
					boolean flag = pq.decreaseKey(v, map.pointDistance(u, v));	// Check if U can be the parent of V
					if (flag) {
						map.setLink(v, u, false);								// Set parents backward from end to start
					}
				}
			}
		}
		foundMST = true;
	}
	
	/**
	 * Find TSP of given map using 2-approximation
	 */
	@Override
	public void TSP() {
		if (!foundMST) {
			MST();
		}
		
		// Do DFS tree-walk on MST
		treeWalk = new ArrayList<>(map.getCount() * 2);
		DFS(0);
		
		// Take shortcuts and build TSP path
		int start = treeWalk.get(0);
		
		for (int next : treeWalk) {
			// If new node, add link to previous unvisited node
			if (!visited[next]) {
				visited[next] = true;
				map.setLink(start, next, false);
				start = next;		
			} 
		}
		map.setLink(start, 0, false);					// Connect end point and start point
		map.redraw();
	}
	
	// DFS helper method to perform tree-walk and obtain path at most twice length of optimal
	private void DFS(int i) {
		treeWalk.add(i);
		ArrayList<Integer> nbrs = nbrList.get(i);
		
		if (nbrs.isEmpty()) {
			return;
		} 
		// Else recurse into neighbors
		for (int nbr : nbrs) {
			DFS(nbr);
			treeWalk.add(i);
		}

	}
	
	/**
	 * Check if a valid tour is found i.e. all nodes connected and visited only once
	 */
	@Override
	public boolean isValidTour() {
		
		boolean[] passed = new boolean[map.getCount()];
		Arrays.fill(passed, false);
		
		int start = 0;
		passed[start] = true;
		while (map.getLink(start) != 0) {
			start = map.getLink(start);
			if (passed[start]) {					// Found visited node
				return false;
			} else {
				passed[start] = true;
			}
		}
		
		for (int i=0; i < passed.length; i++) {
			if (!passed[i]) return false;			// At least one node not in tour
		}
		
		return true;
	}

	/**
	 * Find total tour distance
	 */
	@Override
	public double tourDistance() {
		if (isValidTour()) {
			double sum = 0;
			// Iterate through every point and find distance of path to parent
			for (int i=0; i < map.getCount(); i++) {
				sum += map.pointDistance(i, map.getLink(i));
			}
			return sum;
		}
		return -1;
	}
	
	public static void main(String[] args) {
		TSPMap mapx = new TSPMap("fiftypoints.txt");
		TSPGraph g = new TSPGraph();
		g.initialize(mapx);
		g.MST();
		g.TSP();
	}

}
