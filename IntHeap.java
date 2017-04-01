

public class IntHeap {

  public static void main( final String[] ARGV ) { 
    IntHeap h = new IntHeap();
    for ( int i = 5 ; --i >= 0 ; ) {
      h.offer( i ) ;
      System.out.println( h.toString() );
    }

    while ( ! h.isEmpty() ) {
      System.out.format("%d - %s%n", h.poll(), h.toString() );
    }
  }

  private int[] elements;
  private int   firstEmptySlot = 1 ; 

  public IntHeap( final int size ) {
    elements = new int[size];
  }

  public IntHeap() { this(10); }

  public int poll() {
    int result = elements[1];
    firstEmptySlot--;
    swap( firstEmptySlot, 1, elements );
    bubbleDown( 1, elements, firstEmptySlot );
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

    elements[firstEmptySlot] = element;
    bubbleUp(firstEmptySlot++, elements, firstEmptySlot );
  }

  private final void bubbleUp( int position, int[] elements, int firstEmptySlot ) { 
    while ( position > 1 && invalidHeap( position >> 1, elements, firstEmptySlot  ) ) { 
      int parent = position >> 1;
      swap( position, parent, elements);
      position = parent;
    }
  }

  private final void bubbleDown( int position, int[] elements, int firstEmptySlot ) {
    while( position < firstEmptySlot && invalidHeap( position, elements, firstEmptySlot) ) { 
      int targetPosition = targetPosition( position, elements );
      swap(position, targetPosition, elements) ;
      position = targetPosition;

    }
  }

  private final boolean invalidHeap( int parent, int[] elements, int firstEmptySlot ) { 
    int child1 = parent << 1;
    int child2 = child1 + 1; 

    final boolean noChildNodes = ( child1 >= firstEmptySlot );
    final boolean oneChildNode = ( child2 >= firstEmptySlot );

    final boolean heapPropertyForChild1 = heapProperty( parent, child1, elements );
    final boolean heapPropertyForChild2 = heapProperty( parent, child2, elements );

    return !(
          noChildNodes                                       || // no children so the property holds
          ( oneChildNode && heapPropertyForChild1 )          || // only one child node so only check that 
          ( heapPropertyForChild1 && heapPropertyForChild2)     // two child nodes so check both 
        );

  }

  boolean heapProperty( int parent, int child, int[] elements ) { 
    return elements[ parent ] <= elements[ child ] ;
  }

  final private int targetPosition( final int parent, final int[] elements ) {
    int child1 = parent << 1;
    int child2 = child1 + 1; 

    final boolean noChildNodes = ( child1 >= firstEmptySlot );
    final boolean oneChildNode = ( child2 >= firstEmptySlot );

    if ( noChildNodes) throw new IllegalArgumentException("trying to decide on a target when there are no valid child nodes");
    else if ( oneChildNode ) return chooseTarget(elements,  child1, child1);
    else   return chooseTarget( elements,  child1, child2 );
  }

  int chooseTarget( int[] elements, int child1, int child2) { 
    return ( elements[  child1 ] < elements[ child2 ]) ? child1 : child2;
  }

  private static final int minChild( final int position, int[] elements ) { 
    return ( elements[ position <<1] < elements[ 1 + (position <<1)]) 
      ? elements[     (position << 1)] 
      : elements[ 1 + (position << 1)];
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
