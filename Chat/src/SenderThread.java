import java.io.OutputStream;
import java.io.PrintWriter;

public class SenderThread extends Thread {
	
	private OutputStream outputStream;
	private String data;
	
	public SenderThread(OutputStream outputStream, String data) {
		this.outputStream = outputStream;
		this.data = data;
	}

	@Override
	public void run() {
		
		PrintWriter writer = new PrintWriter(outputStream);
		writer.write(data + "\n");
		writer.flush();
		
	}

}
