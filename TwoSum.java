import java.util.*;

/**
The goal of this problem is to implement a variant of the 2-SUM algorithm covered in this week's lectures.

Your task is to compute the number of target values t in the
interval [-10000,10000] (inclusive) such that there are distinct
numbers x,y in the input file that satisfy x+y=t.

(NOTE: ensuring distinctness requires a one-line addition to the
algorithm from lecture.)

**/

public class TwoSum {

  static boolean _loud  = false;
  static boolean _quiet = true;

  private static long[] testCase0      = new long[]{-3, 1, 11, 2, 2, 6, 7, 9, -1};
  private static long[] range0         = new long[]{3, 10};
  private static long   expectation0   = 8L;

  private static long[] testCase1      = new long[]{0, 1, 2, 3, 4, 5, 6};
  private static long[] range1         = new long[]{3,4};
  private static long   expectation1   = 2L;

  private static long[] testCase2      = new long[]{0, 1, 2, 3, 4, 5, 6};
  private static long[] range2         = new long[]{30,40};
  private static long   expectation2   = 0L;

  private static long[][] testCases    = new long[][]{ testCase0   , testCase1    , testCase2    };
  private static long[][] ranges       = new long[][]{ range0      , range1       , range2       };
  private static long[]   expectations = new long[]  { expectation0, expectation1 , expectation2 };

  public static void main( final String[] ARGV ) throws Exception { 
    int times = 1 ;


    for ( int i = 0 ; i < ARGV.length ; i++ ) {
      if ( "--loud".equals( ARGV[i] )   ) { _loud = true;  _quiet = true;  }
      else if ( "--quiet".equals( ARGV[i] )  ) { _loud = false; _quiet = true;  }
      else if ( "--silent".equals( ARGV[i] ) ) { _loud = false; _quiet = false; }
      else if ( "--times".equals(ARGV[i])) { 
        times = Integer.valueOf( ARGV[++i] ) ;
      }
      else if ( "--test".equals( ARGV[i] )) {
        for ( int j = times ; --j >= 0 ; ) {

          for ( int k = testCases.length ; --k>=0; ) { 
            Set<Long> testLongs = new HashSet<>();
            for (long l : testCases[k]) testLongs.add(l);

            int targetHits = 0 ; 
            for ( long l = ranges[k][1] + 1; --l>= ranges[k][0];){
              if ( _quiet ) System.out.format( "About to search for target of %d%n", l);
              Collection<Tuple<Long>> results = findPairs( testLongs, l,10 )  ;
              if ( _loud ) Utils.logObjects( results ) ;
              if ( !results.isEmpty() ) targetHits++;
            }

            System.out.format("Test case %d produced %d hit(s), expected %d%n", k, targetHits, expectations[k]);
          }

        }
      }
      else if ( "--file".equals( ARGV[i] )) { 
        Long[] longs = Utils.fileToReferenceLongs( ARGV[++i] );
        System.out.format( "File: %s had %d records%n", ARGV[i], longs.length);
        Set<Long> availableLongs = new HashSet<Long>( longs.length );
        for ( Long x : longs) availableLongs.add( x );
        int targetsHit = 0 ;

        for ( long t = -10000L; t <= 10000L; t++) { 
          if ( !findPairs( availableLongs, t, 1 ).isEmpty() ) { 
            targetsHit++;
          }

        }

        System.out.format( "File: %s had %d hits%n", ARGV[i], targetsHit );
      }
    }
  }


  public static final Collection<Tuple<Long>> findPairs ( Set<Long> availableLongs, long target, int maxNeeded ) { 
    Collection<Tuple<Long>> pairs = new LinkedList<>();

    for ( Long X : availableLongs) { 
      //long y = target - x.longVal(); ; 
      //if ( x.longVal() == y ) continue; // we want distinct values
      ///Long Y = Long.valueOf(y);
      //if ( !availableLongs.contains( Y ) ) continue;

      Long Y = findPair( availableLongs, target, X);
      if ( Y == null ) continue;

      pairs.add( new Tuple<Long>(X, Y)) ; 
      //if ( _loud ) System.out.format( "Found a pair for %d, (%d, %d)%n", target, X.longValue(), Y.longValue()); 
      if ( --maxNeeded == 0  ) return pairs;
    }

    return pairs;
  }

  private static final Long findPair ( Set<Long> availableLongs, long target, Long x ) { 
    long y = target - x.longValue(); ; 
    if ( x.longValue() == y ) return null;

    Long Y = Long.valueOf(y);
    if ( !availableLongs.contains( Y ) ) return null;

    return Y;  


  }

} 
