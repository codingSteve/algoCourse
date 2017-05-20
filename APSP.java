
public class APSP { 

	private static int[][] testCase0    = new int[][]{{2,1},{1,2,1}};
	private static int     expectation0 = 1;

	private static int[][][] testCases    = new int[][][]{ testCase0,    };
	private static int[]     expectations = new int[]{     expectation0, };

	private static int       NEGATIVE_CYCLE = -1;
	private static boolean   _loud          = false;

	public static void main(String[] args) {
		int times = 1;
		for ( int i = 0, l = args.length; i < l; i++ ){
			if ( "--loud".equals(args[i]) ) _loud = true;
			else if ( "--quiet".equals( args[i] )) _loud = false; 
			else if ( "--times".equals( args[i] )) times = Integer.valueOf( args[++i]);
			else if ( "--test".equals( args[i] )){
				TESTS:
				for ( int j = 0, tests = testCases.length; j<tests; j++ ){
					for( int k = times; --k >= 0 ; ){
						Dijkstra.Node[] nodes = new Dijkstra.Node[1 + testCases[j][0][0]]; // plus one for ease later
						Dijkstra.Edge[] edges = new Dijkstra.Edge[1 + testCases[j][0][1]]; // plus one for ease later
						parseEdges( testCases[j], nodes, edges);

						long start    = System.nanoTime();
						int actual    = shortestPath( nodes, edges);
						long duration = System.nanoTime() - start;

						System.out.format("run %d of test case %d: produced %d ( expected %d ) in %6dµs%n",
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
			edges[i] = new Dijkstra.Edge(nodes[tailID], nodes[headID], cost);
		}

	}

	public static void bellmanFord( Dijkstra.Node[] G, Dijkstra.Node s, int[] shortestPaths ){

	}

	private static void updateCosts( int[] offset, Dijkstra.Edge[] original, Dijkstra.Edge[] modified ){

	}


	public static int shortestPath( Dijkstra.Node[] G, Dijkstra.Edge[] V ){

		Dijkstra.Node s = new Dijkstra.Node(0);
		G[0] = s;

		for ( int i= G.length; --i>=1; ) {
			Dijkstra.Node n = G[i];
			s._edges.add( new Dijkstra.Edge(s, n, 0) );
		}

		int[] shortestPathsFromS = new int[G.length];
		bellmanFord( G, s, shortestPathsFromS);

		Dijkstra.Edge[] modifiedEdges = new Dijkstra.Edge[V.length];
		updateCosts( shortestPathsFromS, V, modifiedEdges );

		int[][] shortestPaths = new int[G.length][G.length];

		int shortestPath = Integer.MAX_VALUE;
		for ( int i= G.length; --i>=1; ) {
			Dijkstra.Node u = G[i];
			shortestPaths[u._nodeID] = Dijkstra.dijkstra( G	, u );
		}

		return shortestPath;
	}




}