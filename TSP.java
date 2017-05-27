import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TSP {

    private static boolean _loud = false;
    private static double[][] testCase0 = new double[][]{{2.00d}, {1.00d, 1.00d}, {2.00d, 1.00d}, };
    private static double expectation0  = 2.00d;

    private static double[][] testCase1 = new double[][]{{4.00d}, {1.00d, 1.00d}, {2.00d, 1.00d}, {2.00d, 2.00d}, {1.00d, 2.00d}};
    private static double expectation1  = 4.00d;

    private static double[][] testCase2 = new double[][]{{8.00d}, 
	              {1.00d, 1.00d}, {2.00d, 1.00d}, {3.00d, 1.00d}, {4.00d, 1.00d},
	              {1.00d, 2.00d}, {2.00d, 2.00d}, {3.00d, 2.00d}, {4.00d, 2.00d},
								};
    private static double expectation2  = 8.00d;

    private static double[][] testCase3 = new double[][]{{5.00d}, {1.00d, 1.00d}, {2.00d, 1.00d}, {2.00d, 2.00d}, {2.00d, 1.00d}, {1.50d, 1.50d} };
    private static double expectation3  = 4.41d;

    private static double[][][] testCases    = new double[][][]{testCase0,    testCase1,    testCase3,   };
    private static double[]     expectations = new double[]{    expectation0, expectation1, expectation3,};

    public static void main(String[] ARGV) throws Exception {
        int times = 1;
        for (int i = 0, l = ARGV.length; i < l; i++) {
            if ("--loud".equals(ARGV[i])) _loud = true;
            else if ("--quiet".equals(ARGV[i])) _loud = false;
            else if ("--times".equals(ARGV[i])) times = Integer.valueOf(ARGV[++i]);
            else if ("--file".equals(ARGV[i])) {
                for ( int j = times; --j >=0 ; ) {

                    String fileName = ARGV[++i];
                    double[][] rawInput = Utils.fileToRaggedArrayOfDoubles(fileName, " ");
                    long start = System.nanoTime();
                    double actual = tsp(rawInput);
                    long duration = System.nanoTime() - start;

                    System.out.format("Run %3d of file %s produced tour length %6.6f in %6dμs%n",
                             j, fileName, actual, (duration / 1000));
                }
            }
            else if ("--test".equals(ARGV[i])) {
                TESTS:
                for (int j = testCases.length; --j >= 0; ) {
                    double[][] rawInput = testCases[j];
                    double expected = expectations[j];

                    for (int k = times; --k >= 0; ) {
                        long start = System.nanoTime();
                        double actual = tsp(rawInput);
                        long duration = System.nanoTime() - start;

                        System.out.format("Run %3d of testcase %d produced tour length %6.6f (expected %6.6f) in %6dμs%n",
                                k, j, actual, expected, (duration / 1000));
                        if (expected != actual) break TESTS;

                    }
                }
            }
        }
    }

    public static double tsp(double[][] rawInput) {


        final double cities = rawInput[0][0];
        final int arrayLength = (int) Math.pow(2.00d, cities-1);
//        double[][] A = new double[arrayLength][(int) cities+1];
//
//        for ( int s = arrayLength ; --s>=0 ; ) Arrays.fill( A[s], Double.POSITIVE_INFINITY); //A[s][1] = Double.POSITIVE_INFINITY;
//        A[1][1] = 0;

        Map<Integer, double[]> A = new HashMap<>( arrayLength /2);

        double[] homeRow = new double[(int) cities +1 ];
        Arrays.fill( homeRow, Double.POSITIVE_INFINITY);
        homeRow[1]=0;

        A.put( 1, homeRow );

        double[][] C = new double[(int)cities+1][(int)cities+1];
        for (int i = (int) cities+1; --i >=1 ; ) {
            for ( int j = (int) cities+1 ; --j>=1 ;){
                final double[] cityI = rawInput[i];
                final double[] cityJ = rawInput[j];
                C[i][j]= Math.pow(  Math.pow(cityI[0] - cityJ[0], 2) +
                                    Math.pow(cityI[1] - cityJ[1], 2)
                                    , 0.5D);
            }
        }

        if ( _loud ) Utils.logRaggedDoubles( C );

        for (double m = 2; m <= cities; m++) { // increase the budget of used cities until we use them all
            if ( m >= 4 && 1==2) { // we can start cleaning up after ourselves
                for ( int s : sets((int) m-3, (int) cities  -1, 0)) A.remove( 1+ (s<<1) ); 
            }

            int[] includedCities = sets((int)m-1, (int) cities-1, 0);

            if ( _loud ) {
                System.out.format("m=%d, c=%d: ", (int) m, (int)cities);
                Utils.logInts(includedCities);
                Utils.logRaggedInts(Utils.intsToBits( includedCities));
            }

            for (int s : includedCities) {
                int sp = (s<<1)+1; // left shift and include city one always
                double[] currentRecord = new double[(int) cities + 1 ];
                Arrays.fill( currentRecord, Double.POSITIVE_INFINITY);
                A.put( sp, currentRecord );

                if ( _loud ) {
                    System.out.print("Working with cities: ");
                    Utils.logInts( Utils.intToBits(sp) );
                }

                /*
                The intention here is to find whether a particular city is in scope
                this iteration
                 */
                CHCKING_DESTINATIONS: // todo problem here in the j loop
                for ( int j = (int) cities+2; --j>=2; ){

                    int jMask = 1 << (j-1);
                    final int destinationIncluded = jMask & sp;

                    if ( _loud ) {
                        System.out.print("Check  j ") ; Utils.logInts(Utils.intToBits(jMask));
                        System.out.print("Check sp ") ; Utils.logInts(Utils.intToBits(sp));
                        System.out.println("destinationIncluded == " + destinationIncluded);
                    }

                    if ( destinationIncluded == 0) continue CHCKING_DESTINATIONS; // this destination city is not included so skip

                    double minCost = Double.POSITIVE_INFINITY;
                    int subproblem = sp & ~( jMask );

                    CHEAPEST_ROUTE_SEARCH:
                    for (int k =  (int) m+1; --k >=1 ; )  {
                        if (_loud) System.out.format("m=%d, sp=%d, j=%d, k=%d%n", (int) m, (int) sp, (int) j, (int) k);
                        if ( j == k ) continue CHEAPEST_ROUTE_SEARCH;

                        if ( _loud ) {
                            System.out.format("sp         : %4d : ",sp         ); Utils.logInts(Utils.intToBits(sp));
                            System.out.format("jMask      : %4d : ",jMask      ); Utils.logInts(Utils.intToBits(jMask));
                            System.out.format("subproblem : %4d : ",subproblem ); Utils.logInts(Utils.intToBits(subproblem));
                        }

                        final double stubToK = A.get(subproblem)[k]; // cost of hiting cities in `subproblem`end  at k
                        final double newSolution =  (stubToK + C[k][j]); // plus the cost of going from k to j

                        if ( _loud ) {
                            System.out.format("Previous best for stub %6.3f, new option %6.3f for Subproblem with cities: ", stubToK, newSolution);
                            Utils.logInts(Utils.intToBits(sp));
                        }

                        if ( newSolution < minCost ) {
                            minCost = newSolution;
                        }
                    }
                    currentRecord[j] = minCost; // starting at 1 hitting all the cities in`sp` and end at j
                }
            }
        }


        double minTour  = Double.POSITIVE_INFINITY;
        int    bestDest = -1;

        int[] finalProblems = sets((int)cities, (int) cities, 0);

	      if( _loud ) { System.out.print( "Pattern for final problem: "); Utils.logInts( Utils.intToBits( finalProblems[0] ));}

        final double[] lastProblem = A.get(finalProblems[0]);
        if ( _loud ) { System.out.print( "Path lengths from covering all cities: ") ;Utils.logDoubles( lastProblem ); }
        
        for (int i = (int) cities+1; --i >= 1 ; )  {
            double returnCost = C[i][1];
            final double totalTour = lastProblem[i];
            if (minTour > totalTour) {
                bestDest = i;
                minTour = totalTour;
            }
        }

				if( _loud ) {
				  for( int s =0 ; s <= finalProblems[0]; s++) { 
					  double[] paths = A.get(s);
						if ( paths != null ) {
						  System.out.format("Paths hitting %10d %10s :", s, Integer.toBinaryString(s));
							Utils.logDoubles( paths );
						}
					}
				}

				if ( _loud ) System.out.println( "Tour ended at " + bestDest ) ;

        return minTour + C[bestDest][1] ;
    }


    /**
     * Generate a bit set choosing all the combinations of m destinations from c possible in total
     * for example:
     *   sets(m=2, c=3) = [0b110, 0b101, 0b011]
     *
     * @param m our remaining budget
     * @param c our total number of slots
     * @param o an offset to help recursion
     * @return
     */
    private static int[] sets( final int m, final int c, final int o) {
        if ( m == 0 ) return new int[]{o}; // no budget so cant fill any more slots
        if ( c == 0 ) return new int[]{o}; // no slots to fill to can't do anything
        if ( m == c ) return new int[]{o + ((1<<(m)) -1)}; // budget == slots so fill them

        int[] usedM = sets ( m-1, c-1, (1 << c - 1) + o);
        int[] unusedM = sets ( m, c-1, o);

        int[] result = new int[usedM.length + unusedM.length];
        System.arraycopy(usedM,0, result, 0, usedM.length);
        System.arraycopy(unusedM, 0, result, usedM.length, unusedM.length);
        return result;
    }
}
