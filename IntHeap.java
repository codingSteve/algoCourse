

public class IntHeap {

  boolean _loud = false;

  public static void main( final String[] ARGV ) { 
    IntHeap h = new IntHeap();
    for ( int i = 5 ; --i >= 0 ; ) {
      h.offer( i ) ;
      System.out.format( h.toString() + " %d %n", h.size());
    }

    while ( ! h.isEmpty() ) {
      System.out.format("%d - %s%n", h.poll(), h.toString() );
    }
  }

  protected int[] elements;
  protected int   firstEmptySlot = 1 ; 

  public IntHeap( final int size ) {
    elements = new int[size];
  }

  public IntHeap() { this(10); }

  public int poll() {
    int result = elements[1];
    firstEmptySlot--;
    swap( firstEmptySlot, 1, elements );
    bubbleDown( 1 );
    return result;
  }

  public int     peek()    { return elements[1];          }
  public boolean isEmpty() { return firstEmptySlot == 1 ; }
  public int     size()    { return firstEmptySlot - 1  ; } 

  public void offer( final int element ) { 
    if ( firstEmptySlot == elements.length ) { 
      int[] ne = new int[ elements.length + ( elements.length >> 1) ];
      System.arraycopy(elements, 0, ne, 0, elements.length);
      this.elements = ne;
    }

    elements[firstEmptySlot++] = element;
    bubbleUp( firstEmptySlot-1 );
  }

  private final void bubbleUp( int position )  { 
    while ( position > 1 && invalidHeap( position >> 1 ) ) { 
      int parent = position >> 1;
      swap( position, parent, elements);
      position = parent;
    }
  }

  private final void bubbleDown( int position ) {
    while( invalidHeap( position ) ) { 
      int targetPosition = targetPosition( position, elements );
      swap(position, targetPosition, elements) ;
      position = targetPosition;

    }
  }

  private final boolean invalidHeap( int parent ) { 
    int child1 = parent << 1;
    int child2 = child1 + 1; 

    final boolean heapPropertyForChild1 = ( child1 < firstEmptySlot ) ? heapProperty( parent, child1 ) : true;
    final boolean heapPropertyForChild2 = ( child2 < firstEmptySlot ) ? heapProperty( parent, child2 ) : true;

    return   ( heapPropertyForChild1 ) ? !heapPropertyForChild2 : true  ; 
  }

  boolean heapProperty( int parent, int child ) { 
    return elements[ parent ] <= elements[ child ] ;
  }

  final private int targetPosition( final int parent, final int[] elements ) {
    int child1 = parent << 1;
    int child2 = child1 + 1; 

    if      ( child1 >= firstEmptySlot ) throw new IllegalArgumentException("trying to decide on a target when there are no valid child nodes");
    else if ( child2 >= firstEmptySlot ) return child1;
    else                                 return chooseTarget( elements,  child1, child2 );

  }

  int chooseTarget( int[] elements, int child1, int child2) { 
    return ( elements[  child1 ] < elements[ child2 ]) ? child1 : child2;
  }

  private static final  void swap ( int position1, int position2, int[] elements ) { 
    int t = elements[ position1 ];
    elements[ position1 ] = elements[ position2 ];
    elements[ position2 ] = t;
  }

  public String toString() { 
    String result = "[";
    for ( int i = 1 ; i < firstEmptySlot ; i++  ) result += "" + elements[ i ] + ','; 
    return result + "]";
  }

}
