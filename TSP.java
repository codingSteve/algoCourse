import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TSP {

    private static double delta  = 0.005D;

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

    private static double[][] testCase3 = new double[][]{{5.00d}, {1.00d, 1.00d}, {2.00d, 1.00d}, {2.00d, 2.00d}, {1.00d, 2.00d}, {1.50d, 1.50d} };
    private static double expectation3  = 4.414d;

    private static double[][] testCase4 = new double[][]{{12.00d},
        {1.00d, 1.00d}, {1.125d, 1.00d}, {1.25d, 1.00d}, {1.50d, 1.00d}, {1.75d, 1.00d}, {2.00d, 1.00d},
        {1.00d, 2.00d}, {1.125d, 2.00d}, {1.25d, 2.00d}, {1.50d, 2.00d}, {1.75d, 2.00d}, {2.00d, 2.00d} };
    private static double expectation4  = 4.00d;

    // from https://www.coursera.org/learn/algorithms-npcomplete/discussions/weeks/2/threads/H3tqQUMXEeeoWQpRkEmgrg
    private static double[][] testCase5    = new double[][]{{3.00D}, {0.00D, 0.00D}, {0.00D, 3.00D}, {3.00D, 3.00D},};
    private static double     expectation5 = 10.24;
    private static double[][] testCase6    = new double[][]{{8.00D}, {0.00, 2.05}, {3.414213562373095, 3.4642135623730947}, {0.5857864376269049, 0.6357864376269047}, {0.5857864376269049, 3.4642135623730947}, {2.00, 0.00}, {4.05, 2.05}, {2.00, 4.10}, {3.414213562373095, 0.6357864376269047},};
    private static double     expectation6 = 12.36;
    private static double[][] testCase7    = new double[][]{{4.00}, {0.00D, 0.00D}, {4.00D, 3.00D}, {4.00D, 0.00D}, {0.00D, 3.00D},};
    private static double     expectation7 = 14.00;

    private static double[][][] testCases    = new double[][][]{testCase0,    testCase1,    testCase2,    testCase3,    testCase4,    testCase5,    testCase6,    testCase7,    };
    private static double[]     expectations = new double[]{    expectation0, expectation1, expectation2, expectation3, expectation4, expectation5, expectation6, expectation7, };

    public static void main(String[] ARGV) throws Exception {
        int times = 1;
        for (int i = 0, l = ARGV.length; i < l; i++) {
            if ("--loud".equals(ARGV[i])) _loud = true;
            else if ("--quiet".equals(ARGV[i])) _loud = false;
            else if ("--times".equals(ARGV[i])) times = Integer.valueOf(ARGV[++i]);
            else if ("--file".equals(ARGV[i])) {
                String fileName = ARGV[++i];

                for ( int j = times; --j >=0 ; ) {
                    double[][] rawInput = Utils.fileToRaggedArrayOfDoubles(fileName, " ");
                    long start = System.nanoTime();
                    double actual = tsp(rawInput);
                    long duration = System.nanoTime() - start;

                    assert( rawInput[0][0] == rawInput.length -1);

                    System.out.format("Run %3d of file %s produced tour length %6.6f from %d records in %6.3fs%n",
                             j, fileName, actual, rawInput.length -1, (duration / 1E09));
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

                        System.out.format("Run %3d of testCase %d produced tour length %6.3f (expected %6.3f) from %2d in %6dμs%n",
                                k, j, actual, expected, (int) rawInput[0][0], (duration / 1000));
                        if (Math.abs(expected - actual) > delta) break TESTS;

                    }
                }
            }
        }
    }

    public static double tsp(double[][] rawInput) {
        final int cities = (int) rawInput[0][0];
        Map<Integer, double[]> A = new HashMap<>( );

        double[] homeRow = new double[ cities +1 ];
        Arrays.fill( homeRow, Double.POSITIVE_INFINITY);
        homeRow[1]=0;

        A.put( 1, homeRow );

        double[][] C = cacheDistances(rawInput, cities);

        if ( _loud ) Utils.logRaggedDoubles( C );

        for (int m = 2; m <= cities; m++) { // increase the budget of used cities until we use them all
            if ( m >= 4 && !_loud ) { // we can start cleaning up after ourselves
                for ( int s : sets( m-3,  cities  -1, 0)) A.remove( 1+ (s<<1) );
            }

            int[] includedCities = sets(m-1,  cities-1, 0);

            if ( _loud ) logIncludedCities(cities, m, includedCities);

            for (int s : includedCities) {
                int targetPath = includeCityOne(s);

                double[] costsForTargetPathEndingAt = setupAndCacheCurrentRecord( cities, A, targetPath);

                if ( _loud ) logTargetPath(targetPath);

                /*
                The intention here is to find whether a particular city is on our
                target path this iteration, if it is, we look at an earlier, shorter,
                pre-calculated path which has hit every other city but J
                 */
                CHECKING_DESTINATIONS:
                for ( int j = cities+1; --j>=2; ){

                    int jMask = 1 << (j-1);
                    int destinationIncluded = jMask & targetPath;

                    if ( _loud ) logDestinationIncluded(targetPath, jMask, destinationIncluded);

                    // this destination city is not included in our current path so skip
                    if ( destinationIncluded == 0) continue CHECKING_DESTINATIONS;

                    double currentBestForJ = Double.POSITIVE_INFINITY;
                    // Our sub-problem has been to everywhere in `sp` but not j
                    int subProblem = targetPath & ~jMask;
                    double[] subProblemRecord = A.get(subProblem);

                    if ( _loud ) logSubProblemCities(targetPath, jMask, subProblem, subProblemRecord);

                    /*
                    For our sub-problem `sp - {j}` we now check each of the end points
                    to see which is best for us to tack on the final hop to j
                    Route := (1 .. k) + (k .. j)
                     */
                    CHEAPEST_ROUTE_SEARCH:
                    for (int k = cities+1; --k >=1 ; )  {
                        if (_loud) System.out.format("m=%d, sp=%d, j=%d, k=%d%n", m, targetPath, j, k);

                        if ( j == k )                           continue CHEAPEST_ROUTE_SEARCH; //
                        if ( (targetPath & 1 << (k - 1)) == 0 ) continue CHEAPEST_ROUTE_SEARCH; // k is not in the target path

                        double newSolutionForJ = subProblemRecord[k] + C[k][j]; // stub to k then k to j

                        if ( _loud ) logStubPathAndNewSolution(targetPath, subProblemRecord[k], newSolutionForJ, currentBestForJ);

                        if ( currentBestForJ > newSolutionForJ ) currentBestForJ = newSolutionForJ;

                    }
                    costsForTargetPathEndingAt[j] = currentBestForJ; // starting at 1 hitting all the cities in`sp` and end at j
                }
            }
        }


        double minTour  = Double.POSITIVE_INFINITY;

        int[] finalProblems = sets(cities, cities, 0);

        final double[] lastProblem = A.get(finalProblems[0]);
        for (int i = cities+1; --i >= 2 ; )  {
            double totalTour = lastProblem[i] + C[i][1];
            if (minTour > totalTour) minTour = totalTour;
        }

        if( _loud ) {
            System.out.format( "Pattern for final problem: %25s%n", Integer.toBinaryString(finalProblems[0] ));
            System.out.print( "Path lengths from covering all cities: ") ;Utils.logDoubles( lastProblem );
            logSubProblems(cities, A);
        }

        return minTour ;
    }

    private static void logSubProblems(int cities, Map<Integer, double[]> a) {
        for ( int m = 1 ; m <= cities; m++ ){
            int[] sets = sets(m, cities, 0);
            Arrays.sort(sets);
            for ( int s : sets) {
                double[] paths = a.get(s);
                if ( paths == null ) continue;

              System.out.format("Paths hitting %2d venues(s) %10d %10s :", m, s, Integer.toBinaryString(s));
              Utils.logDoubles( paths );
            }
        }
    }

    private static int includeCityOne(int s) {
        return (s<<1)+1;
    }

    private static double[][] cacheDistances(double[][] rawInput, int cities) {
        double[][] C = new double[cities +1][cities +1];
        for (int i = cities +1; --i >=1 ; ) {
            final double[] cityI = rawInput[i];
            for (int j = cities +1; --j>=1 ;){
                final double[] cityJ = rawInput[j];
                C[i][j]= Math.hypot((cityI[0] - cityJ[0]) , (cityI[1] - cityJ[1]) );
            }
        }
        return C;
    }

    private static double[] setupAndCacheCurrentRecord(int cities, Map<Integer, double[]> a, int targetPath) {
        double[] currentRecord = new double[cities + 1 ];
        Arrays.fill( currentRecord, Double.POSITIVE_INFINITY);
        a.put( targetPath, currentRecord );
        return currentRecord;
    }

    private static void logDestinationIncluded(int targetPath, int jMask, int destinationIncluded) {
        System.out.format("Check destination   : %25S%n", Integer.toBinaryString(jMask)  ) ;
        System.out.format("Check targetPath    : %25s%n", Integer.toBinaryString(targetPath)  ) ;
        System.out.format("destinationIncluded : %25s%n", Integer.toBinaryString(destinationIncluded));
    }

    private static void logTargetPath(int targetPath) {
        System.out.format("New Target Path     : %25s%n", Integer.toBinaryString(targetPath));
    }

    private static void logStubPathAndNewSolution(int targetPath, double stubToK, double newSolution, double currentBest) {
        System.out.format("Previous best for stub %6.3f, new option %6.3f ( current best %6.3f ) " +
                "for sub-problem with cities: %25s%n",
                stubToK, newSolution, currentBest, Integer.toBinaryString(targetPath));
    }

    private static void logSubProblemCities(int sp, int jMask, int subProblem, double[] subProblemPaths) {
        System.out.format("targetPath   : %4d : %25s%n", sp        , Integer.toBinaryString(sp));
        System.out.format("jMask        : %4d : %25s%n", jMask     , Integer.toBinaryString(jMask));
        System.out.format("subProblem   : %4d : %25s%n", subProblem, Integer.toBinaryString(subProblem));
        System.out.print("path costs          : "); Utils.logDoubles( subProblemPaths);

    }

    private static void logIncludedCities(int cities, int m, int[] includedCities) {
        System.out.format("m=%d, c=%d: ", m, cities);
        Utils.logInts(includedCities);
        Utils.logRaggedInts(Utils.intsToBits( includedCities));
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
