public class Driver
{
	public static void main(String[] args) throws Exception
	{
		encrypt("hello world", new CaesarCipher());
		encrypt("hello world", new AESCipher());
		//encrypt("hello world", new PolybiusCipher());
	} 
	private static void encrypt(String msg, AESCipher cipher)
	{
		String temp = cipher.encrypt(msg);
		System.out.println(cipher.decrypt(temp));
	}
	private static void encrypt(String msg, Ciphers cipher)
	{
		String temp = cipher.encrypt(msg);
		System.out.println(temp);
		System.out.println(cipher.decrypt(temp));
	}

}