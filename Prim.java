
/**
 * This class computes a minimum spanning tree on an undirected
 * graph using Prims' algorithm.
 *
 * The input data describes the edges of the graph with integer
 * edge costs. It has the format:
 *
 *     [number_of_nodes] [number_of_edges]
 *     [one_node_of_edge_1] [other_node_of_edge_1] [edge_1_cost]
 *     [one_node_of_edge_2] [other_node_of_edge_2] [edge_2_cost]
 *     …
 *
 * For example, the third line of the file is "2 3 -8874",
 * indicating that there is an edge connecting vertex #2 and
 * vertex #3 that has cost -8874.
 * 
 * We do NOT assume that edge costs are positive, nor do we
 * assume that they are distinct.
 * 
 * We report the overall cost of a minimum spanning tree (an
 * integer, which may or may not be negative).
 * 
 * IMPLEMENTATION NOTES: This graph is small enough that the
 * straightforward O(mn) time implementation of Prim's algorithm
 * should work fine. 
 *
 * OPTIONAL: For those of you seeking an
 * additional challenge, try implementing a heap-based version.
 * The simpler approach, which should already give you a healthy
 * speed-up, is to maintain relevant edges in a heap (with keys =
 * edge costs). The superior approach stores the unprocessed
 * vertices in the heap, as described in lecture. Note this
 * requires a heap that supports deletions, and you'll probably
 * need to maintain some kind of mapping between vertices and
 * their positions in the heap.
*/
public class Prim {
  public static void main( final String[] ARGV ) { 
  }

  /**
   * Compute the cost of a minimum spanning tree of a connected
   * graph.
   *
   * Until we have explored all nodes:
   * <ol>
   * <li>select the edge leaving our explored area with the
   * lowest cost which does not form a loop.</li>
   * <li>add the outgoing edges from the newly explored node to
   * our set of candidate edges.</li>
   * <li>increment the total cost of the spanning tree</li>
   * </ol>
   *
   *
   */
  public long mst ( int nodeCount, int edgeCount, Node startNode ) {

    PriorityQueue<Edge> nextEdges = new priorityQueue<Edge>( edgeCount / nodeCount * 2);

    nextEdges.addAll( startNode.getEdges() );

    long totalCost = 0L;
    int exploredNodeCount = 1;

    while ( exploredNodeCount != nodeCount ) { 
      Edge cheapest = nextEdges.remove();
      Node destination = cheapest._tail;
      if ( ! destination._explored ) {
        destination._explored = true;
        totalCost += cheapest._cost;
        nextEdges.addAll( destination.getEdges() );
      }

    }



    return totalCost;
  }

}
