import java.util.*;


/**
 * Your task is to code up the algorithm from the video lectures for computing strongly 
 * connected components (SCCs), and to run this algorithm on the given graph.
 *
 * Output Format: You should output the sizes of the 5 largest SCCs in the given graph,
 * in decreasing order of sizes, separated by commas (avoid any spaces). So if your
 * algorithm computes the sizes of the five largest SCCs to be 500, 400, 300, 200 and 100,
 * then your answer should be "500,400,300,200,100" (without the quotes). If your algorithm 
 * finds less than 5 SCCs, then write 0 for the remaining terms.
 * Thus, if your algorithm computes only 3 SCCs whose sizes are 400, 300, and 100,
 * then your answer should be "400,300,100,0,0" (without the quotes). 
 * (Note also that your answer should not have any spaces in it.)
 *
 * WARNING: This is the most challenging programming assignment of the course. 
 * Because of the size of the graph you may have to manage memory carefully. 
 * The best way to do this depends on your programming language and environment, 
 * and we strongly suggest that you exchange tips for doing this on the discussion forums.
 * **/

public class StronglyConnectedComponents {

  private static Comparator<List> _orderByLengthDesc = new Comparator(){
      public int compare( Object a, Object b ) {
        int la = 0, lb = 0; 

        if ( a != null && b != null && a == b ) {
          return 0;
        }

        if ( a != null && a instanceof List) la = ( (List<?>)a).size();
        if ( b != null && b instanceof List) lb = ( (List<?>)b).size();

        return (la < lb) ? +1 : (( la > lb ) ? -1 : 0 );

      }
    };


  private static boolean _quiet = true;
  private static boolean _loud  = false;

  private static int[][] testCase0      = new int[][]{{1, 2}, {2, 1}, {1, 3}, {3, 4}, {4, 5}, {5, 3}}; // an eye and a triangle
  private static int[]   expectations0  = new int[]  {3, 2, 0, 0, 0};

  // test cases from https://www.coursera.org/learn/algorithms-graphs-data-structures/discussions/weeks/1/threads/1D9ElKnLEeayfRIMfs2y9A
  private static int[][] testCase1      = new int[][]{{1, 4}, {2, 8}, {3, 6}, {4, 7}, {5, 2}, {6, 9}, {7, 1}, {8, 5}, {8, 6}, {9, 7}, {9, 3}};
  private static int[]   expectations1  = new int[]  {3, 3, 3, 0, 0};

  private static int[][] testCase2      = new int[][]{{1, 2}, {2, 6}, {2, 3}, {2, 4}, {3, 1}, {3, 4}, {4, 5}, {5, 4}, {6, 5}, {6, 7}, {7, 6}, {7, 8}, {8, 5}, {8, 7}};
  private static int[]   expectations2  = new int[]  {3, 3, 2, 0, 0};

  private static int[][] testCase3      = new int[][]{{1, 2}, {2, 3}, {3, 1}, {3, 4}, {5, 4}, {6, 4}, {8, 6}, {6, 7}, {7, 8}};
  private static int[]   expectations3  = new int[]  {3,3,1,1,0};

  private static int[][] testCase4      = new int[][]{{2, 3}, {3, 1}, {3, 4}, {4, 3}, {4, 6}, {5, 4}, {6, 4}, {6, 7}, {7, 8}, {8, 6}, {1, 2}};
  private static int[]   expectations4  = new int[]  {7,1,0,0,0};

  private static int[][] testCase5      = new int[][]{ {1,2}, {2,3}, {2,4}, {2,5}, {3,6}, {4,5}, {4,7}, {5,2}, {5,6}, {5,7}, {6,3}, {6,8}, {7,8}, {7,10}, {8,7}, {9,7}, {10,9}, {10,11}, {11,12}, {12,10}};
  private static int[]   expectations5  = new int[]  { 6,3,2,1,0};

  private static int[][][] testCases    = new int[][][] { testCase0     , testCase1     , testCase2     , testCase3     , testCase4     , testCase5     };
  private static int[][]   expectations = new int[][]   { expectations0 , expectations1 , expectations2 , expectations3 , expectations4 , expectations5 };


  public static void main( String ARGV[] ) throws Exception {

    for (int i = 0 ; i < ARGV.length ; i++ )  {
      if ( "--loud".equals( ARGV[i] ) ) {
          _loud = true; _quiet = true;
          continue;
        }
        if ( "--quiet".equals( ARGV[i]) ) {
          _loud = false; _quiet = true;
          continue;
        }
        if ( "--silent".equals( ARGV[i] ) ) { 
          _loud = false; _quiet = false;
          continue;
        }

        if ( "--test".equals( ARGV[i] ) ) {
          for (int j = testCases.length ; --j >=0 ; ) { 
            System.out.println("test case: " + j);
            timeAndCheck(testCases[j], expectations[j]);
          }
        }
        else if ( "--file".equals(ARGV[i]) ) { 
          int[][] input = Utils.fileToRaggedArrayOfInts(ARGV[++i], " ");
          System.out.println( "Processing from file: " + ARGV[i] );
          timeAndCheck( input, null );
        }
    }
  }

