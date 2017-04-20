import java.util.*;

/**
*  Simple representation of a node in a graph.
*
* 
*/
public class Node implements Comparable<Node> {
    final int _nodeID;
    Node _leader;
    int  _featureSet = 0;
    List<Node> _followers;

    boolean _explored       = false;
    Collection<Edge> _edges = new ArrayList<>();

    Node( int nodeID ) { 
      _nodeID = nodeID;
      _leader = this;
      _followers = new LinkedList<Node>();
      _followers.add( this ); 
    }

    Collection<Edge> getEdges() {
        return _edges;
    }

    @Override
    public int compareTo( Node other ){
      return this._nodeID - other._nodeID;
    }

    @Override
    public String toString() { 
      return "{ID: " + _nodeID + ", leader:"+_leader._nodeID +", _featureSet = " + _featureSet + ", edges: [" + _edges.toString() + "]}";
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

  