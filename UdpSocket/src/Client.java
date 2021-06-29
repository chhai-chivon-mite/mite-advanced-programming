import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

	public static void main(String[] args) {
		
		try {
			// Create socket using random port
			DatagramSocket socket = new DatagramSocket(0);
			
			// Set timeout
			socket.setSoTimeout(5000);
			
			// Send request to server
			String data = "Hello MITE.";
			byte[] buffer = data.getBytes();
			InetAddress address = InetAddress.getByName("localhost");
			DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, Constants.SERVER_PORT);
			socket.send(request);
			
			// Read response from the server
			byte[] responseBuffer = new byte[Constants.BUFFER_LENGTH];
			DatagramPacket response = new DatagramPacket(responseBuffer, Constants.BUFFER_LENGTH);
			socket.receive(response);
			
			// Process response
			String responseData = new String(response.getData(), 0, response.getLength());
			System.out.println("Response: " + responseData);
			
			// Close socket
			socket.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
