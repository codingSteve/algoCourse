

    PriorityQueue pq …
    for ( record r : records ) {
      Pq.offer( newLeafFromRecord( r ) )
    } 
    
    Leaf l = pq.poll();
    while ( 1 != pq.size() ) {
      Leaf merged = merge( l, pq.poll() ) ;
      pq.offer ( merged );
    }
    
    