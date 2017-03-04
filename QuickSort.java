import java.util.*;

public class QuickSort { 

	private static final int[] testInts = new int[]{2148, 9058, 7742, 3153, 6324, 609, 7628, 5469, 7017, 504};

	public static void main(final String[] ARGV ) throws Exception {

		for ( int i = 0 ; i < ARGV.length ; i++ ) { 
			if ( "--test".equals(ARGV[i]) ) { 
        compare(testInts);
			}
      else if ( "--file".equals(ARGV[i]) ) {
        compare(Utils.fileToInts( ARGV[++i]) );
      }
		}


	}

  public static void compare( int[] input ) { 
		int[] s = new int[input.length];
		int[] t = new int[input.length];
		System.arraycopy(input, 0, s, 0, input.length);
		System.arraycopy(input, 0, t, 0, input.length);
		Utils.logStrings( "Arrays.sort..." );

    long start = System.nanoTime();
		Arrays.sort(s);
    long end = System.nanoTime();

		Utils.logInts( s );
		Utils.logStrings( "Arrays.sort duration " + ((int)( end - start ) / 1000),  "\n", "QuickSort.sort..." );

    start = System.nanoTime();
		sort( t );
    end = System.nanoTime();

		Utils.logInts( t );
		Utils.logStrings( "QuickSort.sort duration " + ((int)( end - start ) / 1000));

		for ( int j = s.length ; --j >= 0 ; ) { 
			if  ( s[j] != t[j] ) { 
				Utils.logStrings(" Failed at position ", ""+ j);
				break;
			}
		}
  }

	public static void sort(final int[] ints){ sort(ints, 0, ints.length - 1 ); }

	public static void sort(final int[] ints, int from, int to ) { 
    if ( from >= to || from < 0 || to >= ints.length ) return;

		final Tuple p = choosePivot(ints, from , to );
    swap(ints, from, p._2);

    int i = from + 1;
    //Utils.logStrings("Starting with from = " + from, "to = "+to, "pivot = " + p);
    //Utils.logInts(ints);

    for (int j = from+1; j <= to; j++ ) {
      //Utils.logInts(i, j);
      if ( ints[j] < p._1 ) {
        swap(ints, j, i);
        //Utils.logInts( ints );
        i++;
      }

    }
    int pivotTarget = i - 1;
    swap(ints, from, pivotTarget );

    //System.out.print("partitioned about " + p._1 + " : ");
    //Utils.logInts(ints);

    sort( ints, from, pivotTarget - 1 );
    sort( ints, pivotTarget + 1, to);

	}

  public static void swap(final int[] ints, int a, int b) { 
    if ( a == b ) return;
    final int c = ints[a]; ints[a] = ints[b]; ints[b] = c;
  }

	public static Tuple choosePivot(final int[] ints, int from, int to) {
    return new Tuple(ints[from], from); 
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
