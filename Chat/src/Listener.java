import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Listener {

	public static void main(String[] args) {

		try {

			// Listen incomming connection from connector
			ServerSocket serverSocket = new ServerSocket(9999);
			System.out.println("Wait for connector...");
			Socket connection = serverSocket.accept();

			// Start ReceiverThread to listen incomming data
			System.out.println("Start ReceiverThread");
			Thread receiverThread = new ReceiverThread(connection.getInputStream());
			receiverThread.start();

			// Read user input
			Scanner keyboardScanner = new Scanner(System.in);
			while (true) {
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
