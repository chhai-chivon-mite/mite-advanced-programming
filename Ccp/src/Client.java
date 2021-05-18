import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		
		
		try {
			// 1. Connect to server
			Socket connection = new Socket("13.229.49.95", 9999);
			PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
			Scanner scanner = new Scanner(connection.getInputStream());
			
			// 2. Show options to user
			System.out.println("Welcome to CCP Client.");
			
			// 3. Read input from user
			InputStream keyboardInputStream = System.in;
			Scanner keyboardScanner = new Scanner(keyboardInputStream);
			
			while(true) {
				System.out.println("\nEnter:");
				System.out.println(" - L to list all supported currencies");
				System.out.println(" - C to convert currency");
				System.out.println(" - E to Exit.");
				System.out.print("Enter here: ");
				String userInput = keyboardScanner.nextLine();
				
				// 4. Process input
				if(userInput.toUpperCase().equals("L")) {
					sendRequestToServer(printWriter, "CCP/1.0#list#");
					readResponseFromServer(scanner);
				} else if(userInput.toUpperCase().equals("C")) {
					// Ask user to enter more info
					System.out.print("Please enter source currency: ");
					String sourceCurrency = keyboardScanner.nextLine();
					System.out.print("Please enter destination currency: ");
					String destinationCurrency = keyboardScanner.nextLine();
					System.out.print("Please enter amount: ");
					String amount = keyboardScanner.nextLine();
					//String request = "CCP/1.0#convert#<" + sourceCurrency + "><" + amount + "><" + destinationCurrency + ">";
					String request = String.format("CCP/1.0#convert#<%s><%s><%s>", sourceCurrency, amount, destinationCurrency);
					sendRequestToServer(printWriter, request);
					readResponseFromServer(scanner);
				} else if(userInput.toUpperCase().equals("E")) {
					sendRequestToServer(printWriter, "CCP/1.0#exit#");
					System.out.println("Disconnected from server.");
					break;
				} else {
					System.out.println("Invalid input.");
				}
			}
				
			// 5. Close connection
			keyboardScanner.close();
			scanner.close();
			connection.close();
			
		} catch (IOException e) {
			System.out.println("Connect to server error: " + e.getMessage());
		}	
	}
	
	private static void sendRequestToServer(PrintWriter writer, String request) {
		writer.write(request);
		writer.write("\r\n");
		writer.flush();
	}
	
	private static void readResponseFromServer(Scanner scanner) {
		String response = scanner.nextLine();
		String body = getResponseBody(response);
		String[] parts = body.split("><");
		System.out.println("Result:");
		for(String part : parts) {
			String data = part.replace("<", "").replace(">", "");
			System.out.println(" - " + data);
		}
	}
	
	private static String getResponseBody(String response) {
		String[] parts = response.split("#");
		if(parts.length != 3) {
			return null;
		} else {
			return parts[2];
		}
	}
	
}















