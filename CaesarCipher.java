
public class CaesarCipher implements Ciphers
{
   public String encrypt(String msg)
   {
      char[] letters = msg.toCharArray();
      for(int i =0; i<msg.length(); i++)
      {
         int temp =(int)letters[i];
         if(temp>=118)
         {
            temp-=118;
            System.out.println(temp);
         }
         letters[i] = (char)(temp + 10);
      }
      return new String(letters);
   }
   public String decrypt(String msg)
   {
      char[] letters = msg.toCharArray();
      for(int i =0; i<letters.length; i++)
      {
         int temp =(int)letters[i];
         if(temp<10)
         {
            temp+=118;
            System.out.println(temp);
         }
         letters[i] = (char)(temp - 10);
      }
      return new String(letters);
   }
}