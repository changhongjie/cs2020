package sg.edu.nus.cs2020;

import java.util.Arrays;

public class BellmanFord implements IBellmanFord{
	
	// Stores the Adjacency Matrix of the current Graph to run Bellman-Ford with
	private AdjacencyMatrixGraph m_amg;
	
	// Tracks who is the current parent vertex, for each vertex in the graph
	private int[] m_predecessors;
	
	// Store the total distance of each vertex from the source vertex
	private int[] m_distanceFromSource;
	
	// Stores the source and destination vertices chosen for this iteration
	private int m_source;
	private int m_destination;

	
	public BellmanFord(AdjacencyMatrixGraph amg) {
		if (amg == null){
			throw new IllegalArgumentException("Invalid input");
		} else {
			m_amg = amg;
		}
	}
	
	// Construct the shortest path from source to destination
	public void constructPath(int source, int destination) {
		
		m_source = source;
		m_destination = destination;
		// Prepare distance array
		m_distanceFromSource = new int[m_amg.getNumberOfVertices()];
		Arrays.fill(m_distanceFromSource, Integer.MAX_VALUE);
		m_distanceFromSource[m_source] = 0;
		int numVertices = m_distanceFromSource.length;
		// Prepare array of parent pointer for every vertex
		m_predecessors = new int[numVertices];
		
		for (int v = m_source; v < m_source + numVertices; v++) {
			
			boolean converged = true;							// Boolean state for checking if no relaxaton has been done in an iteration
			
			for (int e = 0; e < numVertices; e++) {
				 boolean relaxed = relax((v % numVertices), e);
				 if (relaxed) {
					 converged = false;
					 m_predecessors[e] = v % numVertices;
				 }
			}
			if (converged) break;								// Shortest Path has been found
		}
		
	}
	
	// Checks if at least 1 negative cycle exists in the Graph
	public boolean hasNegativeCycle() {
		
		int numVertices = m_amg.getNumberOfVertices();
		constructPath(0, numVertices-1);
		// Check for an iteration of relaxation, expect no changes to be made
		for (int e = 0; e < numVertices; e++) {
			
			boolean relaxed = relax(0, e);
			if (relaxed) {
				return true;
			}
		}
		return false;
	}
	
	// Abstracted relax method for the process of relaxation of an edge
	private boolean relax(int source, int destination) {
		int edgeWeight = m_amg.getEdgeWeight(source, destination);
		if (m_distanceFromSource[destination] > m_distanceFromSource[source] + edgeWeight) {
			m_distanceFromSource[destination] = m_distanceFromSource[source] + edgeWeight;
			return true;
		}
		return false;
	}
	
	// Produces the String for printing the shortest path
	public String getPath() {
		
		int currentVertex = this.m_destination;
		
		StringBuilder prepender = new StringBuilder();
		StringBuilder resultHolder = new StringBuilder("" + this.m_destination);
		
		while (currentVertex != this.m_source) {
			
			currentVertex = this.m_predecessors[currentVertex];
			
			prepender.append(currentVertex);
			prepender.append(" -> ");
			prepender.append(resultHolder.toString());
			
			resultHolder.replace(0, resultHolder.length(), prepender.toString());
			prepender.delete(0, prepender.length());
		}
		
		return resultHolder.toString();
	}
	

	public static void main(String[] args) {
		
		// Example use
		AdjacencyMatrixGraph amg = new AdjacencyMatrixGraph("testFileIncremental.txt");
		BellmanFord bf = new BellmanFord(amg);
		
		System.out.println("Running Bellman-Ford on testFileIncremental...");
		System.out.println("Has negative cycle(s): " + bf.hasNegativeCycle());
		System.out.println("Shortest path: " + bf.getPath());
	}

}
