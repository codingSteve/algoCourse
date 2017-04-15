import java.util.*;
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
  private static Node A = new Node(1);
  private static Node B = new Node(2);
  private static Node C = new Node(3);
  private static Node D = new Node(4);

  private static int[][] testCase0      = new int[][]{ {2,1}, {1, 2, 1 }};
  private static long    expectation0   = 1L; // Pipe cleaning test case.

  private static int[][] testCase1      = new int[][]{ {3,3}, {1, 2, 1 }, {2, 3, 2}, {1,3,1} };
  private static long    expectation1   = 2L; // MST 3 -> 1 -> 2, cost of 2

  private static int[][] testCase2      = new int[][] {{ 4, 5 } , { 1, 2, 1 } , { 2, 4, 2 } , { 3, 1, 4 } , { 4, 3, 5 } , { 4, 1, 3 } ,};
  private static long    expectation2   = 7L; // from https://www.coursera.org/learn/algorithms-greedy/discussions/weeks/1/threads/vZmiuf_9EeaJEAqoFc7ZZA
  

  private static int[][] testCase3      = new int[][] {{ 6, 10 } , {1, 2, 6,}, {1, 4, 5,}, {1, 5, 4,}, {2, 4, 1,}, {2, 5, 2,}, {2, 3, 5,}, {2, 6, 3,}, {3, 6, 4,}, {4, 5, 2,}, {5, 6, 4,}};
  private static long    expectation3   = 14L; // This is from Algorithms ("DPV") page 139:

  private static int[][][] testCases    = new int[][][] { testCase0   , testCase1    , testCase2    , testCase3    };
  private static long[]    expectations = new long[]    { expectation0, expectation1 , expectation2 , expectation3 };

  private static boolean _loud = false;

  public static void main( final String[] ARGV ) { 

    int times = 1;

    for ( int i = 0 ; i < ARGV.length ; i ++ ) { 
      if ("--times".equals(ARGV[i])) times = Integer.valueOf( ARGV[++i] ).intValue();
      if ("--loud".equals( ARGV[i])) _loud = true;
      else if ("--test".equals( ARGV[i] )){
        boolean allPassed = true;
        for ( int j = testCases.length ; --j >= 0 && allPassed ;  ) {
          
          for ( int k = times ; --k >=0 ; ) {
            Node n = prepareGraph( testCases[ j ] );
            long start = System.nanoTime();
            long mstCost = mst( testCases[j][0][0], testCases[j].length, n);
            long duration = System.nanoTime() - start;

            allPassed = (mstCost == expectations[j]);

            System.out.format("test case %2d, run %4d calculated a cost of %10d (expected %10d) in %6dµs, passed %b %n", 
              j, k, mstCost, expectations[j], duration /1000, allPassed);
          }
        }
      }
    }
  }

  public static Node prepareGraph( int[][] input ) { 
    int nodeCount = input[0][0];
    Map<Integer, Node> nodes = new HashMap<>( nodeCount );

    for ( int i = input.length ; --i >= 1 ; ) { 
      int headID = input[i][0];
      int tailID = input[i][1];

      Node head = nodes.get( headID );
      Node tail = nodes.get( tailID );

      if ( head == null ) {
        head = new Node( headID );
        nodes.put( headID, head);
      }

      if (tail == null ) {
        tail  = new Node(tailID); nodes.put( tailID, tail);
      }

      // if ( _loud ) System.out.format("Created edge %s %n", e.toString());

      head._edges.add( new Edge(head, tail, input[i][2]) );
      tail._edges.add( new Edge(tail, head, input[i][2]) );
    }

    for ( Map.Entry<Integer, Node> e : nodes.entrySet() ) return e.getValue();
    return null;

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
  public static long mst ( int nodeCount, int edgeCount, Node startNode ) {

    PriorityQueue<Edge> nextEdges = new PriorityQueue<Edge>( edgeCount / nodeCount * 2);

    nextEdges.addAll( startNode.getEdges() );
    startNode._explored = true;

    long totalCost = 0L;
    int exploredNodeCount = 1;

    while ( exploredNodeCount != nodeCount ) { 
      Edge cheapest = nextEdges.poll();
      if ( _loud ) System.out.format( "About to explore cheapest edge %s %n", cheapest.toString());
      Node destination = cheapest._head;

      if ( ! destination._explored ) {
        if ( _loud ) System.out.format( "Found a new node %s%n", destination.toString() );
        destination._explored = true;
        totalCost += cheapest._length;
        nextEdges.addAll( destination.getEdges() );
        exploredNodeCount++;
      }
    }



    return totalCost;
  }

}
