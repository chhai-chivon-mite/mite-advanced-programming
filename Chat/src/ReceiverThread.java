import java.io.InputStream;
import java.util.Scanner;

public class ReceiverThread extends Thread {
	
	private InputStream inputStream;

	public ReceiverThread(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(inputStream);
		while(true) {
			String data = scanner.nextLine();
			System.out.println(data);
		}
	}
	
}
