import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	public static void main(String[] args) {
		
		try {
			// 1. Bind to port 9999
			ServerSocket serverSocket = new ServerSocket(9999);
			System.out.println("Bind success");
			
			while(true) {
				// 2. Wait and accept connection from client
				System.out.println("Waiting connection from client...");
				Socket connection = serverSocket.accept();
				
				// 3. Load IO Streams
				InputStream inputStream = connection.getInputStream();
				OutputStream outputStream = connection.getOutputStream();
				
				// 4.1 Read request from the client
				Scanner scanner = new Scanner(inputStream);
				String request = scanner.nextLine();
				System.out.println("Request: " + request);
				
				// 4.2. Send response to the client
				PrintWriter printWriter = new PrintWriter(outputStream);
				printWriter.write("Hello client\n");
				printWriter.flush();
				
				// 5. Close connection
				scanner.close();
			}
			
		} catch (IOException e) {
			System.out.println("Bind error: " + e.getMessage());
		}
		
	}

}
