import java.util.*;

/**
*  Simple representation of a node in a graph.
*
* 
*/
public class Node {
    final int _nodeID;

    boolean _explored      = false;
    Collection<Edge> _edges = new ArrayList<>();

    Node( int nodeID ) { 
      _nodeID = nodeID;
    }

    Collection<Edge> getEdges() {
        return _edges;
    }

    @Override
    public String toString() { 
      return "{" + _nodeID + ",[" + _edges.toString() + ']';
    }

    @Override
    public int hashCode() { return _nodeID ;}

    @Override
    public boolean equals( Object other ) { 
      if ( other instanceof Node ) {
        return _nodeID == ((Node)other)._nodeID;
      }
      return false;
    }

  }

  