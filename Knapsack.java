public class Knapsack {
  private static boolean _loud = false;

  private static int[][][] testCases    = new int[][][]{};
  private static int[]     expectations = new int[]{};

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
        for (int j = 0 ; j < testCases.length ; j++ ) {
          for (int k = times ; --k >=0 ; ) {
            long start = System.nanoTime();

            int[][] solution = fill( testCases[j] );
            if (_loud) { 
              System.out.println("Value solution:");
              Utils.logRaggedInts( solution );
            }

            int totalValue   = -1; // final element of the grid

            // int[] selectedItems = reconstruct( solution );

            long duration = System.nanoTime() - start;
            
          }
          
        }

        

      }
      
    }
  }

  static int[][] fill( int[][] input ){
    int[][] solution = new int[][]{};
    return solution;
  }


}