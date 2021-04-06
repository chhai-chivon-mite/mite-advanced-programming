import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HttpClient {

	public static void main(String[] args) {
	
		
		try {
			// 1. Connect to HTTP Server
			System.out.println("Establish connection to the server...");
			Socket connection = new Socket("192.168.100.3", 80);
			System.out.println("Connected successfully.");
			
			// 2. Load IO streams
			OutputStream outputStream = connection.getOutputStream();
			InputStream inputStream = connection.getInputStream();
			
			// 3.1. Send request to the server
			PrintWriter printWriter = new PrintWriter(outputStream);
			printWriter.write("Hello apache!\r\n");
			printWriter.write("How are you?\r\n");
			printWriter.flush();
			
			// 3.2. Read response from the server
			Scanner scanner = new Scanner(inputStream);
			String response = scanner.nextLine();
			System.out.println("Response: " + response);
			scanner.close();
			
			// 4. Close connection
			connection.close();
			
			
		} catch (IOException e) {
			System.out.println("Failed.");
			System.out.println("Reason: " + e.getMessage());
		}
		
	}
	
}
