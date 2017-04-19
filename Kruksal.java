import java.util.*;


public class Kruksal {
  
  private static int[][] testCase0      = new int[][]{ {2,1}, {1, 2, 1 }};
  private static long    expectation0   = 1L; // Pipe cleaning test case.

  private static int[][] testCase1      = new int[][]{ {3,3}, {1, 2, 1 }, {2, 3, 2}, {1,3,1} };
  private static long    expectation1   = 2L; // MST 3 -> 1 -> 2, cost of 2

  private static int[][] testCase2      = new int[][] {{ 4, 5 } , { 1, 2, 1 } , { 2, 4, 2 } , { 3, 1, 4 } , { 4, 3, 5 } , { 4, 1, 3 } ,};
  private static long    expectation2   = 7L; // from https://www.coursera.org/learn/algorithms-greedy/discussions/weeks/1/threads/vZmiuf_9EeaJEAqoFc7ZZA

  private static int[][] testCase3      = new int[][] {{ 6, 10 } , {1, 2, 6,}, {1, 4, 5,}, {1, 5, 4,}, {2, 4, 1,}, {2, 5, 2,}, {2, 3, 5,}, {2, 6, 3,}, {3, 6, 4,}, {4, 5, 2,}, {5, 6, 4,}};
  private static long    expectation3   = 14L; // This is from Algorithms "DPV" page 139:

  private static int[][] testCase4      = new int[][] {{3, 2}, {1,2,1<<30}, {2,3,1<<30}};
  private static long    expectation4   = (1L<<30) + (1L<<30); // simple pipe cleaner for long paths

  private static int[][][] testCases    = new int[][][] { testCase0   , testCase1    , testCase2    , testCase3    , testCase4    };
  private static long[]    expectations = new long[]    { expectation0, expectation1 , expectation2 , expectation3 , expectation4 };

  private static boolean _loud = false;

  public static void main( String[] ARGV ) {

    int times = 1;

    for ( int i = 0 ; i < ARGV.length ; i ++ ) { 
      if ("--times".equals(ARGV[i])) times = Integer.valueOf( ARGV[i+1] ).intValue();
      
      // else if ( "--file".equals( ARGV[i] )) { 
      //   int[][] rawInput = Utils.fileToRaggedArrayOfInts( ARGV[ i+1 ], " " );
      //   for ( int k = times ; --k >=0 ; ) {
      //       Node n = prepareGraph( rawInput );
      //       long start = System.nanoTime();
      //       long mstCost = mst( rawInput[0][0], rawInput[0][1], n);
      //       long duration = System.nanoTime() - start;

      //       System.out.format("File %s, run %4d calculated a cost of %10d in %6dµs%n", 
      //         ARGV[ i+1 ], k, mstCost, duration /1000);
      //     }
      // }
      else if ("--loud".equals( ARGV[i])) _loud = true;
      else if ("--test".equals( ARGV[i] )){
        boolean allPassed = true;
        for ( int j = testCases.length ; --j >= 0 && allPassed ;  ) {
          
          for ( int k = times ; --k >=0 ; ) {

            Map<Node, List<Node>> connectedComponents = new HashMap<>(testCases[j][0][0]);
            PriorityQueue<Edge> edges = new PriorityQueue<>(testCases[j].length - 1);

            prepareGraph( testCases[ j ], edges, connectedComponents );

            long start = System.nanoTime();
            if ( _loud ) System.out.format( "About to process a graph of %d node(s) and %d edge(s)%n", 
              testCases[j][0][0], testCases[j].length -1 );

            long mstCost = mst( testCases[j][0][0], testCases[j][0][1], edges, connectedComponents);
            long duration = System.nanoTime() - start;

            allPassed = (mstCost == expectations[j]);

            System.out.format("test case %2d, run %4d calculated a cost of %10d (expected %10d) in %6dµs, passed %b %n", 
              j, k, mstCost, expectations[j], duration /1000, allPassed);
          }
        }
      }
    }



  }

   public static void prepareGraph( int[][] input, PriorityQueue<Edge> edges, Map<Node, List<Node>> connectedComponents ) { 
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

      if ( _loud ) System.out.format("Created edge %s %n", new Edge(head, tail, input[i][2]) );

    }

    for ( Map.Entry<Integer, Node> e : nodes.entrySet() ) return e.getValue();
    return null;

  }

  public static long mst( int nodeCount, int edgeCount, PriorityQueue<Edge> edges, Map<Node, List<Node>> connectedComponents ) {
    long cost = 0L;
    int requiredNodes = nodeCount;
    
    while ( requiredNodes != 0 ) {
      Edge cheapest = edges.poll();
      if ( cheapest._tail == cheapest._head._leader ) continue;

      union( cheapest._head, cheapest._tail, connectedComponents );
      cost += cheapest._length;
      requiredNodes--;
    }

    return cost;
  }

  

  private static void union( Node x, Node y, Map<Node, List<Node>> connectedComponents ) {
    
    final List<Node> xFollowers = connectedComponents.get(x);
    final List<Node> yFollowers = connectedComponents.get(y);

    List<Node> union      = xFollowers;
    List<Node> recruits   = yFollowers;
    Node       newLeader  = x._leader;

    if ( yFollowers.size() > xFollowers.size() ) {
      union = yFollowers;
      recruits = xFollowers;
      newLeader = y._leader;
    }

    x._leader = newLeader;
    y._leader = newLeader;


    for ( Node n : recruits ){
      n._leader = newLeader;
      union.add( n );
    }
  }



}