

public class UnorderedList<T extends Comparable<T>> extends OrderedList<T> {
	
	@Override
	public void add( T e ) {
		OrderedList<T> newNode = new UnorderedList<>();
		newNode._rest  = this._rest;
		newNode._first = this._first;
		this._first    = e;
		this._rest     = newNode;
	}
}