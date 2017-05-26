import java.util.*;
import java.nio.file.*;

public class Utils { 
  public static int  _loopMax = 20;

  /** Routine to count the different bits between two integers.
	*
    * This is taken from Wikipedia, it's shorter and will execute the loop
    * fewer times.
    * 
    * Hamming distance:  https://en.wikipedia.org/wiki/Hamming_distance
    */
  public static int hamDistance( final int x, final int y ) {
      int z = (x ^ y);
      int d = 0 ;
      while ( z != 0 ) {
          d++;
          z &= z - 1;
      }
      return d;
  }

  /** Routine to count the different bits between two integers.
  *
  * This is a naive implementation which is longer and will execute the main 
  * loop more times than the version above. 
  * 
  * Hamming distance: https://en.wikipedia.org/wiki/Hamming_distance
  */
  @Deprecated
  private static int hamDistanceS( final int x, final int y ) {
      int z = (x ^ y);
      int d = 0 ;
      while ( z != 0 ) {
          d += ( z & 1 );
          z >>= 1;
      }
      return d;
  }

  public static int bitsToInt( final int[] bits ){
  	int result = 0 ; 
  	for (int i = 0 ; i <bits.length ; i ++ ) { 
  		result <<= 1;
  		result += bits[i];
  	}
  	return result;
  }

  public static int[] intToBits( int n ) {
      int[] r = new int[8];
      for ( int i = 8 ; --i>= 0 ; ) r[i] = (((1<<i)  & n ) != 0 ) ? 1 : 0;
      return r;
  }

  public static int[][] intsToBits( int[] ns) {
      int[][] r = new int[ns.length][8];
      for ( int i = ns.length; --i>=0 ; ) r[i] = intToBits(ns[i]);
      return r;
  }


  public static void logRaggedDoubles( double[]... twoDimensionalArray ) {
    int i = 0;

    System.out.println('[');
    for ( ; i < twoDimensionalArray.length && i < _loopMax ; i++ ) {
      logDoubles(twoDimensionalArray[i]);
    }
    if ( i == _loopMax ) System.out.print( "... ");
    System.out.println(']');
  }

  public static void logRaggedInts( int[]... twoDimensionalArray ) {
    int i = 0;

    System.out.println('[');
    for ( ; i < twoDimensionalArray.length && i < _loopMax ; i++ ) {
      logInts(twoDimensionalArray[i]);
    }
    if ( i == _loopMax ) System.out.print( "... ");
    System.out.println(']');
  }
	public static void logInts(int... ints){
    System.out.print('[');
    int i  =0 ;
    for ( ; i < ints.length && i < _loopMax ; i++ ) {
      System.out.print(ints[i]);
      System.out.print(',');
    }
    if ( i == _loopMax ) System.out.print( "... ");
    System.out.println(']');
  }
public static void logDoubles(double... doubles){
		System.out.print('[');
    int i  =0 ;
		for ( ; i < doubles.length && i < _loopMax ; i++ ) {
			System.out.format("%6.3f,",doubles[i]);
		}
    if ( i == _loopMax ) System.out.print( "... ");
		System.out.println(']');
	}

	public static void logObjects( Collection c ) { 
    System.out.print('[');
    int i = 0 ;
    if( c != null ) {
      for ( Object o : c ) { 
        if ( i++ < _loopMax ) {
          System.out.print( o.toString() );
          System.out.print(',');
        }
        else { System.out.print("…");  break ; } 
      }
    }
    System.out.println(']');
  }
	public static void logObjects( Object... objs ){
		System.out.print('[');
		int i = 0 ; 
    if ( objs != null ) { 
		  for ( ; i < objs.length && i< _loopMax ; i++ ) { 
			  if ( objs[i] != null ) System.out.print(objs[i].toString());
			  System.out.print(',');
		  }
		  if ( i == _loopMax ) System.out.print("…");
    }
		System.out.println(']');
	}

	public static void logIntegers(Integer... ints){
		System.out.print('[');
		for (Integer i : ints) {
			System.out.print(i);
			System.out.print(',');
		}
		System.out.println(']');
	}

	public static void logStrings(String... strings) { 
		System.out.print('[');
		
    int i = 0;
		for(; i < strings.length && i < _loopMax ; i++ ) { 
			System.out.print(strings[i]);
			System.out.print(',');
		}
    if ( i == _loopMax ) System.out.print( "... ");
		System.out.println(']');
	}

  public static int[][] fileToRaggedArrayOfInts( final String fileName ) throws Exception { return fileToRaggedArrayOfInts(fileName, "\t"); }

  public static int[][] fileToRaggedArrayOfInts( final String fileName, final String delimiter ) throws Exception {
        final Path p = FileSystems.getDefault().getPath(fileName);
        final String[] records = fileToStringArray( fileName );

        int[][] rows = new int[records.length][];
        for ( int i = records.length; --i >= 0 ; ) { rows[i] = stringsToInts( records[i].split(delimiter) ); }
        return rows;
  }



  public static String[] fileToStringArray( final String fileName ) throws Exception {
  	final Path p = FileSystems.getDefault().getPath(fileName);
    final List<String> records = Files.readAllLines(p);
    return records.toArray( new String[]{} );
  }

  public static int[] fileToInts( final String fileName ) throws Exception {
        final Path p = FileSystems.getDefault().getPath(fileName);
        final List<String> numbers = Files.readAllLines(p);
        return stringsToInts(numbers);
  }

  public static Integer[] fileToIntegers( final String fileName ) throws Exception {
        final Path p = FileSystems.getDefault().getPath(fileName);
        final List<String> numbers = Files.readAllLines(p);
        return stringsToIntegers(numbers);
  }

  public static Long[] fileToReferenceLongs( final String fileName ) throws Exception {
        final Path p = FileSystems.getDefault().getPath(fileName);
        final List<String> numbers = Files.readAllLines(p);
        return stringsToReferenceLongs(numbers);
  }

	public static int[] stringsToInts( final String[] strings ){
		int[] ints = new int[strings.length];
		for( int i = strings.length; --i >=0;) ints[i] = Integer.valueOf(strings[i]).intValue();
		return ints;
	}

	public static int[] stringsToInts( final List<String> strings ){
		int[] ints = new int[strings.size()];
		for( int i = strings.size(); --i >=0;) ints[i] = Integer.valueOf(strings.get(i)).intValue();
		return ints;
	}

	public static Integer[] stringsToIntegers( final String[] strings ){
		Integer[] integers = new Integer[strings.length];
		for( int i = strings.length; --i >=0;) integers[i] = Integer.valueOf(strings[i]);
		return integers;
	}
	public static Integer[] stringsToIntegers( final List<String> strings ){
		Integer[] integers = new Integer[strings.size()];
		for( int i = strings.size(); --i >=0;) integers[i] = Integer.valueOf(strings.get(i));
		return integers;
	}
	public static Long[] stringsToReferenceLongs( final List<String> strings ){
		Long[] longs = new Long[strings.size()];
		for( int i = strings.size(); --i >=0;) longs[i] = Long.valueOf(strings.get(i));
		return longs;
	}
}
