

public class Satisfaction{

  public static void main( String[] ARGV ) throws Exception {
    int times = 1;
    for ( int i =0, l= ARGV.length; i < l; i++){
      if ( "--times".equals( ARV[i] )) times = Integer.valueOf( ARGV[i] ).intValue();
      
    }
  }

public boolean solve ( int variables, Boolean[] instance, Predicate[] conditions ) { 

for ( int i = (int) Math.log( variables, 2); --i>=0;)
  for ( int j = (int) 2*Math.pow( variables, 2); --j>=0) {
    fixAFailingCondition( conditions );
    if ( conditions.stream().reduce(true, (x,y) -> x && y ) ) return true;
    
  }
    
return false ; 

} 

  private void fixAFailingCondition( Predicate[] conditions ) {
  
  Option<Condition> failure conditions.stream().filter( failingCondition ).findAny(); 
  
  
  
  
  }
  
  private class Condition{
    final Predicate _1;
    final Predicate _2;
    
    final int _v1;
    final int _v2;
    
    Condition( boolean b1, int v1, boolean b2, int v2, boolean[] variables ){
      _v1 = v1;
      _v2 = v2;
      
      _1 = () -> (b1)? variables[v1] : !  variables[v1];
      _2 = () -> (b2)? variables[v2] : !  variables[v2];
    }
    
    boolean test() {
      return _1.test() && _2.test();
    }
  }

}
