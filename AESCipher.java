import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
public class AESCipher {
  static String IV = "AAAAAAAAAAAAAAAA";
  static String encryptionKey = "0123456789abcdef";

   public byte[] encrypt(String val){
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
         return cipher.doFinal(val.getBytes("UTF-8"));
      }
      catch(Exception e)
      {
         System.out.println("Invalid Message");
         return null;
      }
   }

   public String decrypt(byte[] val){
      try
      {
          Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
          SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
          cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
          return new String(cipher.doFinal(val),"UTF-8");
      }
      catch(Exception e)
      {
         return"Invalid Message";
      }
   }
}