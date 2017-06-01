import java.util.*;

public class NearestNeighbourTSP {

	private static double DELTA = 0.05d;
	private static boolean _loud = false;

	public static double[][][] testCases    = TSP.testCases;
    public static double[]     expectations = TSP.expectations;

    public static void main(String[] args) throws Exception {
    	int times = 1;
    	for ( int i = 0, l = args.length; i < l; i++ ) { 


    		if ("--loud".equals(args[i])) _loud = true;
    		else if ( "--quiet".equals(args[i])) _loud = false;
    		else if ( "--times".equals(args[i])) times = Integer.valueOf( args[++i]).intValue();
    		else if ( "--test".equals(args[i])) { 
    			TESTING:
    			for ( int j = testCases.length; --j>=0 ; ){
    				double[][] rawInput = testCases[j];

    				for( int k = times; --k>=0; ) { 

    					long start = System.nanoTime();
    					double actual = nn( rawInput );
    					long duration = System.nanoTime() - start;

    					double expected = expectations[j];
    					System.out.format("Run %2d of test case %d produced %6.3f (expected %6.3f) in %10dµs%n",
    						k, j, actual, expected, (duration/1000));
    					// if ( Math.abs( expected - actual  ) > DELTA ) break TESTING;

    				}
    			}
    		}
    		else if ("--file".equals(args[i])){
    			final String fileName = args[++i];
    			double[][] rawInput = Utils.fileToRaggedArrayOfDoubles( fileName, " " );
				for( int j = times; --j>=0; ) { 

					long start = System.nanoTime();
					double actual = nn( rawInput );
					long duration = System.nanoTime() - start;

					System.out.format("Run %2d of file %s produced %6.3f in %10dms%n",
						j, fileName, actual, (duration/1E06));
				}



    		}
    	}
    }

    public static double nn( double[][] rawInput ){
    	int cities = (int) rawInput[0][0];
    	City[] map = new City[cities+1];

    	for ( int i = rawInput.length ; --i >= 1 ; ) map[i] = new City( i, rawInput[i][0], rawInput[i][1]);		

    	double tour = 0.00D;
    	City previous = map[1];
    	previous._explored = true;

    	City next = map[1];

    	for ( int i = cities + 1 ; --i >= 2;  ){
    		if ( map[i]._explored ) continue;

    		double minDistance = Double.POSITIVE_INFINITY;

    		for ( int j = cities + 1; --j >= 2; ){
    			if ( map[j]._explored ) continue;
    			if ( i == j ) continue;
    			City candidate = map[j];

    			double distance = Math.hypot( Math.abs(previous._x - candidate._x), Math.abs(previous._y - candidate._y) );
    			if ( minDistance > distance ) {
    				next = candidate;
    				minDistance = distance;
    			}

    		}
    		next._explored = true;
    		    		tour += minDistance;

    		System.out.println("Adding " + next._i + " to tour (length == " + tour + ")");
    		previous = next;

    	}

    	return tour + Math.hypot( Math.abs(previous._x - map[1]._x), Math.abs(previous._y - map[1]._y));
    }


    private static class City{
    	final int    _i;
    	final double _x;
    	final double _y;
    	boolean _explored = false;

    	private final PriorityQueue<City> neighbours;

    	City( int i, double x, double y ) { 
    		_i = i; _x = x; _y = y; 
    		neighbours = new PriorityQueue<City>(
    			new Comparator<City>() { 
    				public int compare(City a, City b) { 
    				return (int) (Math.hypot( _x - a._x, _y - a._y) - Math.hypot( _x - b._x, _y - b._y));
    				}
    			}
    		);

    	}

    	void addNeighbour( City c ) { neighbours.add( c ); }

    	City getNearestNeighbour() { 
    		while( ! neighbours.isEmpty() ) {
    			City neighbour = neighbours.poll();
    			if ( ! neighbour._explored && neighbour != this ) return neighbour;
    		}
    		return null;
    	}
    }

}