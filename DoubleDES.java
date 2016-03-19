/*
Cartwright, Stephen D
CMPSC 443 Lab 1
2/2/2016

*/


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESKeySpec;
import java.lang.StringBuilder;
import java.util.Arrays;


public class DoubleDES
{ 
   	
   
	  // PKCS5Padding Pass 1
      // NoPadding Pass 2
	  
	  // Char array holding hex values
	  final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	  final static int hexRad = 16;
	  final static int binRad = 2;
	  final static int bits56 = 56;
	  static String firstEnc = null, secondEnc = null, firstDec = null, secondDec = null;
   	  private static byte[] keyFin_1, keyFin_2;
  	  private static String pl, ci;
	  
	  // Constructor for DoubleDES
	  public DoubleDES(){
//		  String enc = getEnc(plain, key);
//		  String dec = getDec(enc, key);

	  					
	  }
	  
	  
	  // Encryption method taking message and key (k)
	  // 		  hexStr_1, hexStr_2 take substrings of k, splitting it into 14 hex (56 bytes)
	  // 		  encrypt_1, encrypt_2 recieve values of encryption after the first and second passes
	  // 		  byteKeyPart_1, byteKeyPart_2 recieve the byte array of the first and second hex parts (8 bytes)
	  // 			  hex string -> hex byte array -> binary array -> parity adjust -> binary byte array
	  // 		  messageBytes - message in hex byte array
	  // 		  DESKeySpec desKeyEnc_1, desKeyEnc_2 - first and second keys respective keyspecs
	  //	  
	  
	  public static String encrypt(String message, String k, int ret) throws IllegalBlockSizeException,
	  	    BadPaddingException, NoSuchAlgorithmException,
	  	    NoSuchPaddingException, InvalidKeyException, 
	  	    UnsupportedEncodingException {
				
				Cipher cipher_1, cipher_2;
				String encrypt_1 = "", encrypt_2 = "", hexStr_1 = k.substring(0, k.length()/2), 
					hexStr_2 = k.substring(k.length()/2);
				byte[] byteKeyPart_1, byteKeyPart_2, messageBytes;
				SecretKey key_1, key_2;
				SecretKeyFactory skf_1, skf_2;
				DESKeySpec desKeyEnc_1, desKeyEnc_2;

			    keyFin_1 = byteKeyPart_1 = fromBinary(binStrAdj(arrToBinary(getBytesFromHexStr(hexStr_1))));
				keyFin_2 = byteKeyPart_2 = fromBinary(binStrAdj(arrToBinary(getBytesFromHexStr(hexStr_2))));

				try {
					
		 	 	skf_1 = SecretKeyFactory.getInstance("DES");
	 		  	desKeyEnc_1 = new DESKeySpec(byteKeyPart_1, 0);   
	 	  		cipher_1 = Cipher.getInstance("DES/ECB/PKCS5Padding");	
					
			    key_1 = skf_1.generateSecret(desKeyEnc_1);
				cipher_1.init(Cipher.ENCRYPT_MODE, key_1);
				messageBytes = getBytesFromHexStr(message);
			
				messageBytes = cipher_1.doFinal(messageBytes);
				encrypt_1 = getHexFromByteArr(messageBytes);
				
				if(ret == 1) return encrypt_1;
				//print("DES Encrypt Round 1: " + encrypt_1);
				
				skf_2 = SecretKeyFactory.getInstance("DES"); 
				desKeyEnc_2 = new DESKeySpec(byteKeyPart_2, 0);
				key_2 = skf_2.generateSecret(desKeyEnc_2);
				cipher_2 = Cipher.getInstance("DES/ECB/NoPadding");
		
				cipher_2.init(Cipher.ENCRYPT_MODE, key_2);
				messageBytes = cipher_2.doFinal(messageBytes);
					
				encrypt_2 = getHexFromByteArr(messageBytes);
				//print("DES Encrypt Round 2: " + encrypt_2);
				} catch (Exception excep2) {excep2.printStackTrace(); }
				
			return encrypt_2;
	  }
	  
