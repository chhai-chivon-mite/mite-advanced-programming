import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class ClientGui {

	private JFrame frmCcpClient;
	private JTextField textField;
	private JLabel lblStatus;
	private JButton btnConnect;

	private Socket connection;
	private PrintWriter writer;
	private Scanner reader;
	private boolean isConnected;
	private JList<String> lstCurrencies;
	private JComboBox cbxDestinationCurrency;
	private JComboBox cbxSourceCurrency;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGui window = new ClientGui();
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
	public ClientGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCcpClient = new JFrame();
		frmCcpClient.setTitle("CCP Client 1.0");
		frmCcpClient.setBounds(100, 100, 656, 420);
		frmCcpClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCcpClient.getContentPane().setLayout(null);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				connectToServer();

			}
		});
		btnConnect.setBounds(332, 37, 117, 29);
		frmCcpClient.getContentPane().add(btnConnect);

		lstCurrencies = new JList();
		lstCurrencies.setBounds(39, 181, 133, 158);
		frmCcpClient.getContentPane().add(lstCurrencies);

		JLabel lblSupportedCurrencies = new JLabel("Supported Currencies");
		lblSupportedCurrencies.setHorizontalAlignment(SwingConstants.CENTER);
		lblSupportedCurrencies.setBounds(26, 155, 165, 16);
		frmCcpClient.getContentPane().add(lblSupportedCurrencies);

		cbxSourceCurrency = new JComboBox();
		cbxSourceCurrency.setBounds(332, 177, 83, 27);
		frmCcpClient.getContentPane().add(cbxSourceCurrency);

		JLabel lblConvertFrom = new JLabel("Convert From:");
		lblConvertFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConvertFrom.setBounds(220, 181, 100, 16);
		frmCcpClient.getContentPane().add(lblConvertFrom);

		JLabel lblTo = new JLabel("To:");
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTo.setBounds(220, 265, 100, 16);
		frmCcpClient.getContentPane().add(lblTo);

		cbxDestinationCurrency = new JComboBox();
		cbxDestinationCurrency.setBounds(332, 261, 83, 27);
		frmCcpClient.getContentPane().add(cbxDestinationCurrency);

		JLabel lblAmount = new JLabel("Amount:");
		lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmount.setBounds(220, 223, 100, 16);
		frmCcpClient.getContentPane().add(lblAmount);

		textField = new JTextField();
		textField.setBounds(332, 216, 76, 26);
		frmCcpClient.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnConvert = new JButton("Convert");
		btnConvert.setBounds(265, 310, 117, 29);
		frmCcpClient.getContentPane().add(btnConvert);

		JLabel lblResult = new JLabel("Result");
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setBounds(484, 181, 100, 16);
		frmCcpClient.getContentPane().add(lblResult);

		JLabel label = new JLabel("0");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(484, 216, 100, 16);
		frmCcpClient.getContentPane().add(label);

		JLabel lblConnectionStatus = new JLabel("Connection Status:");
		lblConnectionStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblConnectionStatus.setBounds(39, 42, 141, 16);
		frmCcpClient.getContentPane().add(lblConnectionStatus);

		lblStatus = new JLabel("(disconnected)");
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatus.setBounds(177, 42, 141, 16);
		frmCcpClient.getContentPane().add(lblStatus);
	}

	private void connectToServer() {

		Thread thread = new ConnectionHandlerTrhead();
		thread.start();

	}

	private void loadSupportedCurrency() {
		Thread thread = new ServerHandlerThread("list", null);
		thread.start();
	}

	private class ConnectionHandlerTrhead extends Thread {

		@Override
		public void run() {
			try {
				if (isConnected) {
					System.out.println("Disconnect...");
					connection.close();
					lblStatus.setText("(disconnected)");
					btnConnect.setText("Connect");
					isConnected = false;
				} else {
					System.out.println("Connect...");
					connection = new Socket("13.229.49.95", 9999);
					lblStatus.setText("(connected)");
					btnConnect.setText("Disconnect");
					isConnected = true;
					writer = new PrintWriter(connection.getOutputStream());
					reader = new Scanner(connection.getInputStream());
					loadSupportedCurrency();
				}
			} catch (IOException e) {
				lblStatus.setText("(error)");
				System.out.println("Connection error: " + e.getMessage());
			}
		}

	}

	private class ServerHandlerThread extends Thread {

		private String operation;
		private List<String> params;

		public ServerHandlerThread(String operation, List<String> params) {
			super();
			this.operation = operation;
			this.params = params;
		}

		@Override
		public void run() {

			String request = "CCP/1.0#" + operation + "#";
			if (params != null) {
				for (String param : params) {
					request += "<" + param + ">";
				}
			}

			// Send request to the server
			writer.write(request + "\r\n");
			writer.flush();
			System.out.println("[request] " + request);

			// Read response from the server
			String response = reader.nextLine();
			processResponse(response);

		}

		private void processResponse(String response) {
			System.out.println("[processResponse] " + response);
			String status = getResponseStatus(response);
			String body = getResponseBody(response);
			System.out.println("[status] " + status);

			if (status == null) {
				System.out.println("Invalid response");
			} else if (operation.equals("list")) {
				List<String> supportedCurrencies = getSupportedCurrencies(body);
				showSupportedCurrencies(supportedCurrencies);
				showCurrenciesInCombobox(supportedCurrencies);
			}
		}

		private String getResponseStatus(String response) {
			String[] parts = response.split("#");
			if (parts.length != 3) {
				return null;
			} else {
				return parts[1];
			}
		}

		private String getResponseBody(String response) {
			String[] parts = response.split("#");
			if (parts.length != 3) {
				return null;
			} else {
				return parts[2];
			}
		}

		private List<String> getSupportedCurrencies(String body) {
			String[] parts = body.split("><");
			List<String> result = new ArrayList<>();
			for (String part : parts) {
				String data = part.replace("<", "").replace(">", "");
				result.add(data);
			}
			return result;
		}

		private void showSupportedCurrencies(List<String> currencies) {
			System.out.println("[showSupportedCurrencies] " + currencies.toString());
			DefaultListModel<String> listModel = new DefaultListModel<>();
			for (String currency : currencies) {
				listModel.addElement(currency);
			}
			lstCurrencies.setModel(listModel);
		}

		private void showCurrenciesInCombobox(List<String> currencies) {
			for (String currency : currencies) {
				cbxSourceCurrency.addItem(currency);
				cbxDestinationCurrency.addItem(currency);
			}
		}

	}

}
