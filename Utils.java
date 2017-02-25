public class Utils { 
	public static void logInts(int... ints){
		System.out.print('[');
		for (int i : ints) {
			System.out.print(i);
			System.out.print(',');
		}
		System.out.println(']');
	}

	public static void logStrings(String... strings) { 
		System.out.print('[');
		
		for(String s : strings) { 
			System.out.print(s);
			System.out.print(',');
		}
		System.out.println(']');
	}
	public static Integer[] stringsToIntegers( final String[] strings ){
		Integer[] integers = new Integer[strings.length];
		for( int i = strings.length; --i >=0;) integers[i] = Integer.valueOf(strings[i]);
		return integers;
	}
}