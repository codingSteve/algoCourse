


/**
 * Your task is to code up the algorithm from the video lectures for computing strongly 
 * connected components (SCCs), and to run this algorithm on the given graph.
 *
 * Output Format: You should output the sizes of the 5 largest SCCs in the given graph,
 * in decreasing order of sizes, separated by commas (avoid any spaces). So if your
 * algorithm computes the sizes of the five largest SCCs to be 500, 400, 300, 200 and 100,
 * then your answer should be "500,400,300,200,100" (without the quotes). If your algorithm 
 * finds less than 5 SCCs, then write 0 for the remaining terms.
 * Thus, if your algorithm computes only 3 SCCs whose sizes are 400, 300, and 100,
 * then your answer should be "400,300,100,0,0" (without the quotes). 
 * (Note also that your answer should not have any spaces in it.)
 *
 * WARNING: This is the most challenging programming assignment of the course. 
 * Because of the size of the graph you may have to manage memory carefully. 
 * The best way to do this depends on your programming language and environment, 
 * and we strongly suggest that you exchange tips for doing this on the discussion forums.
 * **/

public class StronglyConnectedComponents {
  public static void main( String ARGV[] ){
  }

  public static int[] StronglyConnectedComponents( int[][] rawInput, int outputSize ){
    return new int[5];
  }
  
  public static class Node {
    int _id;
    int _leader;
    int _finishTime;
    boolean _explored; 
    List<Node> _adjecentNodes;
    
    public void dfs() {
      for ( Node n : _adjacentNodes ) {
        if ( ! n.isExplored() ){
          n.isExplored( true );
          n.dfs();
        }
      }
    }
  }
  
  
}
