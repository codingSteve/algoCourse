import java.util.*;

public class Backtracking{
  public static final Boolean[] BOOLEANS = {Boolean.TRUE, Boolean.FALSE};
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
    int variables = rawInput[0][0]; // non-final as we can discard immutable values

    final Boolean[] instance = new Boolean[variables+1];

    final Condition[] conditions = new Condition[rawInput.length];
    final boolean[] onlyTrue     = new boolean[variables+1];
    final boolean[] onlyFalse    = new boolean[variables+1];
    final boolean[] immutable    = new boolean[variables+1];


    for ( int i = rawInput.length ; --i >= 1 ; ) {

      Condition c = new Condition( (rawInput[i][0] >=0), Math.abs(rawInput[i][0]) , 
                                   (rawInput[i][1] >=0), Math.abs(rawInput[i][1])
                                 );
      conditions[i] = c;


    }

    // Arrays.fill( onlyTrue,  true);
    // Arrays.fill( onlyFalse, true);

    // findSingleUseCases(Arrays.asList(conditions), onlyTrue, onlyFalse);
    // variables = markImmutables(variables, instance, onlyTrue, onlyFalse, immutable);
    // List<Condition> requiredConditions = removeSlamDunks(conditions, immutable);

    // Arrays.fill( onlyTrue,  true);
    // Arrays.fill( onlyFalse, true);

    // findSingleUseCases(Arrays.asList(conditions), onlyTrue, onlyFalse);
    // variables = markImmutables(variables, instance, onlyTrue, onlyFalse, immutable);
    // requiredConditions = removeSlamDunks(requiredConditions.toArray(new Condition[0]), immutable);

    // if( _loud ) {
    //   System.out.print("starting instance  : " + instance.length          ); Utils.logBooleans(instance);
    //   System.out.print("immutable positions: " + immutable.length         ); Utils.logBooleans(immutable);
    //   System.out.print("starting conditions: " + conditions.length        ); Utils.logObjects( (Object[]) conditions);
    //   System.out.print("required conditions: " + requiredConditions.size()); Utils.logObjects( (Object[]) requiredConditions.toArray(new Condition[]{}));
    // }

    if (_loud) System.out.format("About to run for %d variables with rawInput size %d%n", variables, rawInput.length);

    return solve( variables, instance, conditions, immutable);
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

  private static int markImmutables(int variables, Boolean[] instance, boolean[] onlyTrue, boolean[] onlyFalse, boolean[] immutable) {
    for ( int i = variables+1 ; --i>=1 ; ) {
      if (onlyTrue[i] || onlyFalse[i]){
        immutable[i] = true;
        if ( onlyFalse[i] ) instance[i] = Boolean.FALSE;
        if ( onlyTrue[i]  ) instance[i] = Boolean.TRUE;
        variables--;
      }
    }
    return variables;
  }

  /**
   * Implementation of the backtracking algorithm for 2Sat problems
   *
   *
   * @param variables
   * @param instance
   * @param conditions
   * @param immutable
   * @return
   */
  public static boolean solve ( int variables, Boolean[] instance, Condition[] conditions, boolean[] immutable) {

    if ( conditions.length == 0 ) return true;

    // a sub-problem is some (partial) assignment of boolean values across the instance.
    Stack<Boolean[]> subProblems = new Stack<>();

    Boolean[] s0 = new Boolean[instance.length];
    System.arraycopy(instance,0,s0,0,instance.length);
    subProblems.push( s0 );

    while( ! subProblems.empty() ) {
      Boolean[] partialAssignment = subProblems.pop(); // CHOOSE

      // expand;
      int i = partialAssignment.length;

      for ( ; --i>=0 ; ) {
        if ( partialAssignment[i] != null ) continue; // TODO find a variable to expand using a condition. we may be expanding pointless nodes here  
        

      OPTION:
      for ( Boolean b : BOOLEANS) { // EXPAND
        Boolean[] option = new Boolean[partialAssignment.length];
        System.arraycopy(partialAssignment, 0, option, 0, partialAssignment.length);

        option[i] = b;

        if ( _loud ) {System.out.print("about to try with: "); Utils.logBooleans(option);}

        boolean anyNulls = false; 
        SATISFACTION_CHECK:
        for( Condition c : conditions ) {
          if (c == null ) continue;
          // condition.passes([B) == null implies we can't decide for this condition yet.
          // but we might still hit a failure meaning we can ditch the option

          if ( c.passes( option ) == null ) { 
            anyNulls = true; 
            continue SATISFACTION_CHECK;
          }

          if ( c.fails( option ) ) {
            if ( _loud ) {System.out.format("Failed on condition c == %s with inputs(%s, %s) %n",c, option[ c._v1], option[ c._v2] );}
            continue OPTION; // discard
          }
        }

        if ( anyNulls ) {
          subProblems.push(option);
          continue OPTION;
        }

        if ( _loud ) {
          System.out.print("About to satisfy: ");
          Utils.logBooleans( option );
        }
        return true; // we've got all the way to the end with this partial solution
      }
      }
    }

    return false ; 
  }


  private static class Condition{
    final Boolean _b1;
    final Boolean _b2;

    final int _v1;
    final int _v2;
    
    Condition( Boolean b1, int v1, Boolean b2, int v2 ){
      _b1 = b1; _b2 = b2;
      _v1 = v1; _v2 = v2;
    }
    
    Boolean clauseOneFails( Boolean[] instance ) {
      Boolean v1 = instance[_v1];
      if  (v1 == null ) return null;
      return  _b1 != v1;
    }

    Boolean clauseTwoFails( Boolean[] instance ) {
      Boolean v2 = instance[_v2];
      if( v2 == null ) return null;
      return   _b2 != v2;
    }

    Boolean passes( Boolean[] instance) {
      Boolean v1 = instance[_v1];
      Boolean v2 = instance[_v2];

      if ( v1 != null )
        if ( _b1 == v1 ) return true;

      else if ( v2 != null )
        if ( _b2 == v2 ) return true;
        else return false;

      return null;
    }

    Boolean fails( Boolean[] instance) {
      return !passes(instance);
    }

    @Override
    public String toString() { 
      return "{(" + 
                ((_b1) ? "" : "!" ) + _v1 +
                " || " + 
                ((_b2) ? "" : "!" ) + _v2  + '}';
    }
  }


}
