import java.util.*;
import java.nio.file.*;

public class Utils { 
  public static int  _loopMax = 20;
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
  public static int[] fileToInts( final String fileName ) throws Exception {
        final Path p = FileSystems.getDefault().getPath(fileName);
        final List<String> numbers = Files.readAllLines(p);
        return stringsToInts(numbers);
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
}
