import java.util.*;

/**
 * The file contains the adjacency list representation of a simple undirected graph. 
 * There are 200 vertices labeled 1 to 200. The first column in 
 * the file represents the vertex label, and the particular row 
 * (other entries except the first column) tells all the vertices 
 * that the vertex is adjacent to.
 * So for example, the 6th row * looks like : "6  155 56  52  120..."
 * This just means that the vertex with label 6 is adjacent to
 * ( * i.e., shares an edge with) the vertices with labels
 * 155,56,52,120...)
 *
 * Your task is to code up and run the randomized contraction algorithm 
 * for the min cut problem and use it on the above graph to compute 
 * the min cut. (HINT: Note that you'll have to figure out an implementation 
 * of edge contractions. Initially, you might want to do this naively,
 * creating a new graph from the old every time there's an edge 
 * contraction. But you should also think about more efficient 
 * implementations.) 
 * (WARNING: As per the video lectures, please * make sure to run the algorithm
 * many times with different random seeds, and remember the smallest cut that you ever find.)
 *
 */

public class Karger {

  private static int[][] testGraph = new int[][]  { { 1, 2, 4}, {2, 3}, {3, 5}, {4, 1, 5} };
  private static int     minCut    = 2;

  private static int[][] testCase1 = new int[][] { {1,2,3,4,7}, {2,1,3,4}, {3,1,2,4}, {4,1,2,3,5}, {5,4,6,7,8}, {6,5,7,8}, {7,1,5,6,8}, {8,5,6,7} };
  private static int     minCut1   = 2;

  // test case 2 is a shuffle of the above
  private static int[][] testCase2 = new int[][] { {1,4,2,7,3}, {2,4,1,3}, {3,1,2,4}, {4,5,1,2,3}, {5,8,7,6,4}, {6,8,5,7}, {7,6,8,5,1}, {8,7,6,5}};
  private static int     minCut2   = 2;

  private static int[][] testCase3 = new int[][]{ {1,2,3,4}, {2,1,3,4}, {3,1,2,4}, {4,1,2,3,5}, {5,4,6,7,8}, {6,5,7,8}, {7,5,6,8}, {8,5,6,7}};
  private static int     minCut3   = 1;

  // test case fourt is a huffle of the above
  private static int[][] testCase4 = new int[][]{ {1,3,4,2}, {2,1,4,3}, {3,1,2,4}, {4,5,3,2,1}, {5,4,8,6,7}, {6,8,7,5}, {7,5,8,6}, {8,5,7,6}};
  private static int     minCut4   = 1;

  private static int[][] testCase5 = new int[][] { {10,9,20,12,14,29}, {11,3,16,30,33,26}, {12,20,10,14,8}, {13,24,39,9,20}, {14,10,12,8,5},
                                                   {15,26,19,1,36}, {16,6,3,11,30,17,35,32}, {17,38,28,32,40,9,16}, {18,2,4,24,39,1},
                                                   {19,27,26,15,1}, {2,36,23,4,18,26,9}, {20,13,9,10,12}, {21,5,29,25,37}, {22,32,40,34,35},
                                                   {23,1,36,2,4}, {24,4,18,39,13}, {25,29,21,37,31}, {26,31,27,19,15,11,2}, {27,37,31,26,19,29},
                                                   {28,7,38,17,32}, {29,8,5,21,25,10,27}, {3,35,6,16,11}, {30,16,11,33,7,37}, {31,25,37,27,26,8},
                                                   {32,28,17,40,22,16}, {33,11,30,7,38}, {34,40,22,35,6}, {35,22,34,6,3,16}, {36,15,1,23,2},
                                                   {37,21,25,31,27,30}, {38,33,7,28,17,40}, {39,18,24,13,9,1}, {4,23,2,18,24}, {40,17,32,22,34,38},
                                                   {5,14,8,29,21}, {6,34,35,3,16}, {7,30,33,38,28}, {8,12,14,5,29,31}, {9,39,13,20,10,17,2},
                                                   {1,19,15,36,23,18,39}}; 
  private static int    minCut5    = 3;

  private static int[][] testCase6 = new int[][] { {1,2,3,4,5}, {2,3,4,1}, {3,4,1,2}, {4,1,2,3,8}, {5,1,6,7,8}, {6,7,8,5}, {7,8,5,6}, {8,4,6,5,7}};
  private static int     minCut6   = 2;

  private static int[][][] testCases = new int[][][] { testGraph, testCase1, testCase2, testCase3, testCase4, testCase5, testCase6};
  private static int[]     minCuts   = new int[]     { minCut,    minCut1,   minCut2,   minCut3,   minCut4,   minCut5,   minCut6  };


  private static boolean _quiet = false;
  private static boolean _loud = false;

