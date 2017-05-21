import java.util.*;

public class APSP { 

	private static int       NEGATIVE_CYCLE = Integer.MIN_VALUE;
	private static boolean   _loud          = false;

	private static int[][] testCase0    = new int[][]{{2,1},{1,2,1}};
	private static int     expectation0 = 1;

	private static int[][] testCase1    = new int[][]{{2,2},{1,2,1}, {2,1,-1}}; // zero cost cycle
	private static int     expectation1 = -1;

	private static int[][] testCase2    = new int[][]{{2,2},{1,2,0}, {2,1,-1}}; // negative cost cycle
	private static int     expectation2 = NEGATIVE_CYCLE;
	
	private static int[][] testCase3    = new int[][]{{6,7},
		{1,2,-2}, 
		{2,3,-1},
		{3,1, 4},
		{3,4, 2},
		{3,5,-3},
		{6,4, 1},
		{6,5,-4},
		}; // negative cost cycle
	private static int     expectation3 = -6;



	private static int[][][] testCases    = new int[][][]{ testCase0,    testCase1,    testCase2,    testCase3,    };
	private static int[]     expectations = new int[]{     expectation0, expectation1, expectation2, expectation3, };

	public static void main(String[] args) throws Exception {
		int times = 1;
		for ( int i = 0, l = args.length; i < l; i++ ){
			if ( "--loud".equals(args[i]) ) { 
				Dijkstra._loud  = true; 
				Dijkstra._quiet = true; 
				_loud = true;
			}
			else if ( "--quiet".equals( args[i] )) {
				Dijkstra._loud  = false; 
				Dijkstra._quiet = true; 
				_loud = false; 
			}
			else if ( "--times".equals( args[i] )) times = Integer.valueOf( args[++i]);
			else if ( "--file".equals( args[i] )){
				String fileName = args[++i];
				int[][] rawInput = Utils.fileToRaggedArrayOfInts( fileName, " " );
				for ( int k = times ; --k>=0 ; ){
					Dijkstra.Node[] nodes = new Dijkstra.Node[1 + rawInput[0][0]]; // plus one for ease later
					Dijkstra.Edge[] edges = new Dijkstra.Edge[1 + rawInput[0][1]]; // plus one for ease later
					parseEdges( rawInput, nodes, edges);

					long start    = System.nanoTime();
					int actual    = shortestPath( nodes, edges);
					long duration = System.nanoTime() - start;

					System.out.format("run %3d of file name %s: produced %12d                   in %6dµs%n",
						k, fileName, actual,  (duration/1000));


				}
			}
			else if ( "--test".equals( args[i] )){
				TESTS:
				for ( int j = testCases.length; --j>=0; ){
					for( int k = times; --k >= 0 ; ){
						Dijkstra.Node[] nodes = new Dijkstra.Node[1 + testCases[j][0][0]]; // plus one for ease later
						Dijkstra.Edge[] edges = new Dijkstra.Edge[1 + testCases[j][0][1]]; // plus one for ease later
						parseEdges( testCases[j], nodes, edges);

						if ( _loud ) {
							System.out.print("About to run on edges: ");
							Utils.logObjects( (Object[]) edges);
						}

						long start    = System.nanoTime();
						int actual    = shortestPath( nodes, edges);
						long duration = System.nanoTime() - start;

						System.out.format("run %3d of test case %d: produced %12d ( expected %12d ) in %6dµs%n",
							k, j, actual, expectations[j], (duration/1000));

						if ( actual != expectations[j]) break TESTS;

					}
				}
			}
		}
	}

	private static void parseEdges( int[][] rawInput, Dijkstra.Node[] nodes, Dijkstra.Edge[] edges){
		for ( int i = rawInput.length ; --i>0 ; ){
			int[] rawEdge = rawInput[i];
			int tailID    = rawEdge[0];
			int headID    = rawEdge[1];
			int cost      = rawEdge[2];

			if( nodes[tailID] == null ){
				nodes[tailID] = new Dijkstra.Node( tailID );
			}
			if ( nodes[headID] == null ){
				nodes[headID] = new Dijkstra.Node( headID );
			}

			Dijkstra.Edge e = new Dijkstra.Edge(nodes[tailID], nodes[headID], cost);
			edges[i] = e;
			e._head._incoming.add(e);
			e._tail._edges.add(e);
		}
	}

