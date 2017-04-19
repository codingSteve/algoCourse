public class Scratch {
	/** Routine to count the different bits between two integers.
	*
    * This is taken from Wikipedia, it's shorter and will execute the loop
    * fewer times.
    * 
    * Hamming distance: {@link https://en.wikipedia.org/wiki/Hamming_distance}
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
    * This is a simple implementation which is longer and will execute the main 
    * loop more times than the version above
    * 
    * Hamming distance: {@link https://en.wikipedia.org/wiki/Hamming_distance}
    */
    @Deprecated
    public static int hamDistanceS( final int x, final int y ) {
        int z = (x ^ y);
        int d = 0 ;
        while ( z != 0 ) {
            d += ( z & 1 );
            z >>= 1;
        }
        return d;
    }
}