  public static void main(final String[] ARGV) throws Exception { 
    long seed  = -1L; int times = 1; int[][] input = new int[][]{};

    for ( int i = 0 ; i < ARGV.length ; i++ ) {
      if ( "--loud".equals( ARGV[i] ) ) {
        _loud = true;
        continue;
      }
      if ( "--quiet".equals( ARGV[i]) ) {
        _loud = false;
        _quiet = true;
        continue;
      }
      if ( "--silent".equals( ARGV[i] ) ) { 
        _loud = false; _quiet = false;
        continue;
      }

      if ( "--times".equals( ARGV[i] )) {
        times = Integer.valueOf( ARGV[ ++i ] ).intValue();
      }

      if ( "--seed".equals(ARGV[i])) {
        seed = Long.valueOf(ARGV[++i]).longValue();
      }

      if ( "--test".equals( ARGV[i] ) ) {
        for (int j = testCases.length ; --j >=0 ; ) { 
          //if ( j != 0 ) continue;
          System.out.println("test case: " + j);
          timeAndCheck(testCases[j], seed, times, minCuts[j]);
        }
      }
      else if ( "--file".equals(ARGV[i]) ) { 
        input = Utils.fileToRaggedArrayOfInts(ARGV[++i]);
        System.out.println( "Processing from file: " + ARGV[i] );
        timeAndCheck(input, seed, times, -1);
      }

    }
  }

  private static void timeAndCheck(int[][] input, long seed, int times, int expectedMinCut) { 
     int minCuts = Integer.MAX_VALUE;

     if ( _loud) System.out.print( "About to run karger on:" );
     if ( _loud) Utils.logRaggedInts( input );

    for (int j = times ; --j >= 0 ; ) { 
      long start = System.nanoTime();
      int cuts   = karger( input, seed );
      long duration = ( System.nanoTime() - start ) / 1000;
      if ( cuts < minCuts ) {
        minCuts = cuts;
        if ( _quiet ) {
          System.out.print('\r');
          System.out.print( j +  ":number of cut edges: " +  cuts + " in " + duration + "µs            ");
        }
      }
    }
    System.out.print("\nmin number of cuts found " + minCuts );

    if ( expectedMinCut != -1 ) { 
      System.out.println ( " matches expected val (" + expectedMinCut + ") ?" + (expectedMinCut == minCuts) );
    }
    else 
      System.out.print("\n");

  }

  public static int karger(int[][] rawInput, long seed ) { 

    Random generator = ( seed == -1L ) ? new Random() : new Random( seed );
    List<Edge> edges = new ArrayList<>( rawInput.length );
    Set<Node>  nodes = new HashSet<> ( rawInput.length );

    for ( int r = rawInput.length ; --r >= 0 ; ) { 
      for ( int c = rawInput[r].length ; --c >= 1 ; ) { 
        if ( rawInput[r][c] <= rawInput[r][0] ) continue;

        Node head = new Node( rawInput[r][0] );
        Node tail = new Node( rawInput[r][c] );
        edges.add( new Edge( head, tail ) );
        nodes.add( head );
        nodes.add( tail );
      }
    }

    if ( _loud ) {
      Utils.logInts( edges.size(), nodes.size() );
      for ( Edge e : edges ) System.out.println ( e.toString() );
      for ( Node n : nodes ) System.out.println ( n.toString() );
    }

    while ( nodes.size() > 2 ) { 
      final int remainingEdges = edges.size();
      //Utils.logInts( remainingEdges, nodes.size() );
      //for ( Edge e : edges ) System.out.println ( e.toString() );
      //for ( Node n : nodes ) System.out.println ( n.toString() );

      final int target = generator.nextInt( remainingEdges ); 

      Edge e = edges.remove( target );

      if ( _loud ) System.out.println( "About to collapse : " + e.toString() );

      nodes.remove( e._tail );

      Iterator<Edge> it = edges.iterator();

      while ( it.hasNext() ) { 
        Edge c = it.next(); 
        if ( _loud) System.out.print( "About to correct " + c );
        if ( c._head.equals( e._tail ) )  c._head = e._head;
        if ( c._tail.equals( e._tail ) )  c._tail = e._head;
        if ( c._head.equals( c._tail ) )  it.remove();
        if ( _loud) System.out.println( " - resulting in " + c );
      }

      if ( _loud) System.out.println("remaining nodes: " + nodes.size() + ", remaining edges: " + edges.size() );
      if ( _loud) for ( Edge a : edges ) System.out.println( a.toString() );
    }

    return edges.size();
  }

  public static class Node {
    public int _nodeId;

    public Node( int nodeId ) { _nodeId = nodeId; }
    public String toString() { 
      return "{" + _nodeId + "}";
    }

    @Override
    public int hashCode() {return _nodeId;}

    @Override
    public boolean equals( final Object other ) { 
      if ( other instanceof Karger.Node ) { 
        return this._nodeId == ( (Node) other)._nodeId;
      }
      return false;
    }

  }

  public static class Edge { 
    Node _tail;
    Node _head;
    public Edge( Node tail, Node head) { _tail = tail; _head = head; }

    @Override
    public String toString() { 
      return "(" + _head.toString() + "->" + _tail.toString() + ")";
    }
  }

}
