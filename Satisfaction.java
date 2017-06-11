import java.util.*;

public class Satisfaction{
  private static double lnToLogBase2  = 0.6931471806D;
  private static boolean _loud = false;

  private static int[][] testCase0      = new int[][]{{3},{1,2}, {1,2}, {2,-3}};
  private static boolean expectation0   = true;

  private static int[][] testCase1      = new int[][]{{3},{1,-2}, {-1,-2}, {2,-3}};
  private static boolean expectation1   = true;

  private static int[][] testCase2      = new int[][]{{4},{1,2}, {-1,3}, {3,4}, {-2,-4}};
  private static boolean expectation2   = true;

  private static int[][] testCase3      = new int[][]{{4},{1,-2}, {-1,2}, {-2,4}, {-2,-4}, {2,4}, {2,-4},} ;
  private static boolean expectation3   = false;

  private static int[][] testCase4      = new int[][]{{1},{1,1}, {-1,-1}} ;
  private static boolean expectation4   = false;

  private static int[][] testCase5      = new int[][]{{2},{1,2}} ;
  private static boolean expectation5   = true;

  private static int[][] testCase6      = new int[][]{{1}, };
  private static boolean expectation6   = true;

  private static int[][] testCase7      = new int[][]{{7},{1,2},  {1,-4}, {2,-4}, {1,-5}, {3,-5}, 
                                                          {1,-6}, {2,-6}, {3,-6}, {4,7},  {5,7},
                                                          {6,7}} ;
  private static boolean expectation7   = true;

  private static int[][][] testCases    = new int[][][]{ testCase0,    testCase1,    testCase2,     testCase3,    testCase4,    testCase5,     testCase6,    testCase7,    };
  private static boolean[] expectations = new boolean[]{ expectation0, expectation1, expectation2,  expectation3, expectation4, expectation5,  expectation6, expectation7, };


  public static void main( String[] ARGV ) throws Exception {
    int times = 1;
    for ( int i =0, l= ARGV.length; i < l; i++){
      if ( "--times".equals( ARGV[i] )) times = Integer.valueOf( ARGV[++i] ).intValue();
      else if ( "--loud".equals( ARGV[i] )) _loud = true;
      else if ( "--quiet".equals( ARGV[i] )) _loud = false;
      else if ( "--file".equals( ARGV[i] )) {

        String fileName = ARGV[++i];
        int[][] rawInput = Utils.fileToRaggedArrayOfInts( fileName, " ");

        for ( int k = times ; --k >=0; ){
            long start = System.nanoTime();
            boolean actual = solve( rawInput );
            long duration = System.nanoTime() - start;

            System.out.format("Run %3d of file %s produced %5s in %10ds.%n", 
              k, fileName, actual, (int) (duration / 1E09) );
        }

      }
      else if( "--test".equals( ARGV[i] )) {
        TESTS:
        for ( int j = testCases.length; --j>=0 ; ){
          int[][] rawInput = testCases[j];
          for ( int k = times; --k>=0; ){
            long start = System.nanoTime();
            boolean actual = solve( rawInput );
            long duration = System.nanoTime() - start;

            boolean expected = expectations[j];

            System.out.format("Run %3d of test %2d produced %5s ( expected %5s ) in %10dµs.%n", 
              k, j, actual, expected, (duration / 1000) );

            if ( expected != actual ) break TESTS;
          }
        }
      }
      
    }
  }

  public static boolean solve( int[][] rawInput ) {
    final int variables = rawInput[0][0];
    final boolean[] instance = new boolean[variables+1];
    final Condition[] conditions = new Condition[rawInput.length];

    for ( int i = rawInput.length ; --i >= 1 ; ) { 

      conditions[i] = new Condition( (rawInput[i][0] >=0), Math.abs(rawInput[i][0]) , 
                                     (rawInput[i][1] >=0), Math.abs(rawInput[i][1]) , 
                                     instance
                                    );
    }

    // if (_loud) System.out.format("About to run for %d variables with rawInput size %d%n", variables, rawInput.length);

    return solve( variables, instance, conditions);
  }

  public static boolean solve ( int variables, boolean[] instance, Condition[] conditions ) { 
    if ( _loud ) Utils.logObjects( (Object[]) conditions); 

    int conditionsLength = conditions.length;
    int trials = Math.max(1, (int) (Math.log( variables ) / lnToLogBase2));
    long localSearchAllowance = 2*(long)Math.pow( variables, 2);

    // if (_loud ) System.out.format("(TRIALS, LOCAL_SEARCH)==(%d, %d)%n", trials, localSearchAllowance);

    TRIALS:
    for ( int i = trials; --i>=0;){
      // if (_loud ) System.out.println( "About to initialize for trial == " + i);

      initialize( instance );
      // if( _loud ) Utils.logBooleans( instance );

      LOCAL_SEARCH:
      for ( long j = localSearchAllowance; --j>=0; ) {
        // if (_loud ) System.out.println( "LOCAL_SEARCH j == " + j );

        SATISFACTION_CHECK:
        for ( int k = conditionsLength ; --k>=1 ; ) {
          if ( conditions[k].fails() ) {
            maybeFixAFailingCondition( conditions[k], instance );
            if( _loud ) Utils.logBooleans( instance );
            continue LOCAL_SEARCH;
          }
        }
        
        return true;
      }
    }   
    return false ; 
  } 

  private static void initialize( final boolean[] instance ) {
    for( int j = instance.length; --j>=1 ; ) instance[j] = coinTossIsHeads();
  }

  private static boolean coinTossIsHeads() { return Math.random() > 0.5 ; }

  /**
  * Flip a coin to decide which variable to change.
  * but only follow the suggestion if the indicated 
  * variable is causing the Condition to fail.
  * 
  */
  private static void maybeFixAFailingCondition( Condition c, boolean[] instance ) {    
    int v1 = c._v1;
    int v2 = c._v2;

    if ( coinTossIsHeads() ) {
      if ( c.clauseOneFails() ) {
        flipVariable( v1, instance) ;
        return;
      }
      else {
        flipVariable( v2, instance );
        return;
      }
    }
    
    if ( c.clauseTwoFails() ) {
        flipVariable( v2, instance );
        return;
      }
      else {
        flipVariable( v1, instance );
        return;
      }
  }

  private static void flipVariable( int i, boolean[] instance ) { instance[ i ] ^= true; }
  
  private static class Condition{
    final boolean[] _instance;
    final boolean _b1;
    final boolean _b2;

    final int _v1;
    final int _v2;
    
    Condition( boolean b1, int v1, boolean b2, int v2, boolean[] instance ){
      _b1 = b1; _b2 = b2;
      _v1 = v1; _v2 = v2;

      _instance = instance;
    }
    
    boolean clauseOneFails() {
      return ( _b1 != _instance[_v1] );
    }

    boolean clauseTwoFails() {
      return ( _b2 != _instance[_v2] );
    }

    /**
    * The test is for TT or FF hence not(xor(a,b))
    */
    boolean passes() {
      if ( _b1 != _instance[_v1] )
        if ( _b2 != _instance[_v2] )
          return false;
      
      return true;
    }

    boolean fails() {
      if ( _b1 != _instance[_v1] )
        if ( _b2 != _instance[_v2] )
          return true;
      
      return false;
    }

    @Override
    public String toString() { 
      return "{(" + 
                ((_b1) ? "" : "!" ) + _v1 +
                " || " + 
                ((_b2) ? "" : "!" ) + _v2 + 
             ") -> " + passes() + '}';
    }
  }


}
