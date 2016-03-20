import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import java.util.List;
import java.util.Arrays;
public class AESCipher implements Ciphers {
  static String IV = "AAAAAAAAAAAAAAAA";
  static String encryptionKey = "0123456789abcdef";

   public String encrypt(String val){
      try
      {
         int extra = val.length() % 16;
         if (extra > 0)
         {
            for(int i =0; i<16-extra; i++)
               val +=' ';
         }
         Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
         SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
         cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
         String temp = new String("");
         for(byte b: cipher.doFinal(val.getBytes("UTF-8")))
         {
          temp+=b + ",";
         }
         
         return temp;
      }
      catch(Exception e)
      {
         System.out.println("Invalid Message to encrypt");
         return null;
      }
   }

   public String decrypt(String val){
      try
      {
         List<String> items = Arrays.asList(val.split("\\s*,\\s*"));
         byte[] msg = new byte[items.size()];
         for(int i=0; i<items.size();i++)
         {
          msg[i]= new Byte(items.get(i));

         }
          Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
          SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
          cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
          return new String(cipher.doFinal(msg),"UTF-8");
      }
      catch(Exception e)
      {
         return val;
      }
   }
}