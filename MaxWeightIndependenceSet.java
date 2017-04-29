class MaxWeightIndependenceSet { 

  private static int[] interestingIndices = new int[]{1, 2, 3, 4, 17, 117, 517, 997};

  private static int[] testCase0      = new int[]{0};
  private static int   expectation0   = 0; // pipe cleaning test case 

  private static int[] testCase1      = new int[]{4, 1, 4, 5, 4};
  private static int   expectation1   = 8; // from the lecture

  private static int[] testCase2      = new int[]{10, 280, 618, 762, 908, 409, 34, 59, 277, 246, 779, };
  private static int   expectation2   = 2626;

  private static int[] testCase3      = new int[]{10, 2, 10, 2, 10, 2, 10, 2, 10, 2, 10, };
  private static int   expectation3   = 50;

  private static int[] testCase4      = new int[]{10, 10, 2, 10, 2, 10, 2, 10, 2, 10, 2, };
  private static int   expectation4   = 50;

  private static int[][] testCases    = new int[][]{ testCase0   , testCase1   , testCase2   , testCase3   , testCase4   , };
  private static int[]   expectations = new int[]  { expectation0, expectation1, expectation2, expectation3, expectation4, };

  private static boolean _loud = false;
  public static void main(String[] args) throws Exception {
    int times = 1;

    for ( int i = 0 ; i < args.length ; i++ ) {
      if ("--loud".equals(args[i])) _loud = true;
      else if ( "--quiet".equals( args[i] ) ) _loud = false;
      else if ( "--times".equals( args[i] ) ) times = Integer.valueOf( args[++i]);
      else if ( "--file".equals(  args[i] ) ) {
        int[] input = Utils.fileToInts( args[++i]);

        for( int j = times ; --j >= 0 ; ){

          int[] selectedNodes = new int[input.length];

          long start       = System.nanoTime();
          int[] maxWeights = mwis( input );
          reconstruct( input, maxWeights, selectedNodes);
          long duration    = System.nanoTime() - start;
          
          int maxWeight    = (input.length == 1 ) ? 0 : maxWeights[ maxWeights.length -1 ];

          System.out.print("Nodes selected: ");
          for( int k = 0 ; k < interestingIndices.length ; k++ ) { 
            System.out.print(selectedNodes[interestingIndices[k]]);
          }
          System.out.format(" in %6dµs%n", duration/1000);

        }
      }
      else if ( "--test".equals( args[i])) {
        boolean allTestsPassed = true;
        for( int j = 0 ; j < testCases.length && allTestsPassed ; j++ ){
          for( int k = 0 ; k < times && allTestsPassed; k++ ){
            int[] selectedNodes = new int[testCases[j].length];

            long start       = System.nanoTime();
            int[] maxWeights = mwis( testCases[j] );

            reconstruct( testCases[j], maxWeights, selectedNodes);

            long duration    = System.nanoTime() - start;

            if ( _loud ) {
              System.out.print("Raw Input:      "); Utils.logInts( testCases[j] );
              System.out.print("Max Weights:    "); Utils.logInts( maxWeights );
              System.out.print("selected Nodes: "); Utils.logInts( selectedNodes );
            }

            int maxWeight    = (testCases[j].length == 1 ) ? 0 : maxWeights[ maxWeights.length -1 ];

           // allTestsPassed &= maxWeight == expectations[j];

            System.out.format("Run %3d of test case %d produced a weight of %6d (expected %6d) in %6dµs%n",
              k, j, maxWeight, expectations[j], (duration / 1000));

          }

        }

      }
    }
    
  }

  public static void reconstruct( int[] input, int[] maxWeights, int[] selectedNodes ){
    for ( int l = selectedNodes.length ; --l >= 1 ; ){

      int lMinusOne = (l >= 2 ) ? maxWeights[ l-1 ] : 0 ;
      int lMinusTwo = (l >= 3 ) ? maxWeights[ l-2 ] : 0 ;

      if (lMinusOne >= lMinusTwo + input[l] )
        continue;
      
      selectedNodes[l] = 1;
      l--;
    }
  }

  public static int[] mwis( int[] input ) { 
    int[] cache = new int[input.length];
    if ( input[0] == 0  ) return new int[]{0}; // a zero length path has zero weight

    cache[0] = 0;
    cache[1] = input[1];
    for ( int i = 2 ; i < input.length ; i++ ){
      cache[i] = Math.max( cache[i-1], cache[i-2] + input[i]);
    }
    
    return cache;
  }

}