	public static void bellmanFord( Dijkstra.Node[] G, Dijkstra.Node s, double[] shortestPaths, int edgeCount ){

		double[] a = new double[G.length];
		Arrays.fill(a, Double.POSITIVE_INFINITY);
		a[s._nodeID] = 0;

		double[] b = new double[G.length];

		for ( int i = 1 ; i <= edgeCount ; i++ ) {
			if ( _loud ) Utils.logObjects("Budget of ", i);

			boolean anyChange = false;
			for ( int v = G.length; --v>=1; ){
				double currentBest = a[v];
				if ( _loud ) System.out.format( "finding shorter path for %d, curent best == %3.0f%n",
												 v, currentBest);


				for ( Dijkstra.Edge e : G[v]._incoming ){
					if ( _loud ) Utils.logObjects(e);

					double newPathLength = a[e._tail._nodeID] + e._length;
					if ( _loud ) System.out.format( "using %s gets new path length = %3.0f ", e, newPathLength);
					if ( currentBest > newPathLength) {
						anyChange   = true;
						currentBest = newPathLength;
						if ( _loud ) System.out.print(" improvement!");
					}
					if ( _loud ) System.out.println();
				}
				b[v] = currentBest;			
				
			}

			if ( _loud ) {
				System.out.print("A[i-1] = "); Utils.logDoubles(a);
			    System.out.print("A[i  ] = "); Utils.logDoubles(b);
			}

			if ( anyChange && i == edgeCount ) {
				Arrays.fill(shortestPaths, NEGATIVE_CYCLE);
				return;
			}

			double[] t = a;
			a = b;
			b = t;
		}
		System.arraycopy ( a, 0, shortestPaths, 0, shortestPaths.length);
	}

	private static void updateCosts( double[] offsets, Dijkstra.Edge[] edges ){
		for ( int i = edges.length ; --i>=1; ){
			Dijkstra.Edge e = edges[i];
			e._length = e._length + ( int ) ( offsets[ e._tail._nodeID ] - offsets[ e._head._nodeID ] );
		}
	}


	public static int shortestPath( Dijkstra.Node[] G, Dijkstra.Edge[] E ){

		Dijkstra.Node s = new Dijkstra.Node(0);
		G[0] = s;

		for ( int i= G.length; --i>=1; ) {
			Dijkstra.Node n = G[i];
			Dijkstra.Edge e = new Dijkstra.Edge(s, n, 0);
			s._edges.add( e );
			n._incoming.add( e );
		}

		double[] shortestPathsFromS = new double[G.length];
		bellmanFord( G, s, shortestPathsFromS, E.length);

		if ( _loud ) Utils.logDoubles( shortestPathsFromS );

		if ( shortestPathsFromS[0] == NEGATIVE_CYCLE ) return NEGATIVE_CYCLE;

		updateCosts( shortestPathsFromS, E );
		if( _loud ) Utils.logObjects( (Object[]) E );

		int[][] shortestPaths = new int[G.length][G.length];

		int shortestPath = Integer.MAX_VALUE;
		for ( int i= G.length; --i>=1; ) { 
			Dijkstra.Node u = G[i];
			for( Dijkstra.Node n : G ) { 
				n._explored = false; 
				n._shortestPath = Dijkstra.UNSET;
			}

			shortestPaths[ i ] = Dijkstra.dijkstra( G, u );

			if ( _loud ) {
				System.out.print("Shortest paths from node " + i + ':' + u.toString() + ": ");
				Utils.logInts(shortestPaths[i]);
			}


			for ( int j = shortestPaths[0].length; --j>=1; ) {
				int length = shortestPaths[ i ][ j ];
				if (length == Dijkstra.UNSET) continue;

				length = length - ( int ) shortestPathsFromS[ i ] + (int) shortestPathsFromS[j];
				
				shortestPaths[ u._nodeID ][j] = length;


				if ( shortestPath > length ) shortestPath = length;
			}
			if ( _loud ) Utils.logInts(shortestPaths[i]);
		}
		if ( _loud ) Utils.logRaggedInts(shortestPaths);

		return shortestPath;
	}




}