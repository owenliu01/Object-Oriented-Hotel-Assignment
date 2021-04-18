import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Creates a hotel server that allows multiple users to connect at once. Allows bookings for the hotel.
 * 
 * @author Owen Liu
 * Student Number: 100321041
 * Date:July 30,2019
 * CPSC 1181-001
 * Lab 10
 */
public class HotelServer{
	    public static final int PORT = 1181;//Standard port for users
	    ServerSocket server;
	    private static Hotel serverHotel = new Hotel();//Hotel for all users
	    
	    public static void main(String[] args)   {
		   (new HotelServer()).run();
		}
		
	    /**
	     * Runs server and says when the a client has connected.
	     */
	    public void run() {
		   try {
		      server = new ServerSocket(PORT);
		      System.out.println("Server has Started.");
		      while (true)
		      {
		         Socket s = server.accept();
		         System.out.println("User has connected to the Hotel server.");
		         Thread t = new Thread(new ClientHandler(s));
		         t.start();
		      }		   
		   } catch (IOException e) {}
	   }
	   
	   /**
	    * Sub class to Handle clients actions between the server. Separate handle for each client.
	    *
	    */
	   public class ClientHandler implements Runnable {
		   private final Socket s;//Standard socket to connect
		   private String userName;//users desired name
		   private String command;//users commands
		   
		   /**
		    * Creates the handle with the given socket
		    * @param s Clients socket that connects them
		    */
		   public ClientHandler(Socket s) {
			   this.s = s;
		   }
		   
		   /**
		    * Display Menu to user of what he can do.
		    * @return The menu to be displayed
		    */
		    public String menu(){
		    	String display = "What would you like to do? \n";
		    	display += "1. User \n";
		    	display += "2. Reserve \n";
		    	display += "3. Cancel \n";
		    	display += "4. Avail \n";
		    	display += "5. Quit \n";
		    	return display;
		    }
		   
		    /**
		     * Runs until the user is done with everything. Allows the user to talk with the server. Takes in 
		     * commands from the user and performs the specified actions.
		     */
		   public void run() {
		         try (Socket s2 = s)   {
		            DataInputStream in = new DataInputStream(s2.getInputStream());
		            DataOutputStream out = new DataOutputStream(s2.getOutputStream());
	                out.writeUTF("Hello, please set your name using, 'USER'.");
	                out.flush();
	                
	                command = in.readUTF();
	                if(!command.equalsIgnoreCase("USER")) {
	                	s2.close();
	                }else {
	                	out.writeUTF("Hello, what is your name? ");
	                	out.flush();
	                	userName = in.readUTF();
	                	out.writeUTF("Hello, " + userName + "\n" + menu());
	                	out.flush();
	                }
	                
	                while(true) {
	                	command = in.readUTF();
	                	switch(command) {
	                		case "1":
	                			out.writeUTF("What is your new name?");
	                			out.flush();
	                			userName = in.readUTF();
	                			out.writeUTF("Hello, " + userName);
	                			out.flush();
	                			break;
	                		case "2":
	                			int firstDay;
	                			int lastDay;
	                			out.writeUTF("What is your first day you would like to book in the month of August?");
	                			firstDay = Integer.parseInt(in.readUTF());
	                			out.writeUTF("Whats is the last day you would like stay at the hotel?");
	                			lastDay = Integer.parseInt(in.readUTF());
	                			out.flush();
	                			if(serverHotel.makeReservation(userName,firstDay,lastDay)) {
	                				out.writeUTF("Reservation made: " + userName + " from " + firstDay + "-" + lastDay);
	                			}else {
	                				out.writeUTF("Reservation unsuccessful: " + userName + " from " + firstDay + "-" + lastDay);
	                			}
	                			out.flush();
	                			break;
	                		case "3":
	                			if(serverHotel.cancelReservation(userName)) {
	                				out.writeUTF("Reservation cancelled for " + userName);
	                			}else {
	                				out.writeUTF("No reservation to cancel for " + userName);
	                			}
	                			out.flush();
	                			break;
	                		case "4":
	                			out.writeUTF(serverHotel.reservationInformation());
	                			out.flush();
	                			break;
	                		case "5":
	                			s2.close();
	                			break;
	                	}
	                }
		         } catch (IOException e) { System.out.println("User Disconnected");}
		   }
	   }
	}

