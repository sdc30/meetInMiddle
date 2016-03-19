import java.util.*;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.math.BigInteger;

public class meetInMiddle extends DoubleDES{

// key 0000000000000011111111111111
// plain 0123456789ABCDEF
// cipher 3057b90bd52bae5e1dc92a4ef6dd9775

// key 0123456789ABCDEF0123456789AB
// plain ABCDEF0123456789
// cipher 3ec465c9a5ffd9d3d52ffb42997934b0

// key ????1111111111??????22222222
//String master = "1ab5111111111189fe3322222222", enc, dec,
// plain 48656c6c6f20576f726c6421
// cipher e89d327477bd5da2f84bcc6d016617d2

 	public static volatile boolean keepOn = true;
	
	private static final HashMap<String, Integer> encrypted = new HashMap<String, Integer>();
	
	private static final ArrayList<String> list = new ArrayList<String>();
	
	public meetInMiddle(){
		
		String one = "1111111111", two = "22222222";
		String key = "0000" + one + "000000" + two, enc, dec,  keys = "";
		
		// CT second round
		// plain is getting hex bytes rep

		String plain = "48656c6c6f20576f726c6421", cipherText = "e89d327477bd5da2f84bcc6d016617d2";
		System.out.println("working");
		
		
        DoubleDES d = new DoubleDES();

	
	 for(int i = 0; i < 0XFFFF + 1; i++){


	  	StringBuilder sb = new StringBuilder(key);

	  			sb.replace(0, 4, pad1(Integer.toString(i, DoubleDES.hexRad), 4));
	  			keys = sb.toString();

	          	enc =  d.getEnc(plain, keys, 1);
	                 if(enc != null) encrypted.put(enc, i);


	  }

	     //
	     // System.out.println("next loop now");
	 for(int i = 0; i < 0xFFFFFF + 1; i++){

	  		StringBuilder sb = new StringBuilder(key);

	  			sb.replace(14, 20, pad1(Integer.toString(i, DoubleDES.hexRad), 6));
	  			keys = sb.toString();
				

	                dec = d.getDec(cipherText, keys, 1);
					
					if(encrypted.containsKey(dec)){					  
										  sb = new StringBuilder(keys);
										  sb.replace(0, 4, pad1(Integer.toString(encrypted.get(dec), DoubleDES.hexRad), 4));	  
										  list.add(sb.toString());
	                                          print();
	                                  }


	  }
			
	}
	
	public static String getStr(){
		return new String();
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("program is started");
		meetInMiddle m = new meetInMiddle();

		

	}
			

	public static String pad1(String s, int x){
		StringBuilder sb = new StringBuilder(s);
		int len = s.length();
			for(int i = 0; i < (x - len); i++)
					sb.insert(0, '0');
			
			
			return sb.toString();
		
	}
	
	public static String pad(String s){
		StringBuilder sb = new StringBuilder(s);
		int len = s.length();
			for(int i = 0; i < (DoubleDES.bits56 - len); i++)
					sb.insert(0, '0');
			
			
			return sb.toString();
	}
	

	public static void print(){

		
        File file = new File("hashes.txt");
		PrintWriter pw = null;
        try {
             pw = new PrintWriter(file);

			for(String key: list){
				pw.println(key);

 			}

			
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally
        {
            if ( pw != null ) 
            {
                pw.close();
                System.exit(0);
		
            }
        }
		

	}
}