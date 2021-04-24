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
			Socket connection = new Socket("localhost", 9999);
			System.out.println("Connected successfully.");
			
			// 2. Load IO streams
			OutputStream outputStream = connection.getOutputStream();
			InputStream inputStream = connection.getInputStream();
			
			// 3.1. Send request to the server
			PrintWriter printWriter = new PrintWriter(outputStream);
			
			HttpRequest request = new HttpRequest("GET", "/fe/", "");
			request.addHeader("Host", "rupp.edu.kh");
			request.addHeader("Connection", "Close");
			
			printWriter.write("CCP/1.0#list#\r\n");
			printWriter.flush();
			
			// 3.2. Read response from the server
			Scanner scanner = new Scanner(inputStream);
			String rawResponse = "";
			while(scanner.hasNextLine()) {
				rawResponse += scanner.nextLine() + "\r\n";
			}
			System.out.println(rawResponse);
			HttpResponse response = new HttpResponse(rawResponse);
			System.out.println("Version: " + response.getHttpVersion());
			System.out.println("Status code: " + response.getStatusCode());
			System.out.println("Reason phrase: " + response.getReasonPhrase());
			System.out.println("Headers: " + response.getHeaders());
			System.out.println("Body: " + response.getBody());
			
			
			scanner.close();
			
			// 4. Close connection
			connection.close();
			
			
		} catch (IOException e) {
			System.out.println("Failed.");
			System.out.println("Reason: " + e.getMessage());
		}
		
	}
	
	
}









