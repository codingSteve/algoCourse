
import java.util.*;

/**
* Your task is to run Dijkstra's shortest-path algorithm on this graph, using 1 (the first vertex) as the source vertex, 
* and to compute the shortest-path distances between 1 and every other vertex of the graph. If there is no path between a 
* vertex v and vertex 1, we'll define the shortest-path distance between 1 and v to be 1000000.
*
* You should report the shortest-path distances to the following ten vertices, in order: 7,37,59,82,99,115,133,165,188,197. 
* You should encode the distances as a comma-separated string of integers. So if you find that all ten of these vertices 
* except 115 are at distance 1000 away from vertex 1 and 115 is 2000 distance away, 
* then your answer should be 1000,1000,1000,1000,1000,2000,1000,1000,1000,1000. 
* Remember the order of reporting DOES MATTER, and the string should be in the same order in which the above ten vertices are given. 
* The string should not contain any spaces. 
*
* The file contains an adjacency list representation of an undirected weighted graph with 200 vertices labeled 1 to 200.
* Each row consists of the node tuples that are adjacent to that particular vertex along with the length of that edge.
* For example, the 6th row has 6 as the first entry indicating that this row corresponds to the vertex labeled 6.
* The next entry of this row "141,8200" indicates that there is an edge between vertex 6 and vertex 141 that has length 8200.
* The rest of the pairs of this row indicate the other vertices adjacent to vertex 6 and the lengths of the corresponding edges.
*
* IMPLEMENTATION NOTES: This graph is small enough that the straightforward O(mn) time implementation of Dijkstra's algorithm should work fine. 
* OPTIONAL: For those of you seeking an additional challenge, try implementing the heap-based version. 
* Note this requires a heap that supports deletions, and you'll probably need to maintain some kind of mapping between vertices and their 
* positions in the heap.
*/
public class Dijkstra {
  private static final int MAX_PATH = 1000000;
  private static final int[] DESTINATION_NODES = new int[]{7,37,59,82,99,115,133,165,188,197};
  
  private static String[] testCase0 = new String[] { 
    "1 2,1", "2 3,1", "3 4,1", "4 5,1", "5 4,1"
  };

  private static int[] expectation0 = new int[]{0,1,2,3,4};

  private static String[] testCase1 = new String[]{ 
   "1 2,1 8,2",
   "2 1,1 3,1",
   "3 2,1 4,1",
   "4 3,1 5,1",
   "5 4,1 6,1",
   "6 5,1 7,1",
   "7 6,1 8,1",
   "8 1,2 7,1"
  };

  private static int[] expectation1 = new int[]{0,1,2,3,4,4,3,2};


  private static String[][] testCases = new String[][]{ testCase0,    testCase1    };
  private static int[][]    expectations = new int[][]{ expectation0, expectation1 };


  private static boolean _quiet = true;
  private static boolean _loud  = false;

  private static Map<Integer, Node> nodes = new HashMap<>();

  public static void main( final String[] ARGV ) throws Exception {
    int times = 1;

    for ( int i = 0; i < ARGV.length ; i++  ) { 
      if ( "--loud".equals( ARGV[i] )) { 
        _quiet = true; _loud  = true;
      }
      else if ( "--quiet".equals(ARGV[i])) { 
        _quiet = true ; _loud = false ;
      }
      else if ( "--silent".equals(ARGV[i])) {
        _quiet = false ; _loud = false ; 
      }

      if ("--times".equals( ARGV[i] ) ) { 
        times = Integer.valueOf(ARGV[++i]).intValue();
      }

      if ( "--test".equals( ARGV[i] )) { 
        if ( _loud ) System.out.println( "About to run tests " + times + " time(s).");

        while ( times-- >= 1 ) { 
          
          for ( int j = testCases.length ; --j >= 0 ; ) { 
            if ( _loud ) System.out.println("About to run test case " + j);
            nodes.clear();
            timeAndCheck(testCases[j], expectations[j]);
          }
        }
        times = 1;
      }
      else if ( "--file".equals( ARGV[i] ) ) { 
        String fileName = ARGV[++i];
        String[] rawInput = Utils.fileToStringArray( fileName );
        if ( _loud ) Utils.logStrings( rawInput );

        if ( _loud ) System.out.println( "About to process files " + times + " time(s).");
        while ( times-- >= 1 ) { 
          nodes.clear();
          int[] pathLengths = timeAndCheck( rawInput, null ); // no expectations for an input file.
          System.out.print("Specific nodes: [");
          for ( int j = 0 ; j < DESTINATION_NODES .length ; j++ ) { 
            System.out.print( pathLengths[DESTINATION_NODES[j] - 1 ]);
            System.out.print(',');
          }
          System.out.println("]");
        }
        times = 1 ;
      }
    }
  }

