import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient
{
   private static Socket clientWrite;
   private static Socket clientRead;
   private static boolean running1=true;
   private static boolean running2=true;
   private static ThreadReceive thread1;
   private static ThreadSend thread2;
   private static int cipherNum;
   private static Ciphers cipher; 


   public static void main(String args [])
   { 
      try
      {

         String serverName = args[0];
         int port = Integer.parseInt(args[1]);
         cipherNum = Integer.parseInt(args[2]);
         
         switch(cipherNum)
         {
            case 0: cipher = new BlankCipher();  //replace with plain text cipher
            break;

            case 1: cipher = new CaesarCipher();
            break;

            case 2: cipher = new AESCipher();  //AESCipher needs to be supported by the ciphers interface
            break;

            default: //cipher = plainTextCipher;
               System.out.println("Invalid cipher choice. Choose 0 for Blank Cipher, 1 for Caesar Cipher, or 2 for AESCipher. The format for starting a client is: java ChatClient 'server name' 'server port number' 'cipher number'. Please try again.");
            break;
         }

        

         boolean running = true;

         System.out.println("Connecting to " + serverName + " on port " + port);
         clientWrite = new Socket(serverName, port);
         clientRead = new Socket(serverName, port);
         System.out.println("Successfully connected to " + clientWrite.getRemoteSocketAddress());
         System.out.println();
    /*     OutputStream outToServer2 = clientWrite.getOutputStream();
         DataOutputStream out2 = new DataOutputStream(outToServer2);
         
         InputStream inFromServer2 = clientRead.getInputStream();
         DataInputStream in2 = new DataInputStream(inFromServer2); */


        
         thread1 = new ThreadReceive();
         thread2 = new ThreadSend();

         thread1.start();
         thread2.start();


         
      }
      catch(IOException ioe)
      {
         ioe.printStackTrace();
      }
      catch(IndexOutOfBoundsException e)
      {
         System.out.println("Too many arguments provided on command line...");
      }
      catch(NumberFormatException e)
      {
         System.out.println("Error initializing ChatClient\njava ChatClient [ServerName] [ServerPortNumber] [Cipher Number]");
      }
   }
   private static class ThreadSend extends Thread
   {

      public void run()
      {
         try
         {
            Scanner ui = new Scanner(System.in);

            OutputStream outToServer = clientWrite.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            while(running1)
            {
               String message = "";
                  message = ui.nextLine();

               if(message.equals("QUIT"))
               {
                  running1 = false;
                  out.writeUTF(clientWrite.getLocalSocketAddress() + ": Goodbye!");
                  break;
               }
               else if(!message.equals(null)&&!message.equals(""))
               {

                  out.writeUTF(cipher.encrypt(clientWrite.getLocalSocketAddress() + ": " +message));
               }
               
            }
         }catch(IOException ioe)
         {
            System.out.println("Error in send");
            ioe.printStackTrace();
            running1 = false;
         }
         catch(Exception e)
         {}
         
          try
         {
            clientWrite.close();
            running2=false;
            clientRead.close();
         }
         catch(IOException e)
         {
            System.out.println("Error closing socket...");
         }
      }

   }

   private static class ThreadReceive extends Thread
   {

      public void run()
      {  
         try
         {
            InputStream inFromServer = clientRead.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            while(running2)
            {
               try
               {
                  String recieved = in.readUTF();
                  if(recieved!=null && recieved!="")
                  {
                     System.out.println(cipher.decrypt(recieved));
                  }
               }catch(IOException ioe)
               {
                  System.out.println("Exited in loop");
                  running2 =false;
               }
            }
         }catch(IOException ioe)
         {
            running2 = false;
         }
         try
         {
            clientRead.close();
            running1=false;
            clientWrite.close();
         }
         catch(IOException e)
         {
            System.out.println("Error closing socket...");
         }

      }

   }

}

