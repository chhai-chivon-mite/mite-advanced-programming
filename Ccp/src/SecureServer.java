import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SecureServer {

	public static void main(String[] args) {

		try {
			// 1. Create a secure server socket and bind to port 9999
			// Create SSL Context
			SSLContext context = SSLContext.getInstance("SSL");

			// Create Key Manager Factory
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

			// Create KeyStore
			KeyStore keyStore = KeyStore.getInstance("JKS");

			// Load KeyStore
			FileInputStream fileInputStream = new FileInputStream("../lib/mite.key");
			char[] password = "111111".toCharArray();
			keyStore.load(fileInputStream, password);

			// Initialize Key Manager Factory
			keyManagerFactory.init(keyStore, password);

			// Initialize SSL Context
			context.init(keyManagerFactory.getKeyManagers(), null, null);

			// Create SSL Server Socket
			SSLServerSocketFactory factory = context.getServerSocketFactory();
			SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(9999);

			while (true) {
				// 2. Wait for connection from clients
				System.out.println("Waiting for client...");
				SSLSocket connection = (SSLSocket) serverSocket.accept();

				// 3. Create another thread to handle the connection
				System.out.println("Create another thread to handle the connection.");
				Thread thread = new ClientHandlerThread(connection);
				thread.start();
			}

		} catch (Exception e) {
			System.out.println("Cannot bind to port 9999: " + e.getMessage());
		}

	}

}
