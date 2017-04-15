import java.util.*;

/**
Your task in this problem is to run the greedy algorithm that
schedules jobs in decreasing order of the difference (weight -
length).

Recall from lecture that this algorithm is not always optimal.
IMPORTANT: if two jobs have equal difference (weight - length),
you should schedule the job with higher weight first.

Beware: if you break ties in a different way, you are likely to
get the wrong answer. You should report the sum of weighted
completion times of the resulting schedule --- a
positive integer --- in the box below.


minimize the weighted average of the completion times

    min ( sum ( w_j * c_j ) ) 

*/




public class Scheduler {

  public static interface Scorer { double getScore( int[] job ); }
  public static Scorer differenceScorer = new Scorer() { public final double getScore( int[] job ){return         job[0] - job[1];}};
  public static Scorer ratioScorer      = new Scorer() { public final double getScore( int[] job ){return 1.00d * job[0] / job[1];}};

  public static abstract class JobComparator implements Comparator< int[] > {
    public abstract double getScore( int[] job );
    public abstract Scorer getScorer();

    @Override
    public int compare( int[] a, int[] b) {
      

      if ( a != null && b != null ) { 
        double aScore = 0.00d;
        double bScore = 0.00d;
        int[] arrayA = (int[]) a;
        int[] arrayB = (int[]) b;

        double score =  getScore(arrayB) - getScore(arrayA);

        if ( score == 0.00d) {
          return arrayB[0] - arrayA[0];
        }
        return ( score < 0.00d ) ? -1 : 1 ;
      }
      throw new IllegalArgumentException( "Exepected  int[]" );
    }


  }

  private static final JobComparator RATIO_COMPARATOR = new JobComparator() { 
    @Override
    public double getScore( int[] job ) {
      return ratioScorer.getScore(job);
    }
    @Override
    public Scorer getScorer() { return ratioScorer; }
  };

  private static final JobComparator DIFFERENCE_COMPARATOR = new JobComparator() { 
    @Override
    public double getScore( int[] job ) {
      return differenceScorer.getScore(job);
    }

    @Override
    public Scorer getScorer() { return differenceScorer; }


  };

  private static JobComparator _comparator = RATIO_COMPARATOR;


/*
Inputs will look like this
10000  // number of jobs in the list
8 50   // weight_of_job_1, length_of_job_1
74 59  // weight_of_job_2, length_of_job_2
… 
*/
  private static int[][] testCase0         = new int[][]{{3, 0}, {3,1 }, {2,2 }, {1, 3 }};
  private static long    diffExpectation0  = (3*1) + (2*3) + (1*6);
  private static long    ratioExpectation0 = (3*1) + (2*3) + (1*6);

  private static int[][] testCase1         = new int[][]{{3, 0}, {9,1 }, {1,1 }, {1, 1 }};
  private static long    diffExpectation1  = (9*1) + (1*2) + (1*3) ; // favour larger weights earlier
  private static long    ratioExpectation1 = (9*1) + (1*2) + (1*3) ; 

  private static int[][] testCase2         = new int[][]{{3, 0}, {1,9 }, {1,9 }, {1, 1 }};
  private static long    diffExpectation2  = (1*1) + (1*10) + (1*19); // favour shorter jobs earlier
  private static long    ratioExpectation2 = (1*1) + (1*10) + (1*19); 

  private static int[][] testCase3         = new int[][]{{12, 0},  {8,50}, {74,59}, {31,73}, {45,79}, {24,10}, {41,66}, {93,43}, {88,4}, {28,30}, {41,13}, {4,70}, {10,58}};
  private static long    diffExpectation3  = 68615; // https://www.coursera.org/learn/algorithms-greedy/discussions/weeks/1/threads/b3cKiAwNEeeoaxKMCL9POg
  private static long    ratioExpectation3 = 67247;

  private static int[][] testCase4         = new int[][]{ {2,0}, {10,9}, {2,1}};
  private static long    diffExpectation4  = (10 * 9) + (2*10);  // the difference on these weights is the same so we should use the weight as a tie breaker
  private static long    ratioExpectation4 = ( 2 * 1) + (10*10); // the ratio is not the same though

