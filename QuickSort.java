import java.util.*;

public class QuickSort { 

	//private static final int[] testInts = new int[]{2148, 9058, 7742, 3153, 6324, 609, 7628, 5469, 7017, 504};
	private static final int[] testInts = new int[]{3, 5, 2, 4, 1};
  private static int _method = 1;
  private static int[] _expectedComparesForFile = new int[]{-1, 162085, 164123, 138382};

	public static void main(final String[] ARGV ) throws Exception {

		for ( int i = 0 ; i < ARGV.length ; i++ ) { 
			if ( "--test".equals(ARGV[i]) ) { 
        compare(testInts);
			}
      else if ( "--file".equals(ARGV[i]) ) {
        compare(Utils.fileToInts( ARGV[++i]) );
      }
      else if ( "--median".equals( ARGV[i] )) {
        _method = 3; 
        Utils.logInts(testInts);
        Utils.logStrings("2", choosePivot(testInts, 0, 4).toString());
        Utils.logStrings("4", choosePivot(testInts, 1, 3).toString());
        Utils.logStrings("2", choosePivot(testInts, 2, 2).toString());
        Utils.logStrings("3", choosePivot(testInts, 0, 2).toString());
        Utils.logStrings("4", choosePivot(testInts, 0, 3).toString());
        Utils.logStrings("2", choosePivot(testInts, 2, 4).toString());
      }
		}
	}

  public static void compare( int[] input ) { 
		int[] s = new int[input.length];
		System.arraycopy(input, 0, s, 0, input.length);
		Utils.logStrings( "Arrays.sort..." );

    long start = System.nanoTime();
		Arrays.sort(s);
    long end = System.nanoTime();

		Utils.logInts( s );
		Utils.logStrings( "Arrays.sort duration " + ((int)( end - start ) / 1000));

    for ( int m = 1 ; m <=3 ; m++ ) { 
      _method = m;
		  int[] t = new int[input.length];
		  System.arraycopy(input, 0, t, 0, input.length);
      start = System.nanoTime();
		  int comparisons = sort( t );
      end = System.nanoTime();

		  System.out.print("QuickSort.sort with pivot choice = " +m+ "...") ; Utils.logInts( t );
		  Utils.logStrings( "QuickSort.sort duration " + ((int)( end - start ) / 1000), " with comparisons == " + comparisons, "expectedComparisons == " + _expectedComparesForFile[m]) ;

		  for ( int j = s.length ; --j >= 0 ; ) { 
			  if  ( s[j] != t[j] ) { 
				  Utils.logStrings(" Failed at position ", ""+ j);
				  break;
			  }
		  }
    }

  }

	public static int sort(final int[] ints){ return sort(ints, 0, ints.length - 1 ); }

	public static int sort(final int[] ints, int from, int to ) { 
    if ( from >= to || from < 0 || to >= ints.length ) return 0 ;

		final Tuple p = choosePivot(ints, from , to );
    swap(ints, from, p._2);

    int i = from + 1;
    //Utils.logStrings("Starting with from = " + from, "to = "+to, "pivot = " + p);
    //Utils.logInts(ints);

    for (int j = from+1; j <= to; j++ ) {
      //Utils.logInts(i, j);
      if ( ints[j] < p._1 ) {
        swap(ints, j, i);
        i++;
      }

    }
    int pivotTarget = i - 1;
    swap(ints, from, pivotTarget );

    //System.out.print("partitioned about " + p._1 + " : ");
    //Utils.logInts(ints);

    int leftComparisons = sort( ints, from, pivotTarget - 1 );
    int rightComparisons = sort( ints, pivotTarget + 1, to);
    //Utils.logInts(leftComparisons, rightComparisons, (to - from));

    return leftComparisons + rightComparisons + ( to - from );

	}

  public static void swap(final int[] ints, int a, int b) { 
    if ( a == b ) return;
    final int c = ints[a]; ints[a] = ints[b]; ints[b] = c;
  }

	public static Tuple choosePivot( final int[] ints, int from, int to) {
    if (_method == 1 ) return new Tuple(ints[from], from);
    if (_method == 2 ) return new Tuple(ints[to],   to);

    int mi = from + ((to - from )/2);
    int l = ints[from], m = ints[mi], r = ints[to];
    //Utils.logInts(from,mi,to);
    //Utils.logInts(l,m,r);

    if ( m <= l && l <= r ) return new Tuple( l, from );
    if ( r <= l && l <= m ) return new Tuple( l, from );
    if ( l <= m && m <= r ) return new Tuple( m, mi );
    if ( r <= m && m <= l ) return new Tuple( m, mi );
    if ( l <= r && r <= m ) return new Tuple( r, to );
    if ( m <= r && r <= l ) return new Tuple( r, to );
    // deliberate and brutal
    return null;

  }

	public static class Tuple{ 
		public final int _1;
		public final int _2;
		public Tuple(final int _1, final int _2) {
			this._1 = _1;
			this._2 = _2;
		}
		public String toString(){
			return "(val=>" + _1 +", position =>"+_2+')';
		}
	}

}
