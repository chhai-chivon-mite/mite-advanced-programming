import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Color;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.DefaultComboBoxModel;

public class SecureClientGui {

	private JFrame frmCcpClient;
	private JLabel lblStatus;
	private JButton btnConnect;

	private SSLSocket connection;
	private boolean isConnected;
	private JLabel lblSourceCurrency;
	private JLabel lblAmount;
	private JLabel lblTo;
	private JTextField etxtAmount;
	private JComboBox<String> cbxSourceCurrency;
	private JComboBox<String> cbxDestinationCurrency;
	private JLabel lblResult;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SecureClientGui window = new SecureClientGui();
					window.frmCcpClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SecureClientGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCcpClient = new JFrame();
		frmCcpClient.setTitle("CCP Client 1.0");
		frmCcpClient.setBounds(100, 100, 583, 398);
		frmCcpClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCcpClient.getContentPane().setLayout(null);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread thread = new ServerHandlerThread("connect");
				thread.start();

			}
		});
		btnConnect.setBounds(393, 25, 117, 29);
		frmCcpClient.getContentPane().add(btnConnect);

		JLabel label = new JLabel("Status:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(25, 30, 62, 16);
		frmCcpClient.getContentPane().add(label);

		lblStatus = new JLabel("(disconnected)");
		lblStatus.setBounds(96, 30, 122, 16);
		frmCcpClient.getContentPane().add(lblStatus);

		lblSourceCurrency = new JLabel("Convert From");
		lblSourceCurrency.setHorizontalAlignment(SwingConstants.CENTER);
		lblSourceCurrency.setBounds(39, 126, 141, 16);
		frmCcpClient.getContentPane().add(lblSourceCurrency);

		lblAmount = new JLabel("Amount");
		lblAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblAmount.setBounds(192, 126, 141, 16);
		frmCcpClient.getContentPane().add(lblAmount);

		lblTo = new JLabel("To");
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setBounds(369, 126, 141, 16);
		frmCcpClient.getContentPane().add(lblTo);

		cbxSourceCurrency = new JComboBox();
		cbxSourceCurrency.setBounds(58, 166, 108, 27);
		frmCcpClient.getContentPane().add(cbxSourceCurrency);

		etxtAmount = new JTextField();
		etxtAmount.setHorizontalAlignment(SwingConstants.CENTER);
		etxtAmount.setBounds(202, 165, 130, 26);
		frmCcpClient.getContentPane().add(etxtAmount);
		etxtAmount.setColumns(10);

		cbxDestinationCurrency = new JComboBox();
		cbxDestinationCurrency.setBounds(383, 166, 108, 27);
		frmCcpClient.getContentPane().add(cbxDestinationCurrency);

		JButton btnConvert = new JButton("Convert");
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread thread = new ServerHandlerThread("convert");
				thread.start();

			}
		});
		btnConvert.setBounds(212, 214, 117, 29);
		frmCcpClient.getContentPane().add(btnConvert);

		JLabel sdfsdf = new JLabel("Result:");
		sdfsdf.setHorizontalAlignment(SwingConstants.RIGHT);
		sdfsdf.setBounds(96, 304, 61, 16);
		frmCcpClient.getContentPane().add(sdfsdf);

		lblResult = new JLabel("");
		lblResult.setBackground(Color.DARK_GRAY);
		lblResult.setBounds(212, 304, 108, 16);
		frmCcpClient.getContentPane().add(lblResult);
	}

	private class ServerHandlerThread extends Thread {

		private String operation;

		public ServerHandlerThread(String operation) {
			this.operation = operation;
		}

		@Override
		public void run() {

			try {
				if (operation.equals("connect")) {
					connectToServer();
				} else if (operation.equals("convert")) {
					convertCurrency();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void connectToServer() {
			try {
				if (isConnected) {
					PrintWriter writer = new PrintWriter(connection.getOutputStream());
					sendRequestToServer(writer, "CCP/1.0#exit#");
					connection.close();
					lblStatus.setText("(disconnected)");
					btnConnect.setText("Connect");
					isConnected = false;
				} else {
					// ****** Method 1: verify server certificate with Trust Store
					/*// Set trust store info
					System.setProperty("javax.net.ssl.trustStore", "./lib/mite.cacerts");
					System.setProperty("javax.net.ssl.trustStorePassword", "111111");
					
					// Connect to server
					SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
					connection = (SSLSocket) factory.createSocket("localhost", 9999);*/
					
					// ****** Method 2: verify server certificate by accepting any certificate
					X509TrustManager trustManager = new X509TrustManager() {
						
						@Override
						public X509Certificate[] getAcceptedIssuers() {
							// TODO Auto-generated method stub
							return null;
						}
						
						@Override
						public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
							// TODO Auto-generated method stub
							
						}
					};
					TrustManager[] allTrustManagers = new TrustManager[] {trustManager};
					SSLContext context = SSLContext.getInstance("SSL");
					context.init(null, allTrustManagers, null);
					SSLSocketFactory factory = context.getSocketFactory();
					connection = (SSLSocket) factory.createSocket("localhost", 9999);
					
					lblStatus.setText("(connected)");
					btnConnect.setText("Disconnect");
					isConnected = true;
					loadSupportedCurrencies();
				}
			} catch (Exception e) {
				lblStatus.setText("(error)");
				System.out.println("Connect error: " + e.getMessage());
			}
		}

		private void sendRequestToServer(PrintWriter writer, String request) {
			writer.write(request);
			writer.write("\r\n");
			writer.flush();
		}

		private void loadSupportedCurrencies() throws IOException {
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			String request = "CCP/1.0#list#";
			sendRequestToServer(writer, request);
			Scanner scanner = new Scanner(connection.getInputStream());
			String response = scanner.nextLine();
			String body = getResponseBody(response);
			List<String> currencies = getCurrencyListFromBody(body);
			showSupportedCurrencies(currencies);
		}

		private String getResponseBody(String response) {
			String[] parts = response.split("#");
			if (parts.length != 3) {
				return null;
			} else {
				return parts[2];
			}
		}

		private List<String> getCurrencyListFromBody(String body) {
			List<String> currencies = new ArrayList<String>();
			String[] parts = body.split("><");
			for (String part : parts) {
				String currency = part.replace("<", "").replace(">", "");
				currencies.add(currency);
			}
			return currencies;
		}

		private void showSupportedCurrencies(List<String> currencies) {
			for (String currency : currencies) {
				cbxSourceCurrency.addItem(currency);
				cbxDestinationCurrency.addItem(currency);
			}
		}

		private void convertCurrency() {

			lblResult.setText("Processing...");

			String sourceCurrency = cbxSourceCurrency.getSelectedItem().toString();
			String destinationCurrency = cbxDestinationCurrency.getSelectedItem().toString();
			String amount = etxtAmount.getText();
			String request = String.format("CCP/1.0#convert#<%s><%s><%s>", sourceCurrency, amount, destinationCurrency);
			// String request = "CCP/1.0#convert#<" + sourceCurrency + "><" + amount + "><"
			// + destinationCurrency + ">";
			try {
				PrintWriter writer;
				writer = new PrintWriter(connection.getOutputStream());
				sendRequestToServer(writer, request);
				Scanner scanner = new Scanner(connection.getInputStream());
				String response = scanner.nextLine();
				String body = getResponseBody(response);
				String result = body.replace("<", "").replace(">", "");
				showConvertResult(Double.parseDouble(result), destinationCurrency);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void showConvertResult(double amount, String currency) {
			NumberFormat numberFormat = NumberFormat.getInstance();
			String result = numberFormat.format(amount);
			lblResult.setText(result + " " + currency);
		}

	}

}
