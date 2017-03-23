public class DescendingList<T extends Comparable<T>> extends OrderedList<T> { 

	@Override
	public void add ( T e ) { 

    if ( _first == null ){  // we're an empty list so e is the head
      _first = e;
      //System.out.println(" added '" + e + "' to the empty list. " + this.toString());
    }
    else if ( _first.compareTo( e ) < 0 ) { // current head is less than e, so e is the new head
      OrderedList<T> ol = new DescendingList<>();
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
      _rest = new DescendingList<>();
      _rest._first = e;
      //System.out.println(" added '" + e + "' as tail element.");
    }
  }

}