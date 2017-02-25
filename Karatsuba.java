import java.util.*;
import java.math.*;

public class Karatsuba {
	private final static String ZEROS = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

	private static final String a = "8539734222673567065463550869546574495034888535765114961879601127067743044893204848617875072216249073013374895871952806582723184";
	private static final String m = "3141592653589793238462643383279502884197169399375105820974944592";
	private static final String n = "2718281828459045235360287471352662497757247093699959574966967627";

	public static void main(String[] ARGV){
		if (ARGV[0].equals("Q")) {
			final String a_ = k(m,n);
			System.out.println("T " + a_.equals(a));
			System.out.println("* " + a_);
		}	
		else if (ARGV[0].equals("E")) { 
			final String a_ = k("1234","5678");
			System.out.println("T " + a_.equals("7006652"));
			System.out.println("* " + a_);
		}
		else 
			System.out.println("* " + k(ARGV[0], ARGV[1]));
	}

	public static String k(String m, String n) {
		System.out.print("Starting with "); Utils.logStrings(m,n);
		if ( m.length() <= 2 && n.length() <= 2 ) { 
			int m_ = Integer.valueOf(m).intValue();
			int n_ = Integer.valueOf(n).intValue();
			System.out.print("returning");
			Utils.logInts( m_, n_, m_* n_ );
			return "" + m_ * n_;
		}
		
		if ( m.length() == 0 || n.length() == 0 ) { 
			return "0";
		}
		
		int mLength = m.length();
		int nLength = n.length();

		final String a = m.substring(0,mLength/2);
		final String b = m.substring(mLength/2);
		final String c = n.substring(0, nLength/2);
		final String d = n.substring(nLength/2);

		final String ac = k(stripLeadingZeros(a), stripLeadingZeros(c)); 
		final String bd = k(stripLeadingZeros(b), stripLeadingZeros(d)); 

		final String e  = new BigInteger((!a.isEmpty()) ? a : "0").add(new BigInteger((!b.isEmpty())? b: "0")).toString();
		final String f  = new BigInteger((!c.isEmpty()) ? c : "0").add(new BigInteger((!d.isEmpty())? d: "0")).toString();

		// logStrings("a,b,e==",a, b, e);
		// logStrings("c,d,f==",c, d, f);
		// logStrings("k(e,f) == ", k(e,f));

		final String ef = new BigInteger(stripLeadingZeros(e)).multiply(new BigInteger(stripLeadingZeros(f)))
							.subtract(new BigInteger(bd))
							.subtract(new BigInteger(ac))
							.toString();

		// logStrings("ac, bd, ef", ac, bd, ef);

		int twoN = (mLength - a.length())  +  (nLength - c.length()) ;

		final BigInteger termOne   = new BigInteger(ac + ZEROS.substring(0, twoN ));
		final BigInteger termTwo   = new BigInteger(bd);
		final BigInteger termThree = new BigInteger(ef + ZEROS.substring(0, twoN/2));

		Utils.logStrings("t1, t2, t3", termOne.toString(), termTwo.toString(), termThree.toString());

		return stripLeadingZeros( termOne.add(termTwo).add(termThree).toString() );
	}

	public static String stripLeadingZeros(String s) { 
		final String s_ = s.replaceFirst("^0+", "");
		return (s_.isEmpty()) ? "0" : s_;
	}


}