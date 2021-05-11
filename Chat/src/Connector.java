import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connector {

	public static void main(String[] args) {
		
		try {
			// Connect to listener
			Socket connection = new Socket("localhost", 9999);
			
			// Start ReceiverThread to listen incomming data
			System.out.println("Start ReceiverThread");
			Thread receiverThread = new ReceiverThread(connection.getInputStream());
			receiverThread.start();
			
			// Read user input
			Scanner keyboardScanner = new Scanner(System.in);
			while(true) {
				System.out.print("Enter your text here: ");
				String data = keyboardScanner.nextLine();
				Thread senderThread = new SenderThread(connection.getOutputStream(), data);
				senderThread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
