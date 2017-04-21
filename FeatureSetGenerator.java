

public class FeatureSetGenerator {
  static boolean _verbose = false;

  private abstract static class IntPredicate {
    abstract boolean test(int n);
  }

  public static IntPredicate alwaysFalse = new IntPredicate() { public boolean test( int n ) { return false; } };
  public static IntPredicate alwaysTrue  = new IntPredicate() { public boolean test( int n ) { return true ; } };

  public static void main( String[] ARGV ){
    _verbose = true;
    int result = generate( 3, 2, 4, alwaysFalse);

    Utils.logInts( result ); // expect four features sets one step away
    System.out.format( "hammingDist(%2d,%2d)== %d %n", 3, result, Utils.hamDistance(3, result) ); 
  }

  public static int generate( int startingSet, int maxDistance, int width, IntPredicate p ){
    
    int resultCount = 0;
    for (int i = 0; i <= maxDistance ; i++ ){
      resultCount += (int) Math.pow(width, i);
      // if ( _verbose ) Utils.logInts( maxDistance, i, resultCount );
    }
    // if ( _verbose ) System.out.println( "Expected resultCount == " + (resultCount +1) );

    int [] result = new int[ resultCount + 1 ];

    result[0]          = startingSet;
    int firstEmptySlot = 1;
    int nextStarter    = 0;

    int distance = 0;
    while ( distance < maxDistance ) {
      // if ( _verbose ) System.out.format("about to create features distance %d away%n", distance+1);

      for ( int j = (int) Math.pow(width, distance ) ; --j >= 0  ; ) {
        // if ( _verbose ) System.out.format("About to use pos %d as next starter %n", nextStarter);
        int set = result[ nextStarter++ ];

        for ( int i = 0 ; i < width ; i ++ ){
          int newFeatureSet = ( set ^ (1<<i) );
          if ( p.test( newFeatureSet ) ) return newFeatureSet;
          result[ firstEmptySlot++ ] = newFeatureSet ;
          if ( _verbose ) {
            Utils.logInts( result );
            System.out.print("Distances [");
            for ( int k = 0 ; k < firstEmptySlot ; k++ ) System.out.format( "%d,", Utils.hamDistance(startingSet, result[k]) );
            System.out.println(']');

          }
        }
      }
      distance++;
    }
    
    return -1; // utter failure

  }
}