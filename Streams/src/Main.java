import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		// Read data from keyboard
		InputStream inputStream = System.in;
		Scanner keyboardScanner = new Scanner(inputStream);
		System.out.println("Welcome to MITE.");
		System.out.println("Enter 'w' to write data.");
		System.out.println("Enter 'r' to read data.");
		System.out.print("Your option: ");
		String option = keyboardScanner.nextLine();
		
		// Process option
		if(option.equals("w")) {
			System.out.println("Enter data:");
			String data = keyboardScanner.nextLine();
			writeDataToFile(data);
			System.out.println("Data has been writen successfully.");
		} else if(option.equals("r")) {
			String data = readDataFromFile();
			System.out.println("Data: " + data);
		} else {
			System.out.println("Invalid option.");
		}
		keyboardScanner.close();
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String readDataFromFile() {
		String fileName = "MyFile65654.txt";
		try {
			InputStream inputStream = new FileInputStream(fileName);
			Scanner scanner = new Scanner(inputStream);
			String data = scanner.nextLine();
			scanner.close();
			inputStream.close();
			return data;
		} catch (Exception e) {
			//e.printStackTrace();
			 System.out.println("File not found.");
			 return null;
		}
	}
	
	static void writeDataToFile(String data) {
		String fileName = "MyFile.txt";

		try {
			OutputStream outputStream = new FileOutputStream(fileName);
			
			PrintWriter printWriter = new PrintWriter(outputStream);
			printWriter.write(data);
			printWriter.flush();
			printWriter.close();			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something wroing with writing file.");
		}
	}

}
