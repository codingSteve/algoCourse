import java.util.*;

public class Knapsack {
  private static boolean _loud = false;

  private static int[][]   testCase0    = new int[][]{{1, 1}, {1, 1}};
  private static int       expectation0 = 1; // pipe cleaning test case;

  private static int[][]   testCase1    = new int[][]{{6,4}, {3,4}, {2,3},{4,2}, {4,3}};
  private static int       expectation1 = 8; // from the lectures

  private static int[][]   testCase2    = new int[][]{{6, 4}, {1,2}, {1,2}, {1,2}, {100, 5}};
  private static int       expectation2 = 100; // massive bar of gold is the final item

  private static int[][]   testCase3    = new int[][]{{6, 4}, {1,2}, {1,2}, {1,2}, {1000, 7}};
  private static int       expectation3 = 3; // a block of something very valuable but too big so we choose the items which fit

  private static int[][]   testCase4    = new int[][]{{6, 4}, {1,4}, {2,4}, {3,4}, {4,4}};
  private static int       expectation4 = 4; // we can only choose one item, so pick the best

  private static int[][]   testCase5    = new int[][]{ {6,4}, {10, 2},{10, 2},{1, 6},{10, 2}, };
  private static int       expectation5 = 30; // skip the heavy and worthless third item.

  private static int[][][] testCases    = new int[][][]{ testCase0   , testCase1   , testCase2   ,  testCase3   , testCase4   , testCase5   , };
  private static int[]     expectations = new int[]{     expectation0, expectation1, expectation2,  expectation3, expectation4, expectation5, };

  public static void main(String[] args) throws Exception {
    int times = 1;

    for (int i = 0 ; i < args.length ; i++  ) {
      if ("--loud".equals(args[i])) {
        _loud = true;
      }
      else if ("--quiet".equals(args[i])){
        _loud = false;
      }
      else if ("--times".equals(args[i])) {
        times = Integer.valueOf( args[ ++ i ] );
      }
      else if ( "--file".equals(args[i])) {
        int[][] input = Utils.fileToRaggedArrayOfInts( args[++i], " ");
        for (int j = times ; --j>=0 ; ) {
          long start       = System.nanoTime();
          int[][] solution = fill( input );
          long duration    = System.nanoTime() - start;
          int totalValue   = solution[input[0][1]][input[0][0]];

          System.out.format("Run %2d of file with  fill %s produced value %d in %6dµs%n",
            j, args[i], totalValue, duration /1000);
          
        }

      }
      else if ( "--rfile".equals(args[i])) {
        int[][] input = Utils.fileToRaggedArrayOfInts( args[++i], " ");
        for (int j = times ; --j>=0 ; ) {
          long start     = System.nanoTime();
          int totalValue = rfill( input, input[0][0] , input[0][1], 0, new HashMap<Tuple, Tuple>(input[0][1]*2));
          long duration  = System.nanoTime() - start;

          System.out.format("Run %2d of file with rfill %s produced value %d in %6dµs%n",
            j, args[i], totalValue, duration /1000);
          
        }

      }
      else if ( "--test".equals(args[i])) {
        
        TESTING:
        for (int j = 0 ; j < testCases.length ; j++ ) {
          for (int k = times ; --k >=0 ; ) {
            long start = System.nanoTime();

            int[][] solution = fill( testCases[j] );
            int totalValue   = solution[testCases[j][0][1]][testCases[j][0][0]]; // final element of the grid
            long duration    = System.nanoTime() - start;

            if (_loud) { 
              System.out.println("Value solution:");
              Utils.logRaggedInts( solution );
            }

            System.out.format("Run %2d of testCase %2d with  fill produced value of %6d (expected %6d) in %6dµs%n",
              k, j, totalValue, expectations[j], duration / 1000);

            if (totalValue != expectations[j]) break TESTING;

            if (_loud) Utils.logRaggedInts(testCases[j]);

            start      = System.nanoTime();
            totalValue = rfill( testCases[j], testCases[j][0][0] , testCases[j][0][1], 0, new HashMap<Tuple, Tuple>(testCases[j][0][1]*2));
            duration   = System.nanoTime() - start;

            System.out.format("Run %2d of testCase %2d with rfill produced value of %6d (expected %6d) in %6dµs%n",
              k, j, totalValue, expectations[j], duration / 1000);

            if (totalValue != expectations[j]) break TESTING;
          }
          
        }
      }      
    }
  }

  private static class Tuple{
    final int _1;
    final int _2;
    int _value;

    public Tuple( int one, int two) {
      _1 = one;
      _2 = two;
    }

    @Override
    public boolean equals( Object other ){
      if ( other instanceof Tuple ) {
        Tuple ot = ( Tuple ) other;
        return ot._1 == _1 && ot._2 == _2;
      }
      return false;
    }
    @Override
    public int hashCode() {
      int hashCode = 7 ; 
      hashCode *= 31; hashCode+=_1;
      hashCode *= 31; hashCode+=_2;
      return hashCode;
    }

  }

  static int rfill ( int[][] input, int capacty, int item, int d, Map<Tuple, Tuple> previousSolutions ){
    if ( capacty == 0 ) return 0;
    if ( item    == 0 ) return 0;

    Tuple solution = new Tuple(capacty, item);
    Tuple ps = previousSolutions.get(solution);
    if ( ps != null ) {
      if (_loud) System.out.format("rfill: d=%d, i=%d, c=%d, pb=%d, cache hit%n",
      d, item, capacty, ps._value);
      return ps._value;
    }

    int pb = rfill( input, capacty, item - 1, d + 1, previousSolutions);


    if ( capacty < input[item][1] ) {
      return pb;
    }

    int ns = rfill( input, capacty - input[item][1], item - 1, d + 1, previousSolutions) + input[item][0];

    if (_loud ) System.out.format("rfill: d=%d, i=%d, c=%d, pb=%d, ns=%d%n",
      d, item, capacty, pb, ns);

    int value = Math.max(pb, ns);
    solution._value = value;
    previousSolutions.put(solution, solution);

    return value;
  }

  static int[][] fill( int[][] input ){
    final int W = input[0][0];
    final int N = input[0][1];

    // plus one here to line up with the input array
    int[][] solution = new int[N+1][W+1];

    for( int i = 1 ; i < solution.length ; i++  ){
      final int vi = input[i][0];
      final int wi = input[i][1];

      final int[] previousColumn = solution[ i-1 ];

      System.arraycopy( previousColumn, 0, solution[i], 0, previousColumn.length );

      for ( int x = wi ; x <= W ; x++ ) {
        
        int previousBest     = previousColumn[x];
        int newValue         = previousColumn[x-wi] + vi;

        if ( _loud ) System.out.format("i=%d, x=%d pb=%d, ns=%d%n",
          i, x, previousBest, newValue);

        solution[i][x] = Math.max( previousBest, newValue);

        if ( _loud ) Utils.logRaggedInts( solution );
        
      }

    }

    return solution;
  }


}