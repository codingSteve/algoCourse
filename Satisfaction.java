import java.util.*;

public class Satisfaction{
  private static boolean _loud = false;

  private static int[][] testCase0      = new int[][]{{3},{1,2}, {1,2}, {2,-3}};
  private static boolean expectation0   = true;

  private static int[][] testCase1      = new int[][]{{3},{1,-2}, {-1,-2}, {2,-3}};
  private static boolean expectation1   = false;

  private static int[][][] testCases    = new int[][][]{ testCase0,    testCase1,    };
  private static boolean[] expectations = new boolean[]{ expectation0, expectation1, };


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


            System.out.format("Run %3d of file %s produced %5s in %10dµs.%n", 
              k, fileName, actual, (duration / 1000) );
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
    final boolean[] instance = new boolean[variables];
    final Condition[] conditions = new Condition[rawInput.length];

    for ( int i = rawInput.length ; --i >= 1 ; ) { 

      conditions[i] = new Condition( (rawInput[i][0] >=0), Math.abs(rawInput[i][0]) , 
                                     (rawInput[i][1] >=0), Math.abs(rawInput[i][1]) , 
                                     instance
                                    );
    }

    return solve( variables, instance, conditions);
  }

  public static boolean solve ( int variables, boolean[] instance, Condition[] conditions ) { 
    if ( _loud ) Utils.logObjects( (Object[]) conditions); 

    TRIALS:
    for ( int i = (int) Math.log( variables ); --i>=0;){

      Arrays.fill( instance, false);

      LOCAL_SEARCH:
      for ( int j = 2*(int)Math.pow( variables, 2); --j>=0; ) {
        fixAFailingCondition( conditions, instance );
        if( _loud ) Utils.logBooleans( instance );

        for ( int k = conditions.length; --k>=1; ) if ( ! conditions[k].test() ) continue LOCAL_SEARCH;

        return true;

      }
    }   
    return false ; 
  } 

  private static void fixAFailingCondition( Condition[] conditions, boolean[] instance ) {
    for ( int i = conditions.length ; --i>=1 ; ){
      Condition c = conditions[i];
      if ( !c.test() ) {
        if ( _loud ) System.out.println( "Found a failing condition: " + c );

        instance[ c._v1 ] = !instance[ c._v1 ]; // change v1
        if ( _loud ) logInstanceAndCondition( instance, c); 
        if ( c.test() ) return;

        
        instance[ c._v1 ] = !instance[ c._v1 ]; // reset v1
        instance[ c._v2 ] = !instance[ c._v2 ]; // change v2
        if ( _loud ) logInstanceAndCondition( instance, c); 
        if ( c.test() ) return;
        
        instance[ c._v1 ] = !instance[ c._v1 ]; // change v1
        if ( _loud ) logInstanceAndCondition( instance, c); 
        
        if ( c.test() ) return;

        instance[ c._v1 ] = !instance[ c._v1 ]; // reset v1
        instance[ c._v2 ] = !instance[ c._v2 ]; // reset v2
        if ( _loud ) logInstanceAndCondition( instance, c); 
      }
    }
  }
  private static void logInstanceAndCondition( boolean[] instance, Condition c){
    System.out.print(c);
    Utils.logBooleans( instance );
  }

  
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
    
    boolean test() {
      return (_b1) ? _instance[_v1] : !  _instance[_v1] || (_b2) ? _instance[_v2] : !   _instance[_v2];
    }

    @Override
    public String toString() { 
      return "{(" + 
                ((_b1) ? "" : "!" ) + _v1 +
                " || " + 
                ((_b2) ? "" : "!" ) + _v2 + 
             ") -> " + test() + '}';
    }
  }


}
