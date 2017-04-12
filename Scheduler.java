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


/*
Inputs will look like this
10000  // number of jobs in the list
8 50   // weight_of_job_1, length_of_job_1
74 59  // weight_of_job_2, length_of_job_2
… 
*/
  private static int[][] testCase0    = new int[][]{{3, 0}, {3,1 }, {2,2 }, {1, 3 }};
  private static int     expectation0 = (3*1) + (2*3) + (1*6);

  private static int[][] testCase1    = new int[][]{{3, 0}, {9,1 }, {1,1 }, {1, 1 }};
  private static int     expectation1 = (9*1) + (1*2) + (1*3) ; // favour larger weights earlier

  private static int[][] testCase2    = new int[][]{{3, 0}, {1,9 }, {1,9 }, {1, 1 }};
  private static int     expectation2 = (1*1) + (1*10) + (1*19); // favour shorter jobs earlier

  private static int[][][] testCases    = new int[][][]{ testCase0   , testCase1   , testCase2    }; 
  private static int[]     expectations = new int[]    { expectation0, expectation1, expectation2 };

  private static boolean _loud = false;

  public static void main( final String[] ARGV ) throws Exception {
    for ( int i = 0 ; i < ARGV.length; i++ ) {
      if( "--loud".equals(ARGV[i++])) _loud = true;

      
      if ( "--file".equals( ARGV[i] ) ) {
        int[][] rawInput = Utils.fileToRaggedArrayOfInts( ARGV[ ++i ] );
        long start = System.nanoTime();
        int weightedCompletionTime = schedule( rawInput );
        long duration = System.nanoTime() - start;
        System.out.format( "File %s produced %d in %dμs%n", ARGV[ i ], weightedCompletionTime, ( duration / 1000) );
        
      }
      
      else if( "--test".equals(ARGV[i])) {
        for ( int j = testCases.length; --j >= 0 ; ) {
          long start = System.nanoTime();
          int avgWeightedCompletionTime = schedule( testCases[j] );
          long duration = System.nanoTime() - start;

          System.out.format("test case %d produced %d (expected %d) in %dµs%n", j, avgWeightedCompletionTime, expectations[j], (duration / 1000));


        }

      }
    }
  }

  /** 
   * This comparator generates a 'score' for each job, which
   * increases with weight but decreases with length  w / l.
   * 
   * Here we're using the difference, but we want the maximum
   * difference at the head of the queue. So we're constructing a
   * reveres sort.
   *
   */
  private static Comparator<int[]> differenceComparator = new Comparator<int[]>() { 
    @Override
    public int compare( int[] a, int[] b) {
      

      if ( a != null && b != null ) { 
        double aScore = 0.00d;
        double bScore = 0.00d;
        int[] arrayA = (int[]) a;
        int[] arrayB = (int[]) b;

        int score = arrayB[0] - arrayB[1] - arrayA[0] + arrayA[1];

        if ( score == 0 ) {
          return arrayA[0] - arrayB[0];
        }
        return score;
      }
      throw new IllegalArgumentException( "Exepected  int[]" );
    }

  };

  public static int schedule( int[][] jobs ) { 
    PriorityQueue<int[]> schedule = new PriorityQueue<>( differenceComparator );
    // add the job to a priority queue
    for ( int i = jobs.length; --i>=1 ; ) schedule.offer( jobs[i] );

    int completionTime = 0 ;
    int weightedSum    = 0 ;
    while ( schedule.size() != 0 ) {
      int[] nextJob = schedule.poll(); 
      if ( _loud ) {System.out.print("next job: "); Utils.logInts( nextJob ); }
      completionTime += nextJob[1];
    //   calculate the average weighted completion time.
      weightedSum += ( nextJob[0] * completionTime );
      if ( _loud ) System.out.format("Completed at %d, weightedSum == %d%n", completionTime, weightedSum);
      
    }
    return weightedSum;
  }
}
