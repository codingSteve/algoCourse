import java.util.*;

public class Huffman {
  
  private static int[] testCase0    = new int[]{1, 1}; // a single symbol
  private static int[] expectation0 = new int[]{0, 0};

  private static int[] testCase1    = new int[]{2, 1, 1}; // two symbols with equal weight
  private static int[] expectation1 = new int[]{1, 1};

  private static int[] testCase2    = new int[]{5, 5, 25, 32, 20, 18, };
  private static int[] expectation2 = new int[]{2, 3};
  // https://www.coursera.org/learn/algorithms-greedy/discussions/weeks/3/threads/FQUCiQFfEeeR2AqDeyCCgA

  private static int[] testCase3    = new int[]{6, 3, 2, 6, 8, 2, 6};
  private static int[] expectation3 = new int[]{2, 4}; // example from the lectures

  private static int[][] testCases     = new int[][]{ testCase0   , testCase1   , testCase2   , testCase3   ,  };
  private static int[][] expectations  = new int[][]{ expectation0, expectation1, expectation2, expectation3,  };

  private static boolean _loud  = false;

  public static void main(String[] ARGV) throws Exception {
    int times = 1;
    for ( int i = 0 ; i < ARGV.length ; i++ ) {
      if      ( "--loud".equals( ARGV[i]) ) _loud = true;
      else if ( "--quiet".equals(ARGV[i]) ) _loud = false;
      else if ( "--times".equals(ARGV[i]) ) times = Integer.valueOf( ARGV[++i] );
      else if ( "--test".equals(ARGV[i])) {
        boolean allTestsPassed = true;
        for ( int j = 0 ; j < testCases.length && allTestsPassed ; j++ ){ 
          for( int k = times ; --k >= 0 && allTestsPassed ; ) {
            long start    = System.nanoTime();
            Tree result   = encode( testCases[j] );
            long duration = System.nanoTime() - start;

            List<String> paths = result.getPaths();

            int minLength = Integer.MAX_VALUE;
            int maxLength = Integer.MIN_VALUE;
            for( String s : paths ) { 
              if ( s.length() > maxLength ) maxLength = s.length(); 
              if ( s.length() < minLength ) minLength = s.length();
            }

            allTestsPassed &= ( minLength == expectations[j][0]) && (maxLength == expectations[j][1]); 

            System.out.format("Run %4d; Test case %d produced min codeword length %d and max codeword length %d expected (%d and %d) in %6dµs%n",
              k, j, minLength, maxLength, expectations[j][0], expectations[j][1], ( duration / 1000) 
              );
          }
        }
      }
    }
  }


  private static Tree encode( int[] raw ){
    PriorityQueue<Tree> subTrees = new PriorityQueue<>( weightComparator );
    for ( int i = raw.length ; --i >= 1 ; ){
      subTrees.offer( new Tree( raw[i], i ));
    }
    if ( _loud ) System.out.format("Raw trees created as : %s %n", subTrees);

    
    while( subTrees.size() >= 2 ){
      Tree t1 = subTrees.poll();  
      Tree t2 = subTrees.poll();

      subTrees.offer( new Tree( t1, t2 ));
    }

    return subTrees.poll();
  }


  private static Comparator<Tree> weightComparator = new Comparator<Tree>(){
    @Override
    public int compare( Tree a, Tree b ){

      int weightA = a.getWeight();
      int weightB = b.getWeight();
      int result  = weightA - weightB;
      if ( result ==0 ) return a._value - b._value; // trouble here for trees with the same weight 
      return result;  
    }
  };

  private static class Tree {
    int _weight;
    int _value;
    Tree _left;
    Tree _right;
    
    Tree( Tree l, Tree r ) {_left = l; _right = r ; }
    Tree( int w,  int v  ) {_weight = w ; _value = v ; }

    int getWeight() { 
      if ( _left != null )
        return _left.getWeight() + _right.getWeight();
            
      return _weight;
    }

    List<String> getPaths() {return getPaths(""); }

    protected List<String> getPaths( String path ) { 
      if ( _left == null ) {
        LinkedList<String> paths = new LinkedList<>();
        paths.add(path); 
        return paths;
      }

      List<String> paths = _left.getPaths( path + "0");
      paths.addAll(  _right.getPaths(path + "1"));
      return paths;
    }

    @Override
    public String toString(){
      return "{w:" + _weight + ", v:" + _value + "}";
    }

  }




}