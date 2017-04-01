import java.util.*;

public class Median {

  private static boolean _loud  = false;
  private static boolean _quiet = true;

  private static int     answerSize = 10000;

  private static Integer[]   testCase0    = new Integer[]{1, 666, 10, 667, 100, 2, 3};
  private static Integer     expectation0 = 142;
  private static Integer[]   testCase1    = new Integer[]{6331, 2793, 1640, 9290, 225, 625, 6195, 2303, 5685, 1354};
  private static Integer     expectation1 = 9335;
  private static Integer[]   testCase2    = new Integer[]{1,4,9,5,2,3,6,7,8}; 
  private static Integer     expectation2 = 30;
  private static Integer[][] testCases    = new Integer[][] { testCase0   , testCase1   , testCase2    };
  private static Integer[]   expectations = new Integer[]   { expectation0, expectation1, expectation2 };

  /**
   * The goal of this problem is to implement the "Median Maintenance" algorithm. 
   * 
   * The text file contains a list of the integers from 1 to
   * 10000 in unsorted order; you should treat this as a stream
   * of numbers, arriving one by one. Letting xi denote the ith
   * number of the file, the kth median mk is defined as the
   * median of the numbers x1,…,xk. (So, if k is odd, then mk is
   * ((k+1)/2)th smallest number among x1,…,xk; if k is even,
   * then mk is the (k/2)th smallest number among x1,…,xk.)
   * 
   * In the box below you should type the sum of these 10000
   * medians, modulo 10000 (i.e., only the last 4 digits).
   *
   * That is, you should compute (m1+m2+m3+⋯+m10000)%10000.

   * OPTIONAL EXERCISE: Compare the performance achieved by
   * heap-based and search-tree-based implementations of the
   * algorithm.
   **/
  public static void main ( String[] ARGV ) throws Exception { 

    int times = 1; 
    for( int i = 0 ; i < ARGV.length ; i++ ) { 
      if ( "--silent".equals(ARGV[i]) ) { _loud = false ; _quiet = false ; }
      if ( "--quiet".equals(ARGV[i])  ) { _loud = false ; _quiet = true ;  }
      if ( "--loud".equals(ARGV[i])   ) { _loud = true  ; _quiet = true ;  }

      if ( "--times".equals(ARGV[i])  ) times = Integer.valueOf( ARGV[ ++i ] ).intValue();

      if ( "--test".equals( ARGV[i] )) {
        boolean passed = true;
        for ( int j = testCases.length; --j >= 0 ; ) { 
          for ( int k = times; passed && --k >= 0 ; ) passed &= timeAndCheck(testCases[j], expectations[j] );
        }
      }

      if ( "--file".equals( ARGV[i] ) ) { 
        Integer[] input = Utils.fileToIntegers( ARGV[++i] );
        for ( int k = times ; --k >= 0 ; ) { 
          long start = System.nanoTime();
          int[] medians = median( input );
          long duration = System.nanoTime() - start;

          int result = sumAndMod( medians , answerSize  );
          System.out.format("median produced an answer of %d from %s in %d µs%n",result, ARGV[i], duration/1000);
        }
      }
    }
  }

  private static boolean timeAndCheck( Integer[] input, int expectation ) { 
    long start = System.nanoTime();
    int[] medians = median( input );
    long duration = System.nanoTime() - start;
    int result = sumAndMod( medians , answerSize  );
    System.out.format("median produced %d (expected %d) in %d µs%n",result, expectation, duration/1000);
    return result == expectation;
  }

  private static int sumAndMod( int[] input, int modValue ) {
    int sum = 0;
    for ( int i = input.length ; --i >= 0 ; )  sum += input[i];
    return ( sum % modValue );
  }

  private static Comparator<Integer> reversedIntegerComparator = new Comparator<Integer>() { 
    @Override
    public int compare( Integer a, Integer b ) { 
      return  ( b.compareTo( a ) );
    }
  };


  public static int[] median( Integer[] input ) { 
    IntHeap lower = new MaxIntHeap( input.length /2 );
    IntHeap upper = new IntHeap( input.length /2 );

    int[] medians = new int[ input.length ];

    lower.offer( input[0] );
    medians[0] = input[0];

    for (int i = 1 ; i < input.length ; i ++ ) { 

      if ( input[i] < lower.peek() ) { // less than the highest "low" number
        lower.offer( input[i] );
      }
      else if ( upper.isEmpty() ||  input[i] > upper.peek() ) { // greater than the lowest "high" number
        upper.offer(input[i]);
      }
      else { // the new number falls between the two heaps.
        lower.offer( input[i] );
      }

      if ( _loud ) System.out.format("%d added : %nlower: %s%nupper: %s%n", input[i], lower.toString(), upper.toString());

      // rebalance heaps with a preference for having more in the
      // lower heap if there's an odd number. This means we'll
      // always be able to take the median as the max of the
      // lower numbers.

      if ( lower.size() - upper.size() >= 1 ) upper.offer( lower.poll() ); 
      if ( upper.size() - lower.size() >= 1 ) lower.offer( upper.poll() ); 

      if ( _loud ) System.out.format("%d added and balanced: %nlower: %s%nupper: %s%n", input[i], lower.toString(), upper.toString());

      medians[ i ] = lower.peek();
    }
    return medians;
  }



}
