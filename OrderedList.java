import java.util.*;

/**
 * A simple iterabel reverse sorted list
 */
public class OrderedList<T extends Comparable<T>> implements Iterable<T> { 

  T               _first = null; 
  OrderedList<T>  _rest  = null;

  public Iterator<T> iterator() { 
    final OrderedList<T> ol = new OrderedList<>();
    ol._first = _first;
    ol._rest  = _rest;

    return new Iterator<T>() { 
      OrderedList<T> __list = ol;

      public boolean hasNext() {
        return __list != null &&  __list._first != null;
      }

      public T next() { 
        T element = __list._first;
        __list = __list._rest;
        return element;
      }
    };
  }

  public void add ( T e ) { 

    if ( _first == null ){  // we're an empty list so e is the head
      _first = e;
      //System.out.println(" added '" + e + "' to the empty list. " + this.toString());
    }
    else if ( _first.compareTo( e ) < 0 ) { // current head is less than e, so e is the new head
      OrderedList<T> ol = new OrderedList<>();
      ol._first = _first;
      ol._rest  = _rest;
      _first = e;
      _rest  = ol;
      //System.out.println("added '"+ e +"' as new head. " + this.toString() );
    }
    else if ( _first.compareTo( e ) >= 0 && _rest != null ) { // curent head is greater than e, so e is addded elsewhere
      //System.out.println("About to add '"+e+"' to a non-empty list.");
      _rest.add( e );
      //System.out.println(" After addition: " + this ) ;
    }
    else {
      _rest = new OrderedList<>();
      _rest._first = e;
      //System.out.println(" added '" + e + "' as tail element.");
    }
  }

  public int size() { return 1 + ( ( _rest == null) ? 0 : _rest.size() ) ; } 

  public String toString() { 
    return ( ( _first == null ) ? "NULL" : _first.toString() ) + ',' + ( (_rest != null) ?  _rest.toString() : "");
  }


}
