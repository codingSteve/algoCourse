import java.util.*;
import java.nio.file.*;

public class Utils { 
  public static int  _loopMax = 20;
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

	public static void logObjects( Object... objs ){
		System.out.print('[');
		int i = 0 ; 
		for ( ; i < objs.length && i< _loopMax ; i++ ) { 
			System.out.print(objs[i].toString());
			System.out.print(',');
		}
		if ( i == _loopMax ) System.out.print("…");
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
}
