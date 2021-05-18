import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientHandlerThread extends Thread {
	
	private Socket connection;
	
	
	public ClientHandlerThread(Socket connection) {
		this.connection = connection;
	}


	@Override
	public void run() {
		
		try {
		InputStream inputStream = connection.getInputStream();
		Scanner scanner = new Scanner(inputStream);
		while(true){
			String request = scanner.nextLine();
			
			// 4. Process data
			sleep(3000);
			if(!processRequest(connection, request)) {
				break;
			}
		}
		scanner.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean processRequest(Socket connection, String request) throws IOException {
		String[] parts = request.split("#");
		if(parts.length < 2 || parts.length > 3 ) {
			sendResponse(connection, "CCP/1.0#invalid#<Invalid request.>");
			return true;
		}
		
		String operation = parts[1];
		if(operation.equals("list")) {
			sendResponse(connection, "CCP/1.0#ok#<USD><KHR><JPY><CNY><THB><VND>");
		} else if(operation.contains("convert")) {
			String parameters = parts[2];
			String[] parameterParts = parameters.split("><");
			if(parameterParts.length != 3) {
				sendResponse(connection, "CCP/1.0#invalid#<Invalid request.>");
				return true;
			}
			String sourceCurrency = parameterParts[0].replace("<", "");
			double amount = Double.parseDouble(parameterParts[1]);
			String destinationCurrency = parameterParts[2].replace(">", "");
			double result = convertCurrency(sourceCurrency, amount, destinationCurrency);
			sendResponse(connection, "CCP/1.0#ok#<" + result + ">");
		} else if(operation.equals("exit")) {
			connection.close();
			return false;
		}
		
		
		return true;
	}
	
	private void sendResponse(Socket connection, String response) throws IOException {
		PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
		printWriter.write(response + "\r\n");
		printWriter.flush();
	}
	
	private double convertCurrency(String sourceCurrency, double amount, String destinationCurrency) {
		Map<String, Double> rates = new HashMap<String, Double>();
		rates.put("USD", 1.0);
		rates.put("KHR", 4100.0);
		rates.put("JPY", 107.88);
		rates.put("CNY", 6.5);
		rates.put("VND", 23071.0);
		rates.put("THB", 31.43);
		
		double sourceRate = rates.get(sourceCurrency);
		double destinationRate = rates.get(destinationCurrency);
		double result =  destinationRate * amount / sourceRate;
				
		return result;
		
	}

}
