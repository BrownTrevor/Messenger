
import java.util.List;
import java.util.Arrays;
public class CaesarCipher implements Ciphers
{
   public String encrypt(String msg)
   {
      char[] letters = msg.toCharArray();
      String temp = new String("");
      for(int i =0; i<msg.length(); i++)
      {
         temp +=(int)letters[i] + ",";
      }
      return temp;
   }
   public String decrypt(String msg)
   {
      List<String> items = Arrays.asList(msg.split("\\s*,\\s*"));
      String temp = new String("");
      for(int i =0; i<items.size(); i++)
      {
         try
         {
            int l = Integer.parseInt(items.get(i));
            temp+= (char)l;
         }
         catch(Exception e)
         {
         }
      }
      return temp;
   }
}
