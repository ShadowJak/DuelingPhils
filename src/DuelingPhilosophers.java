// Adrian Melendez
// A1540936
// COP3503C-15Fall 0001
// Dueling Philosophers

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class DuelingPhilosophers {

	@SuppressWarnings({ "resource" })
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		// ArrayList to hold all the cases
		ArrayList<PhilCase> list = new ArrayList<PhilCase>();
		
		// Number of Vertexes
		int n;
		// Number of Edges
		int m;
		
		while (true) {
			n = in.nextInt();
			m = in.nextInt();
			
			// End loop if zero vertexes.
			if (n == 0) {
				break;
			}
			
			// An array of Vertexes
			Vertex[] matrix = new Vertex[n];
			
			// Populate matrix with new Vertexes
			for (int i = 0; i < n; i++) {
				matrix[i] = new Vertex();
			}
			
			
			for (int i = 0; i < m; i++) {
				// Vertex a term is defined in. Creates an out
				int d = in.nextInt() - 1;
				// Vertex a term is used in. Creates an in
				int u = in.nextInt() - 1;
				
				// The index of the matrix is the Vertex number. This array keeps track of the
				//   ins and outs of each vertex.
				matrix[d].outs.add(u);
				matrix[u].ins.add(d);
			}
			
			// Make a case object and add it to the list to be processed later.
			PhilCase temp = new PhilCase(matrix);
			list.add(temp);
		}
		
		// Print the result for each case. Getters and Setters are for the weak (or I'm lazy).
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).result);
		}
	}
}

class PhilCase {
	// Array to keep track of the Vertexes that have been used.
	private boolean[] used;
	// Number of Vertexes
	private int n;
	// Array of Vertexes. Index corresponds to the number of the Vertex.
	private Vertex[] matrix;
	// This is the answer we want.
	int result;
	
	PhilCase(Vertex[] matrix) {
		this.matrix = matrix;
		n = matrix.length;
		used = new boolean[n];
		result = findResult();
	}
	
	public int findResult() {
		// Starting index for an array
		int index = 0;
		// Default result value. May be changed to a 0 or 2.
		int result = 1;
		
		while (index < n) {
			// Counter to keep track of the nodes with an in degree of zero.
			int count = 0;
			// Array to keep track of the vertexes to be removed. The vertexes with an in
			//   degree of zero will be removed along with their out edges. If at any point
			//   there are no vertexes with an in degree of zero, it is known a cycle exists
			//   and the result will be 0.
			int[] remove = new int[n];
			for (int i = 0; i < n; i++) {
				// If the vertex hasn't been used AND the in degree is zero, that vertex can be used
				//   as a starting point. That vertex needs to be added to the list of vertexes to be
				//   removed and the counter index incremented.
				if (!used[i] && matrix[i].ins.size() == 0) {
					remove[count++] = i;
				}
			}
				
			// If no vertex has an in degree of zero, there is a cycle.
			if (count == 0) return 0; 
			// If more than one vertex has an in degree of zero, either can be the start so there is 
			//   more than one path available. We can't return here because a cycle might still be found.
			if (count > 1) result = 2;
			
			// Get rid of the 0 degree vertexes
			for (int i = 0; i < count; i++) {
				// Increment the index for each Vertex to be removed.
				index++;
				// Set the removed Vertex to true in the used list so it will be skipped over in subsequent
				//   iterations.
				used[remove[i]] = true;
			
				// For each out edge in each vertex, remove that edge so a new batch of Vertexes can be the
				//   new potential start points that have zero in degree.
				for (Integer j : matrix[remove[i]].outs) {
					matrix[j].ins.remove(remove[i]);
				}
					
			}
		}
		
		// After everything is done, return result.
		return result;
	}
	
}

class Vertex {
	// HashSets were used because they don't allow duplicate values.
	// Could have used a Hash Table for this but this program doesn't deal with key/value pairs, 
	//   it only deals with keys.
	
	// This Vertex defines a term in this set of other vertexes 
	public HashSet<Integer> outs = new HashSet<Integer>();
	// This Vertex uses a term in this set from other vertexes 
	public HashSet<Integer> ins = new HashSet<Integer>();;
	
}