
public class Inversions {
  private static String numbers = "100\n1\n101\n2";

  public static void main(final String[] ARGV) { 
  	System.out.println("inversions == " + countInversions(numbers));
  }

  static Tuple countInversions(String numbers) {
  	return countInversions(numbers.split("\n"));
  }

  static Tuple countInversions(String... numbers) {

  	if ( numbers.length <= 1 )
  		return new Tuple(0, numbers);


  	final String[] l = new String[numbers.length / 2 ];
  	final String[] r = new String[numbers.length - (numbers.length / 2 ) ];

  	System.arraycopy(numbers,        0, l, 0, l.length );
  	System.arraycopy(numbers, l.length, r, 0, r.length );

  	Tuple lResult = countInversions(l);
  	Tuple rResult = countInversions(r);

  	Tuple sResult = countSplitInversions(lResult._2, rResult._2);

  	int inversions = lResult._1 + rResult._1 + sResult._1;

  	return new Tuple(inversions, sResult._2);
  }

  static Tuple countSplitInversions( String[] l, String[] r ){
  	String[] merged = new String[l.length + r.length];
  	int inversions = 0;

  	int j = 0;
  	int k = 0;
  	
  	for (int i = 0 ; i < merged.length ; i++ ) { 
  		boolean remainingL = j < l.length;
  		boolean remainingR = k < r.length;

  		if ( remainingL && remainingR ) { 
  			int currentL = Integer.valueOf(l[j]).intValue();
  		 	int currentR = Integer.valueOf(r[k]).intValue();

  		 	if ( currentL <= currentR ) {
  				merged[i] = l[j++];
  			}
  			else {
  				merged[i] = r[k++];
  				inversions += l.length - j;
  			}
  		}
  		else if (!remainingL) { 
  				merged[i] = r[k++];
  		}
  		else if ( !remainingR ) {
  			merged[i] = l[j++];
  		}
	  	
	  	// Utils.logStrings(merged);
	}
  	return new Tuple(inversions, merged);
  }

  static class Tuple { 
  	final public int _1;
  	final public String[] _2;
  	Tuple(int elementOne, String[] elementTwo) {
  		_1 = elementOne;
  		_2 = elementTwo;
  	}
  	
  	public String toString() { 
  		return "" + _1; 
  	}
  }
}