import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
	
	public static void main(String[] args) {
		
		
		try {
			// Bind to port 9999
			DatagramSocket socket = new DatagramSocket(Constants.SERVER_PORT);
			
			while(true) {
				// Read request from client
				byte[] buffer = new byte[Constants.BUFFER_LENGTH];
				DatagramPacket request = new DatagramPacket(buffer, Constants.BUFFER_LENGTH);
				System.out.println("Waiting request from clients...");
				socket.receive(request);
				
				// Process the request
				// - Convert from byte[] to String
				String data = new String(request.getData(), 0, request.getLength());
				System.out.println("Request: " + data);
				
				// Send response to client
				String responseData = "Welcome to MITE Server.";
				byte[] responseBuffer = responseData.getBytes();
				DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length, request.getAddress(), request.getPort());
				socket.send(response);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