	  // Decryption method taking message and key (k)
	  // 		  hexStr_1, hexStr_2 take substrings of k, splitting it into 14 hex (56 bytes)
	  // 			  using the hex keys in reverse order to decrypt
	  // 		  decrypt_1, decrypt_2 recieve values of edecryption after the first and second passes
	  // 		  byteKeyPart_1, byteKeyPart_2 recieve the byte array of the first and second hex parts (8 bytes)
	  // 			  hex string -> hex byte array -> binary array -> parity adjust -> binary byte array
	  // 		  messageBytes - message in hex byte array
	  // 		  DESKeySpec desKeyEnc_1, desKeyEnc_2 - first and second keys respective keyspecs
	  //
	  //
	  
	  public static String decrypt(String encrypted, String k, int ret) throws InvalidKeyException,
	  	    NoSuchAlgorithmException, NoSuchPaddingException,
	  	    IllegalBlockSizeException, BadPaddingException, IOException {
				Cipher decipher_1, decipher_2;
				String decrypt_1 = "", decrypt_2 = "", hexStr_1 = k.substring(0, k.length()/2), 
					hexStr_2 = k.substring(k.length()/2);
				byte[] byteKeyPart_1, byteKeyPart_2, messageBytes;
				SecretKey key_1, key_2;
				SecretKeyFactory skf_1, skf_2;
		
			
				byteKeyPart_1 = fromBinary(binStrAdj(arrToBinary(getBytesFromHexStr(hexStr_2))));
				byteKeyPart_2 = fromBinary(binStrAdj(arrToBinary(getBytesFromHexStr(hexStr_1))));

			try {
				
	 	 	skf_1 = SecretKeyFactory.getInstance("DES");
		  	DESKeySpec desKeyDec_1 = new DESKeySpec(byteKeyPart_1, 0);
			decipher_1 = Cipher.getInstance("DES/ECB/NoPadding");	
		    key_1 = skf_1.generateSecret(desKeyDec_1);
  			decipher_1.init(Cipher.DECRYPT_MODE, key_1);
			
			messageBytes = getBytesFromHexStr(encrypted);
			messageBytes = decipher_1.doFinal(messageBytes);
			
			decrypt_1 = getHexFromByteArr(messageBytes);
			
			if(ret == 1) return decrypt_1;
			print("DES Decrypt Round 1: " + decrypt_1);	
			
			skf_2 = SecretKeyFactory.getInstance("DES");
			DESKeySpec desKeyDec_2 = new DESKeySpec(byteKeyPart_2, 0);
			decipher_2 = Cipher.getInstance("DES/ECB/PKCS5Padding");
			key_2 = skf_2.generateSecret(desKeyDec_2);
			decipher_2.init(Cipher.DECRYPT_MODE, key_2);
			
			messageBytes = decipher_2.doFinal(messageBytes);
			decrypt_2 = getHexFromByteArr(messageBytes);
			print("DES Decrypt Round 2: "+ decrypt_2);
			
			} catch (Exception excep2) {excep2.printStackTrace(); }
			
		return decrypt_2;
	  
	  }
	  
 	 //	 binary to hex method
 	 // 		  takes binary string of length 56 bits
 	 // 			  takes substrings of every 2 hex
 	 //  
	  
	  public static String binToHex(String bin){
		  assert bin.length() == bits56;
		StringBuilder hex = new StringBuilder();
		String s = null;
			 for(int i = 0; i < bin.length()-1; i = i + binRad){
				  s = bin.substring(i, i + 1);
				  hex.append(Short.parseShort(s, hexRad));
					  
			 } 
			  
			return hex.toString();
		
	  }
	  
	  // get bytes from hex string
	  // 	take in a string and parse every 2 hex digits into a byte
	  //
	  // 
	  
	  public static String getHexFromByteArr(byte[] bytes) {
	      char[] hex = new char[bytes.length * 2];
	      for (int i = 0; i < bytes.length; i++) {
	          int bits = bytes[i] & 0xFF;
	          hex[i * 2] = hexArray[bits >>> 4];
	          hex[i * 2 + 1] = hexArray[bits & 0x0F];
	      }
	      return new String(hex);
	  }    
	  
