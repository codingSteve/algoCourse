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
  private static int     expectation0 = 15;

  private static int[][] testCase1    = new int[][]{{3, 0}, {9,1 }, {1,1 }, {1, 1 }};
  private static int     expectation1 = 11; // favour larger weights earlier

  private static int[][] testCase2    = new int[][]{{3, 0}, {1,9 }, {1,9 }, {1, 1 }};
  private static int     expectation2 = 30; // favour shorter jobs earlier

  private static int[][][] testCases    = new int[][][]{ testCase0   , testCase1   , testCase2    }; 
  private static int[]     expectations = new int[]    { expectation0, expectation1, expectation2 };

  public static void main( final String[] ARGV ) throws Exception {
    for ( int i = 0 ; i < ARGV.length; i++ ) {
      if( "--test".equals(ARGV[i])) {
        for ( int j = testCases.length; --j >= 0 ; ) {
          long start = System.nanoTime();
          int avgWeightedCompletionTime = schedule( testCases[j] );
          long duration = System.nanoTime() - start;

          System.out.format("test case %d produced %d (expected %d) in %dµs%n", j, avgWeightedCompletionTime, expectations[j], (duration / 1000));


        }

      }
    }
  }

  public static int schedule( int[][] jobs ) { 
    // generate 'score' for each job, which increases with weight but decreases with length  w / l 
    // add the job to a priority queue
    // drain the queue and calculate the average weighted completion time.
    return -1;
  }
}
