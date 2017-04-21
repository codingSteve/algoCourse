

public class FeatureSetGenerator {
  static boolean _verbose = false;

  abstract static class IntPredicate {
    abstract boolean test(int n);
  }

  static IntPredicate alwaysFalse = new IntPredicate() { public boolean test( int n ) { return false; } };
  static IntPredicate alwaysTrue  = new IntPredicate() { public boolean test( int n ) { return true ; } };

  public static void main( String[] ARGV ){
    
    int[] result = allNeighbours( 3, 2 , 10 );

    for ( int n : result ) {
      // System.out.format( " (3,%2d) = %d %n", n, Utils.hamDistance(3, n));
      System.out.println(n);
    }

  }

  private static int[] neighbours;
  private static int   firstEmptySlot = -1;

  public static int[] allNeighbours( int startingSet, int maxDistance, int width ){
    firstMatchingNeighbour( startingSet, maxDistance, width, null);
    int[] result = new int[firstEmptySlot];
    System.arraycopy(neighbours, 0, result,0, result.length ); 
    return  result;
  }

  public static int firstMatchingNeighbour( int startingSet, int maxDistance, int width, IntPredicate p ){
    
    int resultCount = 0;
    for (int i = 0; i <= maxDistance ; i++ ){
      resultCount += (int) Math.pow(width, i);
    }

    neighbours = new int[ resultCount + 1 ];

    neighbours[0]   = startingSet;
    firstEmptySlot  = 1;
    int nextStarter = 0;

    int distance = 0;
    while ( distance < maxDistance ) {

      for ( int j = (int) Math.pow(width, distance ) ; --j >= 0  ; ) {
        int set = neighbours[ nextStarter++ ];

        for ( int i = 0 ; i < width ; i ++ ){
          int newFeatureSet = ( set ^ (1<<i) );

          if ( Utils.hamDistance(startingSet, newFeatureSet ) != distance+1 ) continue;

          if ( p != null && p.test( newFeatureSet ) ) return newFeatureSet;

          neighbours[ firstEmptySlot++ ] = newFeatureSet ;
          if ( _verbose ) {
            Utils.logInts( neighbours );
            System.out.print("Distances [");
            for ( int k = 0 ; k < firstEmptySlot ; k++ ) System.out.format( "%d,", Utils.hamDistance(startingSet, neighbours[k]) );
            System.out.println(']');

          }
        }
      }
      distance++;
    }
    return -1; // utter failure

  }
}