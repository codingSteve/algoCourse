import java.util.*;


public class Kruksal {
  
  private static int[][] testCase0      = new int[][]{ {3,2}, {1, 2, 1 }, {1, 3, 1}, {2, 3, 10}};
  private static long    expectation0   = 1L; // Pipe cleaning test case. {1,2} {3} there are two routes across the cut and we expect the minimum one

  private static int[][] testCase1      = new int[][]{ {3,2}, {1, 2, 1 }, {1, 3, 20}, {1, 3, 10} };
  private static long    expectation1   = 10L; // {1, 2}, {3}. there are two routes across the cut and we expect the minimum one

  private static int[][] testCase2      = new int[][]{{5, 2}, { 1, 2,  1}, { 1, 3, 100}, { 1, 4, 100}, { 1, 5, 100}, { 2, 3, 100}, { 2, 4, 100}, { 2, 5, 100}, { 3, 4,  10}, { 3, 5,  10}, { 4, 5,  10},};
  private static long    expectation2   = 100L; // https://www.coursera.org/learn/algorithms-greedy/discussions/weeks/2/threads/iC2LwBGIEeeLRw7GFY8sFA

  private static int[][] testCase3      = new int[][]{{4, 2}, {1, 2, 1}, {1, 3, 4}, {1, 4, 3}, {2, 4, 2}, {3, 4, 5}};
  private static long    expectation3   = 4L;

  private static int[][][] testCases    = new int[][][] { testCase0   , testCase1    , testCase2    , testCase3    };
  private static long[]    expectations = new long[]    { expectation0, expectation1 , expectation2 , expectation3 };

  private static boolean _loud = false;

  public static void main( String[] ARGV ) throws Exception {

    int times = 1;

    for ( int i = 0 ; i < ARGV.length ; i ++ ) { 
      if ("--times".equals(ARGV[i])) times = Integer.valueOf( ARGV[i+1] ).intValue();
      
      else if ( "--file".equals( ARGV[i] )) { 
        int[][] rawInput = Utils.fileToRaggedArrayOfInts( ARGV[ i+1 ], " " );
        System.out.format("File %s has %d edges%n", ARGV[ i+1], rawInput.length -1 );

        for ( int k = times ; --k >=0 ; ) {
            long preprocessStart = System.nanoTime(); 
            PriorityQueue<Edge> edges = new PriorityQueue<>(rawInput.length - 1);
            long preprocessDuration = System.nanoTime() - preprocessStart;
            System.out.format("File %s preprocessed in %6dµs%n",ARGV[ i+1],preprocessDuration/1000 );

            Map<Integer,Node> nodes = prepareGraph( rawInput, edges );

            long start = System.nanoTime();
            mst( rawInput[0][0], edges, 4);
            long duration = System.nanoTime() - start;

            Edge nextEdge = edges.poll();
            while ( nextEdge._head._leader == nextEdge._tail._leader) 
              nextEdge = edges.poll();

            long minSeparation = nextEdge._length;

            System.out.format("File %s, run %4d calculated a minSeparation of %10d in %6dµs%n", 
              ARGV[ i+1 ], k, minSeparation, duration /1000);
          }
      }
      else if ("--loud".equals( ARGV[i])) _loud = true;
      else if ("--quiet".equals( ARGV[i])) _loud = false;
      else if ("--test".equals( ARGV[i] )){
        
        boolean allPassed = true;
        for ( int j = testCases.length ; --j >= 0 && allPassed ;  ) {
          
          for ( int k = times ; --k >=0 ; ) {

            PriorityQueue<Edge> edges = new PriorityQueue<>(testCases[j].length - 1);

            Map<Integer, Node> nodes = prepareGraph( testCases[ j ], edges );

            long start = System.nanoTime();
            if ( _loud ) System.out.format( "About to process a graph of %d node(s) and %d edge(s)%n", 
              testCases[j][0][0], testCases[j].length -1 );

            mst( testCases[j][0][0], edges, testCases[j][0][1]);

            long duration = System.nanoTime() - start;

            Edge nextEdge = edges.poll();
            while ( nextEdge._head._leader == nextEdge._tail._leader) 
              nextEdge = edges.poll();

            long minSeparation = nextEdge._length;

            if ( _loud ) { 
              for (Map.Entry<Integer, Node> e : nodes.entrySet() ) {
                if ( e.getValue() == e.getValue()._leader) {
                  System.out.format("NodeID %d _followers: %s%n", e.getValue()._nodeID, e.getValue()._followers.toString() );
                }
              }

            }

            allPassed = (minSeparation == expectations[j]);

            System.out.format("test case %2d, run %4d calculated a cost of %10d (expected %10d) in %6dµs, passed %b %n", 
              j, k, minSeparation, expectations[j], duration /1000, allPassed);
          }
        }
      }
    }



  }

   public static Map<Integer, Node> prepareGraph( int[][] input, PriorityQueue<Edge> edges ) { 
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

      edges.offer( new Edge(head, tail, input[i][2]) );

    }
    return nodes;

  }

  public static void mst( int nodeCount, PriorityQueue<Edge> edges, int requiredClusters) {
    long cost = 0L;
    int clusters = nodeCount;
    
    while ( requiredClusters != clusters ) {
      if ( _loud ) System.out.println("clusters == " + clusters + ", edgesRemaining ==  " + edges.size());
      Edge cheapest = edges.poll();
      if ( cheapest._tail._leader == cheapest._head._leader ) continue;

      if ( _loud ) logEdge(cheapest);

      union( cheapest._head, cheapest._tail );
      cost += cheapest._length;
      clusters--;

    }

  }

private static void logEdge( Edge e ){
  System.out.format("About to add %s to the mst%n", e.toString());
}
  

  private static void union( Node x, Node y ) {
    
    final List<Node> xFollowers = x._leader._followers;
    final List<Node> yFollowers = y._leader._followers;

    final List<Node> enlisted;   
    final List<Node> recruits;   
    final Node       newLeader;  

    if ( yFollowers.size() > xFollowers.size() ) {
      enlisted  = yFollowers;
      recruits  = xFollowers;
      newLeader = y._leader;
      x._leader = y._leader;
    }
    else {
      enlisted   = xFollowers;
      recruits   = yFollowers;
      newLeader  = x._leader;
      y._leader  = x._leader;
    }

    Iterator<Node> it = recruits.iterator();

    while ( it.hasNext() ){ 
      Node n = it.next();
      it.remove();

      n._leader = newLeader;
      enlisted.add( n );
    }
  }



}