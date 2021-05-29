import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) {
		System.out.println("Server started!");
		try {
			ServerSocket serverSocket = new ServerSocket(9999);
			while(true) {
				System.out.println("Waiting for client...");
				Socket connection = serverSocket.accept();
				Thread thread = new ClientHandler(connection);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
}
