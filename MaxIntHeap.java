
public class MaxIntHeap extends IntHeap { 

  public static void main( final String[] ARGV ) { 
    IntHeap h = new MaxIntHeap();
    for ( int i = 5 ; --i >= 0 ; ) {
      h.offer( i ) ;
      System.out.format("Added %d to %s%n", i, h.toString() );
    }

    while ( ! h.isEmpty() ) {
      System.out.format("%d - %s%n", h.poll(), h.toString() );
    }
    /////////////////////////
    h = new IntHeap();
    for ( int i = 5 ; --i >= 0 ; ) {
      h.offer( i ) ;
      System.out.format("Added %d to %s%n", i, h.toString() );
    }

    while ( ! h.isEmpty() ) {
      System.out.format("%d - %s%n", h.poll(), h.toString() );
    }
  }

  @Override
  boolean heapProperty( int parent, int child, int[] elements ) { 
    return elements[ parent ] >= elements[ child ] ;
  }

  @Override
  int chooseTarget( int[] elements, int child1, int child2) { 
    return ( elements[  child1 ] > elements[ child2 ]) ? child1 : child2;
  }


}
