import java.util.*;

public class Tuple<A extends Comparable<A>> {
  public final A _1;
  public final A _2;

  public Tuple( A a, A b ) { 
    //if ( a.compareTo(b) < 0  ) { _1 = a ; _2 = b ;} 
    //else { _1 = b; _2 = a; }
    _1 = a; _2 = b;
  }
  public String toString() { return "(" + _1.toString() + ',' + _2.toString() + ')'; } 

  public boolean equals( Object other ) { 
    if ( other instanceof Tuple ) { 
      return  ( this._1.equals( ( (Tuple) other)._1) &&
                this._2.equals( ((Tuple) other)._2)
              );
    }

    return false;
  }

  public int hashCode() { 
    int result = 7 ;
    result *= 31;
    result += _1.hashCode();
    result *= 31;
    result += _2.hashCode();
    return result;
  }
}
