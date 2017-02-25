
import java.nio.file.*;
import java.util.*;

public class Inversions {
  private static Tuple test = new Tuple(3, Utils.stringsToIntegers("100\n1\n101\n2".split("\n")));

  public static void main(final String[] ARGV) throws Exception { 

    if ("--file".equals(ARGV[0])) {
      for (int i = 1 ; i < ARGV.length ; i ++ ){
        String fileName = ARGV[i];
        final Path p = FileSystems.getDefault().getPath(fileName);
        final List<String> numbers = Files.readAllLines(p);
        final Integer[] numbersArray = Utils.stringsToIntegers(numbers.toArray(new String[]{}));

        long start = System.nanoTime();

        System.out.println("Inversions from " + fileName + " == " + countInversions(numbersArray) + " in " + (int) ( (System.nanoTime() - start) / 10E6) + "ms");

      }
    }
    else if ("--test".equals(ARGV[0]))
  	   System.out.println("inversions for test case correct? " + ( test._1 == countInversions(test._2)._1));
     else {
      System.out.println("Inversions from ARGV == " + countInversions( Utils.stringsToIntegers(ARGV)));
     }
  }

  static Tuple countInversions(String numbers) {
  	return countInversions( Utils.stringsToIntegers(numbers.split("\n")));
  }

  static Tuple countInversions( Integer[] numbers ) {

  	if ( numbers.length <= 1 )
  		return new Tuple(0, numbers);


  	final Integer[] l = new Integer[numbers.length / 2 ];
  	final Integer[] r = new Integer[numbers.length - (numbers.length / 2 ) ];

  	System.arraycopy(numbers,        0, l, 0, l.length );
  	System.arraycopy(numbers, l.length, r, 0, r.length );

  	Tuple lResult = countInversions(l);
  	Tuple rResult = countInversions(r);

  	Tuple sResult = countSplitInversions(lResult._2, rResult._2);

  	long inversions = lResult._1 + rResult._1 + sResult._1;

  	return new Tuple(inversions, sResult._2);
  }

  static Tuple countSplitInversions( Integer[] l, Integer[] r ){
  	Integer[] merged = new Integer[l.length + r.length];
  	long inversions = 0;
  	int i=0, j=0 , k=0 ;
  	
  	while( i < merged.length ) { 
  		if ( j < l.length && k < r.length ) { 

        Integer cl = l[j] ;
        Integer cr = r[k] ;
  			
  		 	if ( cl.compareTo( cr ) <= 0 ) {
  				merged[i++] = cl;
          j++;
  			}
  			else {
  				merged[i++] = cr; 
          k++;
  				inversions += l.length - j;
  			}
  		}
      else {
        for ( ; j < l.length; j++ ) merged[i++] = l[j];
        for ( ; k < r.length; k++ ) merged[i++] = r[k];
      }
	  }
  	
    return new Tuple(inversions, merged);
  }

  static class Tuple { 
  	final public long _1;
  	final public Integer[] _2;
  	Tuple(long elementOne, Integer[] elementTwo) {
  		_1 = elementOne;
  		_2 = elementTwo;
  	}
  	
  	public String toString() { 
  		return "" + _1; 
  	}
  }
}