  private static int[][] testCase5         = new int[][]{ {2,0}, {1<<30, 2}, { 100, 1}};
  private static long    diffExpectation5  = ( 1L << 31 ) + (100 * 3 );
  private static long    ratioExpectation5 = ( 1L << 31 ) + (100 * 3 );


  private static int[][][] testCases         = new int[][][]{ testCase0        , testCase1        , testCase2         , testCase3         , testCase4        , testCase5         }; 
  private static long[]    diffExpectations  = new long[]   { diffExpectation0 , diffExpectation1 , diffExpectation2  , diffExpectation3  , diffExpectation4 , diffExpectation5  };
  private static long[]    ratioExpectations = new long[]   { ratioExpectation0, ratioExpectation1, ratioExpectation2 , ratioExpectation3 , ratioExpectation4, ratioExpectation5 };

  private static boolean           _loud       = false;
  private static int               _times      = 1;

  public static void main( final String[] ARGV ) throws Exception {
    for ( int i = 0 ; i < ARGV.length; i++ ) {
      if( "--loud".equals(ARGV[i])) _loud = true;
      if( "--times".equals( ARGV[ i] )) _times = Integer.valueOf( ARGV[++i]);

      if ("--comparator".equals( ARGV[ i ] )) { 
        switch ( ARGV [++i] ) {
          case "difference": _comparator = DIFFERENCE_COMPARATOR;
                             break;
          default:           _comparator = RATIO_COMPARATOR;
        }
      }

      else if ( "--file".equals( ARGV[i] ) ) {
        int[][] rawInput = Utils.fileToRaggedArrayOfInts( ARGV[ ++i ], " " );
        System.out.format("File %s (%d record(s)) ", ARGV[ i ], rawInput.length );
        long start = System.nanoTime();
        long weightedCompletionTime = schedule( rawInput );
        long duration = System.nanoTime() - start;
        System.out.format( "produced %d in %dμs%n", weightedCompletionTime, ( duration / 1000) );
      }
      else if( "--test".equals(ARGV[i])) {
        boolean allPassed = true;
        for ( int j = testCases.length; --j >= 0 && allPassed ; ) {
          for (int k = _times ; --k >= 0 ; ) {
            _comparator = DIFFERENCE_COMPARATOR ;
            long start = System.nanoTime();
            long weightedCompletionTime = schedule( testCases[j] );
            long duration = System.nanoTime() - start;
            boolean testResult = weightedCompletionTime == diffExpectations[j];
            System.out.format("Run %d: diff  test case %2d produced %10d (expected %10d) in %6dµs, passed? %b %n",k, j, weightedCompletionTime, diffExpectations[j], (duration / 1000), testResult);
            allPassed |= testResult;

            _comparator = RATIO_COMPARATOR;
            start = System.nanoTime();
            weightedCompletionTime = schedule( testCases[j] );
            duration = System.nanoTime() - start;
            testResult = weightedCompletionTime == ratioExpectations[j];
            System.out.format("Run %d: ratio test case %2d produced %10d (expected %10d) in %6dµs, passed? %b %n",k, j, weightedCompletionTime, ratioExpectations[j], (duration / 1000), testResult);
            allPassed &= testResult;
          }
        }
      }
    }
  }


  public static long schedule( int[][] jobs ) { 
    PriorityQueue<int[]> schedule = new PriorityQueue<>( _comparator );
    // add the job to a priority queue
    for ( int i = jobs.length; --i>=1 ; ) schedule.offer( jobs[i] );

    long completionTime = 0 ;
    long weightedSum    = 0 ;
    while ( schedule.size() != 0 ) {
      int[] nextJob = schedule.poll(); 
      if ( _loud ) {
        System.out.print("next job: "); Utils.logInts( nextJob ); 

      }
      completionTime += nextJob[1];
    
      weightedSum += ( nextJob[0] * completionTime );
      if ( _loud ) System.out.format("Completed at %d, weightedSum == %d with score == %f %n", completionTime, weightedSum, _comparator.getScorer().getScore( nextJob ));
      
    }
    return weightedSum;
  }
}
