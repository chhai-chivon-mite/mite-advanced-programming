import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
	
	
	public static void main(String[] args) {
		
		try {
			// 1. Create a server socket and bind to port 9999
			ServerSocket serverSocket = new ServerSocket(8888);
			
			while(true) {
				// 2. Wait for connection from clients
				System.out.println("Waiting for client...");
				Socket connection = serverSocket.accept();
				
				// 3. Create another thread to handle the connection
				System.out.println("Create another thread to handle the connection.");
				Thread thread = new ClientHandlerThread(connection);
				thread.start();
			}
			
		} catch (IOException e) {
			System.out.println("Cannot bind to port 9999: " + e.getMessage());
		}
		
		
		
	}
	

}









