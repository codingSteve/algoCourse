import java.util.Arrays;

public class TSP {

    private static boolean _loud = false;
    private static double[][] testCase0 = new double[][]{{4.00d}, {1.00d, 1.00d}, {2.00d, 1.00d}, {2.00d, 2.00d}, {1.00d, 2.00d}};
    private static double expectation0  = 4.00d;

    private static double[][][] testCases = new double[][][]{testCase0,};
    private static double[] expectations = new double[]{expectation0,};

    public static void main(String[] ARGV) throws Exception {
        int times = 1;
        for (int i = 0, l = ARGV.length; i < l; i++) {
            if ("--loud".equals(ARGV[i])) _loud = true;
            else if ("--quiet".equals(ARGV[i])) _loud = false;
            else if ("--times".equals(ARGV[i])) times = Integer.valueOf(ARGV[++i]);
            else if ("--test".equals(ARGV[i])) {
                TESTS:
                for (int j = testCases.length; --j >= 0; ) {
                    double[][] rawInput = testCases[j];
                    double expected = expectations[j];

                    for (int k = times; --k >= 0; ) {
                        long start = System.nanoTime();
                        double actual = tsp(rawInput);
                        long duration = System.nanoTime() - start;

                        System.out.format("Run %3d of testcase %d produced tour length %6.6f (expected %6.6f) in %6dμs",
                                k, j, actual, expected, (duration / 1000));
                        if (expected != actual) break TESTS;

                    }
                }
            }
        }
    }

    public static double tsp(double[][] rawInput) {


        final double cities = rawInput[0][0];
        final int arrayLength = (int) Math.pow(2.00d, cities);
        double[][] A = new double[arrayLength][arrayLength];

        for ( int s = arrayLength ; --s>=0 ; ) A[s][1] = Double.POSITIVE_INFINITY;
        A[1][1] = 0;


        double[][] C = new double[(int)cities+1][(int)cities+1];
        for (int i = (int) cities; --i >=1 ; ) {
            for ( int j = (int) cities ; --j>=1 ;){
                final double[] cityI = rawInput[i];
                final double[] cityJ = rawInput[j];
                C[i][j]= Math.pow(  Math.pow(cityI[0] - cityJ[0], 2) +
                                    Math.pow(cityI[1] - cityJ[1], 2)
                                    , 0.5D);
            }
        }

        if ( _loud ) Utils.logRaggedDoubles( C );

        for (double m = 2; m <= cities; m++) {
            int[] includedCities = sets((int)m-1, (int) cities-1, 0);
            if ( _loud ) {
                System.out.format("m=%d, c=%d: ", (int) m, (int)cities);
                Utils.logInts(includedCities);
                Utils.logRaggedInts(Utils.intsToBits( includedCities));
            }
            for (int s : includedCities) {
                int sp = (s<<1)+1; // left shift and include city one

                if ( _loud ) {
                    System.out.print("Working with cities: ");
                    Utils.logInts( Utils.intToBits(sp) );
                }

                CHCKING_DESTINATIONS:
                for (int j = 2; j < m; j++) {

                    int jMask = 1 << (j);
                    final int destinationIncluded = jMask & sp;
                    if ( _loud ) {
                        System.out.print("Check  j ") ; Utils.logInts(Utils.intToBits(jMask));
                        System.out.print("Check sp ") ; Utils.logInts(Utils.intToBits(sp));
                        System.out.println("destinationIncluded == " + destinationIncluded);
                    }


                    if ( destinationIncluded == 0) continue CHCKING_DESTINATIONS; // this destination city is not included so skip

                    double minCost = Double.POSITIVE_INFINITY;

                    CHEAPEST_ROUTE_SEARCH:
                    for (int k = 2; k < m; k++) {
                        if (_loud) System.out.format("m=%d, s=%d, j=%d, k=%d%n", (int) m, (int) s, (int) j, (int) k);
                        if ( j == k ) continue CHEAPEST_ROUTE_SEARCH;


                        int kMask = 1<<(k);
                        int subproblem = sp - destinationIncluded;

                        final double previousBest = A[subproblem][k];
                        if ( _loud ) {
                            System.out.format("Previos best %6.3f for Subproblem with cities: ", previousBest);
                            Utils.logInts(Utils.intToBits(subproblem));
                        }


                        double basdName =  (previousBest + C[k][j]);
                        if ( basdName < minCost ) {
                            minCost = basdName;
                            A[jMask][j] = basdName;
                        }

                    }
                }
            }
        }

        if ( _loud ) Utils.logRaggedDoubles(A);


        return -1.00d;
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