  public static int[] timeAndCheck( String[] rawInput, int[] expectation ) { 
      Node[] graph = parseInput( rawInput );

      if ( _loud ) Utils.logObjects( (Object[]) graph);

      long start = System.nanoTime();

      int[] shortestPaths = dijkstra( graph );

      long duration = System.nanoTime() - start;


      System.out.println( "Runtime " + (duration / 1000 ) + "µs finding paths." );
      System.out.print("result     : "); Utils.logInts(shortestPaths);


      if (expectation != null ){ 
        System.out.print("expectation: "); Utils.logInts( expectation );
        System.out.print("Passed?    : ");
        boolean passed  = true; 
        for ( int i = expectation.length ; --i >= 0 && passed ; ) { 
          passed &= shortestPaths[i] == expectation[i];
        }
        System.out.println(passed);
      }

      return shortestPaths ; // new int[]{MAX_PATH, MAX_PATH, MAX_PATH, MAX_PATH, MAX_PATH, 
                             //           MAX_PATH, MAX_PATH, MAX_PATH, MAX_PATH, MAX_PATH};
  }

  public static int[] dijkstra( Node[] graph ) { 
    Node s = graph[0];
    return dijkstra( graph, s);
  }

  public static int[] dijkstra( Node[] graph, Node s ) { 

    s._explored = true;

    PriorityQueue<Edge> crossingEdges = new PriorityQueue<Edge>();
    crossingEdges.addAll( s._edges );

    while ( ! crossingEdges.isEmpty() ) { // there are unexplored nodes
      Edge shortest = crossingEdges.remove();


      if ( _loud ) System.out.println("Selected  " + shortest );
      if ( shortest._head._explored ) continue; 

      Node tail = shortest._tail;
      Node head = shortest._head;
      head._shortestPath = tail._shortestPath + shortest._length;

      head._explored = true;

      crossingEdges.addAll( head._edges ) ;

      if ( _loud ) { 
        Utils.logObjects( (Object[]) graph );
        System.out.println( crossingEdges.toString() );
      }



    }


    int[] pathLengths = new int[graph.length];
    for (int n = graph.length ; --n >= 0  ; ) {
      pathLengths[n] = graph[n]._shortestPath;
    }
    return pathLengths;
  }

  public static Node[] parseInput( String[] rawInput ) { 
    Node[] parsed = new Node[rawInput.length];

    for ( int n = rawInput.length ; --n >= 0 ;  ){
      String[] tokens = rawInput[n].split("[ \t]");
      Integer nodeID = Integer.valueOf(tokens[0]);

      Node tail = nodes.get(nodeID);
      if ( tail == null ) { 
        tail = new Node( nodeID );
        nodes.put( nodeID, tail);
      }

      parsed[n] = tail;
      for (int m = tokens.length ; --m >= 1 ; ) { 
        String[] edgeDescription = tokens[m].split(",");

        Integer headID = Integer.valueOf( edgeDescription[0] );
        Node head = nodes.get( headID );
        if ( head == null ) { 
          head = new Node( headID.intValue() );
          nodes.put( headID, head );
        }

        tail._edges.add(new Edge( tail, head, Integer.valueOf( edgeDescription[1] ).intValue() ));
      }
    }

    return parsed;
  }

  public static class Node {
    final int _nodeID;

    boolean _explored     = false;
    int     _shortestPath = 0;

    PriorityQueue<Edge> _edges    = new PriorityQueue<>();
    Collection<Edge>    _incoming = new ArrayList<>();

    Node( int nodeID ) { 
      _nodeID = nodeID;
    }

    @Override
    public String toString() { 
      return "{" + _nodeID + ", (" + _shortestPath + ")} -> [" + _edges.toString() + ']';
    }
  }

  public static class Edge implements Comparable<Edge> { 
    final Node _tail;
    final Node _head;
    final int _length;

    Edge ( Node tail, Node head, int length ) { 
      _tail = tail; _head = head; _length = length;
    }

    public int score(){
      return _length + _tail._shortestPath;
    }

    public String toString() { 
      return "{" + _tail._nodeID + "->" + _head._nodeID + '(' + _length + ")}";
    }

    @Override
    public boolean equals( Object other ) { 
      if ( other != null && other instanceof Edge ) {
        Edge m2 = (Edge) other;
        return m2._head == this._head && m2._tail == this._tail && m2._length == this._length;
      }
      return false;
    }

    @Override
    public int hashCode() {
      int result = 7 ; 

      result *= 31; result += _head._nodeID;
      result *= 31; result += _tail._nodeID;
      result *= 31; result += _length;

      return result;
    }

    @Override
    public int compareTo( Edge other) { 
      if ( other == this ) {
        return 0;
      }
      
      Edge m2 = (Edge) other;
      if ( this.equals( m2 ) ) return 0;
      if (this.score() == m2.score() ) return ( (this.hashCode() < m2.hashCode() ) ? -1 : 1) ;
      return this.score() - m2.score();
    }

  }
}
