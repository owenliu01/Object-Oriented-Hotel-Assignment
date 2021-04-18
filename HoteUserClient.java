import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * Allows the user to connect to the Hotel server and start making reservations, cancels, or changing their name until 
 * they are finished.
 * 
 * @author Owen Liu
 * Student Number: 100321041
 * Date:July 30,2019
 * CPSC 1181-001
 * Lab 10
 */
public class HoteUserClient {
	
	public static void main(String[] args) {
		Scanner userIn = new Scanner(System.in);
		try (Socket s = new Socket("2001:569:71b3:a00:b4e9:511:1676:f5bf", HotelServer.PORT)){
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			String text = "";
			while(s.isConnected()) {
				text = in.readUTF();
				System.out.println(text);
				text = userIn.nextLine();
				out.writeUTF(text);
				out.flush();
			}
		}catch(IOException e) {e.printStackTrace();}
	}
}
