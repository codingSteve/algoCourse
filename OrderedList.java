import java.util.*;

/**
 * A simple iterable list inspired by lisp lists
 */
public abstract class OrderedList<T extends Comparable<T>> implements Iterable<T> { 

  T               _first = null; 
  OrderedList<T>  _rest  = null;

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

  public int size() { return 1 + ( ( _rest == null) ? 0 : _rest.size() ) ; } 

  public String toString() { 
    return ( ( _first == null ) ? "NULL" : _first.toString() ) + ',' + ( (_rest != null) ?  _rest.toString() : "");
  }

  public abstract void add( T e);


}
