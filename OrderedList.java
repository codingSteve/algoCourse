import java.util.*;

/**
 * A simple iterable list inspired by lisp lists
 */
public abstract class OrderedList<T extends Comparable<T>> implements Iterable<T> { 

  T               _first = null; 
  OrderedList<T>  _rest  = null;

  static boolean _VERBOSE = false;

  public Iterator<T> iterator() { 

    return new Iterator<T>() { 
      OrderedList<T> __list = OrderedList.this;

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
  
  public T remove() { 
    T head = _first;
    if ( _VERBOSE ) System.out.println( "before: " + this.toString() ) ;


    if ( _rest != null ) { 
      _first = _rest._first;
      _rest  = _rest._rest; 
    }
    else { 
      _first = null;
    }

    if ( _VERBOSE ) System.out.println( "after : " + this.toString() );
    return head;
  }

  public T remove( T element ) { 
    if ( element.equals( element ) ) {
      return this.remove();
    }
    else {
      return ( _rest == null ) ? null :  _rest.remove( element );
    }
  }

  public boolean isEmpty() { return _first == null ;}

  public int size() { return (isEmpty()) ? 0 : (1 + ( ( _rest == null) ? 0 : _rest.size() ) ); } 

  public String toString() { 
    return ( ( _first == null ) ? "NULL" : _first.toString() ) + ',' + ( (_rest != null) ?  _rest.toString() : "");
  }

  public void addAll( OrderedList<T> other ) { 
    for ( T e : other ) { 
      this.add ( e );
      if ( _VERBOSE ) System.out.println("adding all: " + this.toString() );
    }
  }

  public abstract void add( T e );


}
