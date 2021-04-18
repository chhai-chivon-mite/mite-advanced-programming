
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
			Socket connection = new Socket("rupp.edu.kh", 80);
			System.out.println("Connected successfully.");
			
			// 2. Load IO streams
			OutputStream outputStream = connection.getOutputStream();
			InputStream inputStream = connection.getInputStream();
			
			// 3.1. Send request to the server
			HttpRequest request = new HttpRequest("GET", "/fe/", "");
			request.addHeader("Host", "rupp.edu.kh");
			request.addHeader("User-Agent", "My User Agent");
			request.addHeader("Connection", "closed");
			System.out.println("Request: ");
			System.out.println(request.toRawRequest());
			
			PrintWriter printWriter = new PrintWriter(outputStream);
			printWriter.write(request.toRawRequest());
			printWriter.flush();
			
			// 3.2. Read response from the server
			Scanner scanner = new Scanner(inputStream);
			String rawResponse = "";
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				rawResponse += line + "\r\n";
			}
			scanner.close();
			
			// Process response
			System.out.println("Response: ");
			System.out.println(rawResponse);
			HttpResponse response = new HttpResponse(rawResponse);
			System.out.println("Version: " + response.version);
			System.out.println("Code: " + response.statusCode);
			System.out.println("Reason: " + response.reasonPhrase);
			
			// 4. Close connection
			connection.close();
			
			
		} catch (IOException e) {
			System.out.println("Failed.");
			System.out.println("Reason: " + e.getMessage());
		}
		
	}
	
	
}




















