import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Automatically tries to reserve at the hotel multiple times. Disconnects after 6 tries at booking. Books random days 
 * with the given name.
 * 
 * @author Owen Liu
 * Student Number: 100321041
 * Date:July 30,2019
 * CPSC 1181-001
 * Lab 10
 */
public class HotelAutoClient {

	public static void main(String[] args) throws IOException{
	      Socket s = new Socket("2001:569:71b3:a00:b4e9:511:1676:f5bf", HotelServer.PORT);

	      InputStream instream = s.getInputStream();
	      OutputStream outstream = s.getOutputStream();

	      DataInputStream in = new DataInputStream(instream);
	      DataOutputStream out = new DataOutputStream(outstream);
	      String command="";
	      String response = in.readUTF();
	      System.out.println("Receiving: " + response);
	      
	      out.writeUTF("User");
	      out.writeUTF("Bob");     
	      out.flush();
	      command = "Name \n";
	      System.out.println("Sending: " + command);
	      
	      response = in.readUTF();
	      System.out.println("Receiving: " + response);
	      command = "Name \n";
    	  System.out.println("Sending: " + command);
	      
    	  response = in.readUTF();
	      System.out.println("Receiving: " + response);
	      command = "Sending: Reserve \n";
	      System.out.println(command);
	      
	      //Loops 6 times and tries to book each time with random dates
	      for(int i = 0; i < 6; i ++) {
	    	  int firstDay = (int)(Math.random() * 32);
	    	  int lastDay = (int)(Math.random() * 32);
	    	  while(lastDay < firstDay) {
	    		  lastDay = (int)(Math.random() * 32);
	    	  }
		      
	    	  out.writeUTF("2");
	    	  out.writeUTF(Integer.toString(firstDay));
	    	  out.writeUTF(Integer.toString(lastDay));
	    	  out.flush();    
	      	  response = in.readUTF();
		      System.out.println("Receiving: " + response);
		      if(i != 5) {
		    	  command = "Day \n";
		    	  System.out.println("Sending: " + command);
		      }
	      }
	      command = "QUIT \n";
	      System.out.println("Sending: " + command);
	      out.writeUTF("5");
	      out.flush();   
	      s.close(); 
	   }
	}

