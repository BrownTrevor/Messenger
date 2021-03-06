import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatServer extends Thread
{
   private ServerSocket ss;
   private Socket socket;
   private static ArrayList<Socket> clients = new ArrayList<Socket>();
   private static ArrayList<DataOutputStream> outs;
   private static ArrayList<DataInputStream> ins;
   private static ArrayList<String> names = new ArrayList<String>();



   public ChatServer(int portNum) throws IOException
   {
      //Binds server socket to the port number
      ss = new ServerSocket(portNum);
      ss.setSoTimeout(10000);
   }

   public void run()
   {
      int count = 0;
      System.out.println("Starting up a new chat server for Brown Panda Messenger.");
      while(true)
      {
         count ++; 

         try
         {
            //Set up
            //Prints the server's port number
            System.out.println("Waiting for people to join the chat.");
            //waits for a connection to be made to the server socket
            Socket server = ss.accept();
            clients.add(server);
            

            if(clients.size()%2==1)
            {
               System.out.println("Connection established between server socket and " 
               + server.getRemoteSocketAddress());
               DataOutputStream out = new DataOutputStream(server.getOutputStream());
               out.writeUTF("Thanks for connecting to " + server.getLocalSocketAddress() 
                  + "\n\tPlease wait for other clients to connect...");
            }

         }
         catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            System.out.println("All clients accepted...");

            break;
            
         }
         catch(IOException ieo)
         {
            ieo.printStackTrace();
            break;
         }
      }

      //Start chat
      boolean running = true;

      System.out.println("\nChat Started...\nPress Enter or send a message to recieve messages\nChat Members:");
      for(int i = 0; i < clients.size(); i+=2)
      {
         System.out.println(clients.get(i).getRemoteSocketAddress());
      }

      outs = new ArrayList<DataOutputStream>();
      ins = new ArrayList<DataInputStream>();

      try
      {
         for(int i = 0; i < clients.size(); i++)
         {
   
            outs.add(new DataOutputStream(clients.get(i).getOutputStream()));
            ins.add(new DataInputStream(clients.get(i).getInputStream()));
         }
         for(int i = 0; i < ins.size()-1; i+=2)
         {
            names.add(ins.get(i).readUTF())  ;
         }
         for(int i = 0; i < outs.size(); i++)
         {
            outs.get(i).writeUTF(names.get((i-1)/2) + ", your Brown Panda Messenger chat session has begun. Type your message and then press 'Enter' to send.\n");
         }
         System.out.println("\nLogging User Chat...\n");
         while(running && clients.size() !=0)
         {
            String listen = "";
            int messageFrom = -1;
            for(int i = 0; i < ins.size(); i++)
            {
               clients.get(i).setSoTimeout(100);
               try
               {
                  listen = ins.get(i).readUTF();
                  System.out.println("\n" +listen);
                  if(!listen.equals(""))
                  {
                     messageFrom = i;
                  }
               }
               catch(SocketTimeoutException e)
               {

               }
               catch(SocketException se)
               {
                  System.out.println("Removing socket "+ i);
                  ins.remove(i);
                  outs.remove(i);
                  if(ins.size()==0)
                  {
                     System.exit(0);
                  }
               }
            }


            if(!listen.equals(""))
            {
               for(int i = 1; i < outs.size(); i+=2)
               {  
                  if(i-1 == messageFrom)
                  {
                     continue;
                  }

                  System.out.println("Writing" + listen+" to " + i);
                  outs.get(i).writeUTF(listen+"\n");
               }

            }
         } 

      }
      catch(IOException ioe)
      {
         //ioe.printStackTrace();
         System.out.println("Closing...");
      }


      

      try
      {
         
         //close all sockets
         for(int i = 0; i < clients.size(); i++)
         {
            clients.get(i).close();
         }
      }
      catch(IOException ioe)
      {
         System.out.println("Error closing sockets...");
      }
   }

  





   public static void main(String args [])
   {
      try
      {
      int port = Integer.parseInt(args[0]);
      
         Thread t = new ChatServer(port);
         t.start();
      }
      catch(IOException ieo)
      {
         ieo.printStackTrace();
      }
      catch(IndexOutOfBoundsException e)
      {
         System.out.println("Provide a valid port as argument...");
      }
      catch(NumberFormatException e)
      {
         System.out.println("Provide a valid port as argument...");
      }
   }
}