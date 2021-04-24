import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		
		
		try {
			// 1. Connect to server
			Socket connection = new Socket("13.229.49.95", 9999);
			
			// 2. Send request to the server
			PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
			printWriter.write("CCP/1.0#convert#<USD><50><CNY>");
			printWriter.write("\r\n");
			printWriter.flush();
			
			// 3. Rread response from server
			Scanner scanner = new Scanner(connection.getInputStream());
			String response = scanner.nextLine();
			System.out.println("Response: " + response);
			
			// 4. Close connection
			printWriter.write("CCP/1.0#exit#");
			printWriter.write("\r\n");
			printWriter.flush();
			connection.close();
			
		} catch (IOException e) {
			System.out.println("Connect to server error: " + e.getMessage());
		}
		
		
		
		
	}
	
}
