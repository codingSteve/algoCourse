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

  private static int[][] testCase7      = new int[][]{{7},{1,2},  {1,-6}, 
                                                          {2,-4}, {2,-6}, 
                                                          {3,-5}, {3,-6}, 
                                                          {4,7},  {1,-4}, 
                                                          {5,7},  {1,-5}, 
                                                          {6,7}};
  private static boolean expectation7   = true;

  private static int[][] testCase8      = new int[][]{{4}, {1,1}, {-1,-1}, {1, 4}, {2, 4}, {3, 4}};
  private static boolean expectation8   = false;

  private static int[][][] testCases    = new int[][][]{ testCase0,    testCase1,    testCase2,     testCase3,    
                                                         testCase4,    testCase5,     testCase6,    testCase7,    
                                                         testCase8,
                                                       };
  private static boolean[] expectations = new boolean[]{ expectation0, expectation1, expectation2,  expectation3, 
                                                         expectation4, expectation5,  expectation6, expectation7, 
                                                         expectation8,
                                                       };


  @Deprecated
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

            // if ( expected != actual ) break TESTS;
          }
        }
      }
      
    }
  }

  public static boolean solve( int[][] rawInput ) {
    int variables = rawInput[0][0]; // non-final as we can discard immutables 

    final boolean[] instance = new boolean[variables+1];

    final Condition[] conditions = new Condition[rawInput.length];
    final boolean[] onlyTrue     = new boolean[variables+1];
    final boolean[] onlyFalse    = new boolean[variables+1];
    final boolean[] immutable    = new boolean[variables+1];



    for ( int i = rawInput.length ; --i >= 1 ; ) {

      Condition c = new Condition( (rawInput[i][0] >=0), Math.abs(rawInput[i][0]) , 
                                   (rawInput[i][1] >=0), Math.abs(rawInput[i][1]) , 
                                   instance
                                 );
      conditions[i] = c;


    }

    Arrays.fill( onlyTrue,  true);
    Arrays.fill( onlyFalse, true);

    findSingleUseCases(Arrays.asList(conditions), onlyTrue, onlyFalse);
    variables = markImmutables(variables, instance, onlyTrue, onlyFalse, immutable);
    List<Condition> requiredConditions = removeSlamDunks(conditions, immutable);

//    findSingleUseCases(Arrays.asList(conditions), onlyTrue, onlyFalse);
//    variables = markImmutables(variables, instance, onlyTrue, onlyFalse, immutable);
//    requiredConditions = removeSlamDunks(requiredConditions.toArray(new Condition[0]), immutable);



    if( _loud ) {
      System.out.print("starting instance  : " + instance.length          ); Utils.logBooleans(instance);
      System.out.print("immutable positions: " + immutable.length         ); Utils.logBooleans(immutable);
      System.out.print("starting conditions: " + conditions.length        ); Utils.logObjects( (Object[]) conditions);
      System.out.print("required conditions: " + requiredConditions.size()); Utils.logObjects( (Object[]) requiredConditions.toArray(new Condition[]{}));
    }

    if (_loud) System.out.format("About to run for %d variables with rawInput size %d%n", variables, rawInput.length);

    return solve( variables, instance, requiredConditions.toArray(new Condition[]{}), immutable);
  }

  private static List<Condition> removeSlamDunks(Condition[] conditions, boolean[] immutable) {
    List<Condition> requiredConditions = new ArrayList<>();
    for ( int i = conditions.length ; --i >= 1 ; ) {
      Condition c = conditions[i];
      if ( immutable[ c._v1 ] || immutable[ c._v2 ] ) continue;
      requiredConditions.add( c );
    }
    return requiredConditions;
  }

  private static void findSingleUseCases(Collection<Condition> conditions, boolean[] onlyTrue, boolean[] onlyFalse) {
    for ( Condition c : conditions ) {
      if ( c == null ) continue;

      if ( c._b1 )
        onlyFalse[c._v1] = false;
      else
        onlyTrue[c._v1]  = false;

      if ( c._b2 )
        onlyFalse[c._v2] = false;
      else
        onlyTrue[c._v2]  = false;
    }
  }

  private static int markImmutables(int variables, boolean[] instance, boolean[] onlyTrue, boolean[] onlyFalse, boolean[] immutable) {
    for ( int i = variables+1 ; --i>=1 ; ) {
      if (onlyTrue[i] || onlyFalse[i]){
        immutable[i] = true;
        if ( onlyFalse[i] ) instance[i] = false;
        if ( onlyTrue[i]  ) instance[i] = true;
        variables--;
      }
    }
    return variables;
  }

  @Deprecated
  public static boolean solve ( int variables, boolean[] instance, Condition[] conditions, boolean[] immutable) { 

    if ( conditions.length == 0 ) return true; 

    int conditionsLength = conditions.length;
    int trials = Math.max(1, (int) (Math.log( variables ) / lnToLogBase2));
    long localSearchAllowance = 2*(long)Math.pow( variables, 2);

    if (_loud ) System.out.format("(TRIALS, LOCAL_SEARCH)==(%d, %d)%n", trials, localSearchAllowance);

    TRIALS:
    for ( int i = trials; --i>=0;){
      if (_loud ) System.out.println( "About to initialize for trial == " + i);

      initialize( instance, immutable );
      if( _loud ) Utils.logBooleans( instance );

      LOCAL_SEARCH:
      for ( long j = localSearchAllowance; --j>=0; ) {
        if (_loud ) System.out.println( "LOCAL_SEARCH j == " + j );

        boolean allPassed = true;
        SATISFACTION_CHECK:
        for ( int k = conditionsLength ; --k>=0 ; ) {
          if ( conditions[k].fails() ) {
            allPassed = false;
            maybeFixAFailingCondition( conditions[k], instance );
            if( _loud ) Utils.logBooleans( instance );
            if( _loud ) Utils.logObjects( (Object[])conditions );
            continue LOCAL_SEARCH;
          }
        }
        if ( allPassed ) return true;
      }
    }   
    return false ; 
  } 

  private static void initialize( final boolean[] instance, boolean[] immutable ) {
    for( int j = instance.length; --j>=1 ; ) 
      if ( ! immutable[j] ) instance[j] = coinTossIsHeads();
  }

  private static boolean coinTossIsHeads() { return Math.random() > 0.5 ; }

  private static void maybeFixAFailingCondition( Condition c, boolean[] instance ) {    
    int v1 = c._v1;
    int v2 = c._v2;
    
    if ( coinTossIsHeads() ) {
      flipVariable( v1, instance) ;
      return;
    }

    flipVariable( v2, instance) ;
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
      if ( _b1 == _instance[_v1] ) return true;
      if ( _b2 == _instance[_v2] ) return true;
      
      return false;
    }

    boolean fails() {
      if ( _b1 == _instance[_v1] ) return false;
      if ( _b2 == _instance[_v2] ) return false;
      
      return true;
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
