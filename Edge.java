public class Edge implements Comparable<Edge> { 
    final Node _tail;
    final Node _head;
    final int _length;

    Edge ( Node tail, Node head, int length ) { 
      _tail = tail; _head = head; _length = length;
    }

    public int score(){
      return _length ;
    }

    public String toString() { 
      return "{" + _tail._nodeID + "->" + _head._nodeID + '(' + _length + ")}";
    }

    @Override
    public boolean equals( Object other ) { 
      if ( other != null && other instanceof Edge ) {
        Edge m2 = (Edge) other;
        return m2._head == this._head && m2._tail == this._tail && m2._length == this._length;
      }
      return false;
    }

    @Override
    public int hashCode() {
      int result = 7 ; 

      result *= 31; result += _head._nodeID;
      result *= 31; result += _tail._nodeID;
      result *= 31; result += _length;

      return result;
    }

    @Override
    public int compareTo( Edge other) { 
      if ( other == this ) {
        return 0;
      }
      
      Edge m2 = (Edge) other;
      if ( this.equals( m2 ) ) return 0;
      if (this.score() == m2.score() ) return ( (this.hashCode() < m2.hashCode() ) ? -1 : 1) ;
      return this.score() - m2.score();
    }
}