public class AscendingList<T extends Comparable<T>> extends OrderedList<T> {

  static boolean _VERBOSE = false;

	@Override
	public void add ( T e ) { 

    if ( _first == null ){  // we're an empty list so e is the head
      _first = e;
      if (_VERBOSE ) System.out.println(" added '" + e + "' to the empty list. " + this.toString());
    }
    else if ( _first.compareTo( e ) > 0 ) { // current head is greater than e, so e is the new head
      OrderedList<T> ol = new AscendingList<>();
      ol._first = _first;
      ol._rest  = _rest;
      _first = e;
      _rest  = ol;
      if (_VERBOSE ) System.out.println("added '"+ e +"' as new head. " + this.toString() );
    }
    else if ( _first.compareTo( e ) <= 0 && _rest != null ) { // curent head is less than e, so e is addded elsewhere
      if (_VERBOSE ) System.out.println("About to add '"+e+"' to a non-empty list.");
      _rest.add( e );
      if (_VERBOSE ) System.out.println(" After addition: " + this ) ;
    }
    else {
      _rest = new AscendingList<>();
      _rest._first = e;
      if (_VERBOSE ) System.out.println(" added '" + e + "' as tail element.");
    }
  }
}