  public static void timeAndCheck( int[][] graph, int[] expectations ){

    if ( _loud ) {
      Utils.logRaggedInts( graph );
    }

    long start = System.nanoTime();
    int[] result = stronglyConnectedComponents( graph );
    long duration = ( System.nanoTime() - start ) / 1000;
    System.out.print("results:      "); Utils.logInts( result       );

    if ( expectations != null ){
      boolean passed = true; 
      
      System.out.print("expectations: "); Utils.logInts( expectations );

      for ( int j = expectations.length ; --j >= 0 && passed  ; ) { 
        passed &= result[j] == expectations[j];
      }
      System.out.println("success? " + passed);
    }
    System.out.println("Complete in " + duration + "µs");

  }


  public static int[] stronglyConnectedComponents( int[][] rawInput ){
    Node[] nodes = new Node[ rawInput.length ];

    if ( _quiet ) System.out.println( "About to build graph ");
    for ( int r = rawInput.length ; --r >= 0 ; ){
      Node head = nodes[ rawInput[r][0] ];
      Node tail = nodes[ rawInput[r][1] ];

      if ( head == null ) { 
        int id = rawInput[r][0];
        nodes[id] = head = new Node( id );
      }

      if ( tail == null ) { 
        int id = rawInput[r][1];
        nodes[id] = tail = new Node( id );
      }

      head.addEdge( tail );
      
      if ( _loud ) {
        System.out.println( head.toString() );
      }
    }

    if (_quiet ) System.out.println( "first pass : mark finish time");
    int finishTime = 0;
    for ( int i = nodes.length ; --i >= 0 ; ) { 
      Node n =nodes[i];
      if ( n == null ) continue;
      finishTime = n.reverseDfs( finishTime );
    } 

    if ( _quiet ) System.out.println( "book keeping: unset explored and reorder" );
    Node[] reordered = new Node[nodes.length];
    for ( int i = nodes.length ; --i >= 0 ; ) { 
      if ( nodes[i] == null ) continue;
      Node n = nodes[i];
      n._explored = false;
      reordered[ n._finishTime ] = n ; 
      if ( _loud ) System.out.println( i + " " + n.toString() );
    }

    if (_quiet ) System.out.println(" Second pass: set leaders");
    // second pass to mark leaders
    for ( int i = reordered.length ; -- i>=0; ){
      Node n = reordered[i];
      if ( n == null ) continue;
      n.dfs( );
    }

    List[] leadersAndFollowers = new List[reordered.length];
    for (int i = reordered.length ; --i >= 0 ; )  {
      Node n = reordered[ i ];
      if ( n == null ) continue;
      List<Node> followers = leadersAndFollowers[n._leader];

      if ( followers == null ) {
        followers = new ArrayList<Node>();
        leadersAndFollowers[n._leader] =  followers;
      }
      followers.add(n);
    }

    
    Arrays.sort( leadersAndFollowers, _orderByLengthDesc );
    int[] result = new int[5];

    for( int i = 0; i < result.length ; i++ ) { 
      List<?> followers = leadersAndFollowers[i];
      if (followers == null ) continue;
      result[i] = followers.size();
    }
    return result;
  }
  
  public static class Node {
    int _nodeId;
    int _leader;
    int _finishTime;
    boolean _explored; 
    List<Node> _adjacentNodes;
    List<Node> _incomingEdges;


    public Node( int nodeId ) { 
      _adjacentNodes = new ArrayList<Node>();
      _incomingEdges = new ArrayList<Node>();

      _nodeId = nodeId; 
    }
    
    public String toString() { 
      String neighbours = "[";
      for ( Node n : _adjacentNodes ) { neighbours += "{" + n._nodeId + "}, "; }
      neighbours += "] [";
      for ( Node n : _incomingEdges ) { neighbours += "{" + n._nodeId + "}, "; }
      return "{" + _nodeId + " - (" + _explored + ", " + _finishTime + ", " + _leader +  ")} -> " + neighbours + "]" ;
    }

    public boolean isExplored ( boolean explored ) { return _explored = explored; }
    public boolean isExplored (                  ) { return _explored;            }

    public void addEdge( Node n ) { 
      this._adjacentNodes.add( n ); 
      n._incomingEdges.add(this);
    }
    
    public void dfs() { dfs( _nodeId ); }

    public void dfs( final int leader ) {
      if ( _loud ) 
        Utils.logStrings( "starting dfs on ", toString(), " with " + leader);
      
      if ( _explored ) return;
      _explored = true;          
      _leader = leader;

      for ( Node n : _adjacentNodes ) {
        if ( ! n.isExplored() ){

          n.dfs( leader );
        }
      }
    }

    public int reverseDfs( int t ) {
      if ( _loud ) 
        Utils.logStrings( "starting rdfs on ", toString(), " with " + t);

      if ( _explored ) return t;
      _explored = true;

      for ( Node n : _incomingEdges ) {
        if ( ! n.isExplored() ){
          t = n.reverseDfs( t );
        }
      }
      _finishTime = t+1;
      return _finishTime;
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
  
  
}
