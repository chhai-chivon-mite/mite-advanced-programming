import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientHandler extends Thread {
	
	private Socket connection;
	private PrintWriter writer;
	private Scanner scanner;
	private int key;

	public ClientHandler(Socket connection) {
		this.connection = connection;
	}
	
	@Override
	public void run() {

		try {
			writer = new PrintWriter(connection.getOutputStream());
			scanner = new Scanner(connection.getInputStream());
			while(true) {
				String request = readRequest();
				if(request.equals("key")) {
					Random random = new Random();
					key = random.nextInt(8) + 1;
					writer.write(key + "\n");
					writer.flush();
					System.out.println("[Key] " + key);
				} else {
					if(!processRequest(request)) {
						connection.close();
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private String readRequest() {
		return scanner.nextLine();
	}
	
	private void sendResponse(String rawResponse) {
		System.out.println("[Raw Response] " + rawResponse);
		String response = encrypt(rawResponse);
		System.out.println("[Encrypt Response] " + response);
		writer.write(response + "\n");
		writer.flush();
	}

	private boolean processRequest(String rawRequest) throws IOException {
		System.out.println("[Raw Request] " + rawRequest);
		String request = decrypt(rawRequest);
		System.out.println("[Decrypt Request] " + request);
		if(!request.startsWith("STFMP/1.0##")) {
			sendResponse("SFTMP/1.0##invalid##Invalid request.");
			return true;
		}
		if(request.startsWith("STFMP/1.0##write##")) {
			String body = request.replace("STFMP/1.0##write##", "");
			String[] parts = body.split("#");
			String fileName = parts[0];
			String content = parts[1];
			writeFile(fileName, content);
			sendResponse("SFTMP/1.0##ok##The file has been written.");
		} else if(request.equals("STFMP/1.0##list##")) {
			File folder = new File("./files/");
			String[] fileNames = folder.list();
			String data = String.join("#", fileNames);
			sendResponse("SFTMP/1.0##ok##" + data);
		} else if(request.startsWith("STFMP/1.0##view##")) {
			String fileName = request.replace("STFMP/1.0##view##", "");
			File file = new File("./files/" + fileName);
			if(!file.exists()) {
				sendResponse("SFTMP/1.0##not_found##File not found.");
				return true;
			}
			BufferedReader bufferReader = new BufferedReader(new FileReader(file));
			String content = bufferReader.readLine();
			sendResponse("SFTMP/1.0##ok##" + content);
			bufferReader.close();
		} else if(request.equals("STFMP/1.0##close##")) {
			return false;
		} else {
			sendResponse("SFTMP/1.0##invalid##Invalid request.");
		}
		
		return true;
	}
	
	private String encrypt(String message) {
		String cipherText = "";
		for(char chr : message.toCharArray()) {
			cipherText += String.valueOf((char) ((int) chr + key));
		}
		return cipherText;
	}
	
	private String decrypt(String cipherText) {
		String message = "";
		for(char chr : cipherText.toCharArray()) {
			message += String.valueOf((char)((int)chr - key));
		}
		return message;
	}
	
	private void writeFile(String fileName, String content) throws IOException {
		OutputStream outputStream = new FileOutputStream("./files/" + fileName);
		PrintWriter fileWriter = new PrintWriter(outputStream);
		fileWriter.write(content);
		fileWriter.flush();
		fileWriter.close();
	}
	
	
}












