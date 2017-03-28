public class Median {


  /**
  The goal of this problem is to implement the "Median Maintenance" algorithm. 
  
  The text file contains a list of the integers from 1 to 10000 in unsorted order;
  you should treat this as a stream of numbers, arriving one by one. Letting xi
  denote the ith number of the file, the kth median mk is defined as the median of 
  the numbers x1,…,xk. (So, if k is odd, then mk is ((k+1)/2)th smallest number
  among x1,…,xk; if k is even, then mk is the (k/2)th smallest number among x1,…,xk.)

  In the box below you should type the sum of these 10000 medians, modulo 10000
  (i.e., only the last 4 digits).
  That is, you should compute (m1+m2+m3+⋯+m10000)%10000.

  OPTIONAL EXERCISE: Compare the performance achieved by heap-based and search-tree-based implementations of the algorithm.
  **/
  public static void main ( String[] ARGV ) { 
  }


  public static int[] median( int[] input ) { 
    for (int i = 0 ; i < input.length ; i ++ ) { 
      // if intput[i] < max of the lower heap add to lower heap
      // if intput[i] > min of the upper heap add to upper heap
      // if heaps are imbalanced  rebalance heaps
      // if heaps have equal size the median is i/2 order stat
      // if heaps have unequal size then the median is the (i+1)/2 order stat
    }
  }



}
