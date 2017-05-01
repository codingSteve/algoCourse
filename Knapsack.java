public class Knapsack {
  private static boolean _loud = false;

  private static int[][]   testCase0    = new int[][]{{1, 1}, {1, 1}};
  private static int       expectation0 = 1; // pipe cleaning test case;

  private static int[][]   testCase1    = new int[][]{{6,4}, {3,4}, {2,3},{4,2}, {4,3}};
  private static int       expectation1 = 8; // from the lectures

  private static int[][][] testCases    = new int[][][]{ testCase0   , testCase1   , };
  private static int[]     expectations = new int[]{     expectation0, expectation1, };

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
      else if ( "--test".equals(args[i])) {
        
        TESTING:
        for (int j = 0 ; j < testCases.length ; j++ ) {
          for (int k = times ; --k >=0 ; ) {
            long start = System.nanoTime();

            int[][] solution = fill( testCases[j] );
            if (_loud) { 
              System.out.println("Value solution:");
              Utils.logRaggedInts( solution );
            }

            int totalValue   = solution[testCases[j][0][1]][testCases[j][0][0]]; // final element of the grid

            long duration = System.nanoTime() - start;

            System.out.format("Run %2d of testCase %2d produced value of %6d (expected %6d) in %6dµs%n",
              k, j, totalValue, expectations[j], duration / 1000);

            if (totalValue != expectations[j]) break TESTING;
            
          }
          
        }

        

      }
      
    }
  }

  static int[][] fill( int[][] input ){
    final int W = input[0][0];
    final int N = input[0][1];

    // plus one here to line up with the input array
    int[][] solution = new int[N+1][W+1];

    for( int i = 1 ; i < input.length ; i++  ){
      final int vi = input[i][0];
      final int wi = input[i][1];


      for ( int x = wi ; x <= W ; x++ ) {

        int[] previousColumn = solution[ i-1 ];
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