	  // get bytes from hex str
  	  // 		  take in hex string and build byte array

      public static byte[] getBytesFromHexStr(String s){
		  
		  int len = s.length();
		  byte f = 0xF;
		  byte[] temp = new byte[len], out = new byte[len/2];
		 	  
		  for(int i = 0; i < len; i++){
		  	if(i % 2 == 0) temp[i] = (byte)((Byte.parseByte(s.substring(i, i+1), hexRad) & f) << 0x4);
			else if (i % 2 == 1) temp[i] = (byte)(Byte.parseByte(s.substring(i, i+1), hexRad) & f) ; 
			
		  }	
		  
		  for(int i = 0; i < len/2; i++){
			  out[i] = (byte)(temp[2*i] | temp[2*i + 1]);
		  }	
		  
			return out;
      }	
		  
	  public static byte[] fromBinary(String s)
	  {
	      int len = s.length();
	      byte[] out = new byte[(len + Byte.SIZE - 1) / Byte.SIZE];
	      char c;
	      for( int i = 0; i < len; i++ )
	          if( (c = s.charAt(i)) == '1' )
	              out[i / Byte.SIZE] = (byte) (out[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
	          else if ( c != '0' )
	              throw new IllegalArgumentException();
	      return out;
	  }			 
		 
		  
	  public static  String arrToBinary(byte[] bytes){
	      StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
	      for(int i = 0; i < Byte.SIZE * bytes.length; i++)
	          sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
	      return sb.toString();
	  }
	  
	  // binary string adjust 
	  // We start a loop from the beginning to end moving up every 7 bits
	  // we check the string to see if when we remove a 1 if it is an even or odd number
	  // depending on the answer we will add a 1 or not as parity
	  
	  public static  String binStrAdj(String bin){
				  
				  StringBuilder binary = new StringBuilder();
		  		  String s;
				  int len;
				  for(int i = 0; i < bin.length(); i+= Byte.SIZE-1){
					  s = bin.substring(i, i + (Byte.SIZE-1));
					  
					  StringBuilder build = new StringBuilder(s);
					  len = s.length() - s.replace("1", "").length();
					  if(len % 2 == 0) binary.append(build.append('1'));
					  else if (len % 2 == 1) binary.append(build.append('0'));
					  				  
				  } 
				  
			return binary.toString();  
	  }
	  
	  public static String hexToAscii(String hex){
		  StringBuilder output = new StringBuilder();
		  String str;
		      for (int i = 0; i < hex.length(); i+=2) {
		           str = hex.substring(i, i+2);
		           output.append((char)Integer.parseInt(str, 16));
		      }
		  return output.toString();
	  }
	  
	  public static  String getEnc(String plain, String key, int ret) {
		String s = null;
  		try {
			
				s = encrypt(plain, key, ret);
  
  			} catch (NoSuchAlgorithmException e) {
  				e.printStackTrace();
  			} catch (NoSuchPaddingException e) {
  				e.printStackTrace();
  			} catch (InvalidKeyException e) {
  				e.printStackTrace();
  			} catch (IllegalBlockSizeException e) {
  	  	  		e.printStackTrace();
  			} catch (BadPaddingException e) {
  	  	  		e.printStackTrace();
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
		  
		  
			return s;
	  }
	  
	  public static  String getDec(String encrypted1, String key, int ret) {
		  
		  String s = null;
		  
    		try {
				
  				s = decrypt(encrypted1, key, ret);
             
                
    			} catch (NoSuchAlgorithmException e) {
    				e.printStackTrace();
    			} catch (NoSuchPaddingException e) {
    				e.printStackTrace();
    			} catch (InvalidKeyException e) {
    				e.printStackTrace();
    			} catch (IllegalBlockSizeException e) {
    	  	  		e.printStackTrace();
    			} catch (BadPaddingException e) {
    	  	  		e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
				
				return s;
	  }
	  

	  
      public static void print(String s) {
		  System.out.println(s);
      }
	  
	  public static void pBytes(byte[] bytes) {
		 print(Arrays.toString(bytes));
	  }


	  
}

