import java.util.*;

public class QuickSort { 

	private static final int[] testInts = new int[]{2148, 9058, 7742, 3153, 6324, 609, 7628, 5469, 7017, 504};

	public static void main(final String[] ARGV ) {

		for ( int i = 0 ; i < ARGV.length ; i++ ) { 
			if ( "--test".equals(ARGV[i]) ) { 
				int[] s = new int[testInts.length];
				int[] t = new int[testInts.length];
				System.arraycopy(testInts, 0, s, 0, testInts.length);
				System.arraycopy(testInts, 0, t, 0, testInts.length);
				Utils.logStrings( "Arrays.sort..." );
				Arrays.sort(s);
				Utils.logInts( s );
				Utils.logStrings( "QuickSort.sort..." );
				sort( t );
				Utils.logInts( t );

				for ( int j = s.length ; --j >= 0 ; ) { 
					if  ( s[j] != t[j] ) { 
						Utils.logStrings(" Failed at position ", ""+ j);
						break;
					}
				}
			}
		}


	}

	public static void sort(final int[] ints ) { 

		final Tuple t = choosePivot(ints);
		for ( int i = ints.length ; -- i >=0;){
			if ( ints[i] < t._1 ) { 

			}
		}


	}

	public static Tuple choosePivot(final int[] ints) { return new Tuple(ints[0], 0); }

	public static class Tuple{ 
		public final int _1;
		public final int _2;
		public Tuple(final int _1, final int _2) {
			this._1 = _1;
			this._2 = _2;
		}
		public String toString(){
			return "(" + _1 +','+_2+')';
		}
	}

}