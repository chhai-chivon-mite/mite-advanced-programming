import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// Show options to user
		System.out.println("Welcome to MITE App:");
		System.out.println("Enter 'w' to write a file, and 'r' to read a file.");
		System.out.print("Enter here: ");
		
		// Read data from keyboard
		InputStream keybordInputStream = System.in;
		Scanner keyboardScanner = new Scanner(keybordInputStream);
		String option = keyboardScanner.nextLine();
		
		// Process
		if(option.equals("w")) {
			System.out.print("Enter file content: ");
			String content = keyboardScanner.nextLine();
			// Write content to the file
			writeToFile(content);
			System.out.println("Content has been written!");
		} else if(option.equals("r")) {
			String content = readFromFile();
			System.out.println("Content: " + content);
		} else {
			System.out.println("Invalid option.");
		}
		
		keyboardScanner.close();
		
	}
	
	private static void writeToFile(String content) {
		try {
			OutputStream outputStream = new FileOutputStream("./asset/myfile.txt");
			PrintWriter writer = new PrintWriter(outputStream);
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static String readFromFile() {
		try {
			InputStream inputStream = new FileInputStream("./asset/myfile.txt");
			Scanner scanner = new Scanner(inputStream);
			String content = scanner.nextLine();
			scanner.close();
			return content;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Error";
		}
		
	}
	
}
























