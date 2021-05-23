import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Game {

	private JFrame frmVehicleGame;
	private JTextField txtAddress;
	private JComboBox cbxVehicle;
	private JLabel lblVehicle;
	private JLabel lblParterVehicle;
	
	private Socket connection;
	private JLabel ssdsf;
	private JLabel lblStatus;
	private JLabel lblVehicle_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game window = new Game();
					window.frmVehicleGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Game() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVehicleGame = new JFrame();
		frmVehicleGame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				moveVehicle(e.getX(), e.getY());
			}
		});
		frmVehicleGame.setTitle("Vehicle Game");
		frmVehicleGame.setBounds(100, 100, 544, 414);
		frmVehicleGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVehicleGame.getContentPane().setLayout(null);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		btnConnect.setBounds(237, 20, 117, 29);
		frmVehicleGame.getContentPane().add(btnConnect);
		
		txtAddress = new JTextField();
		txtAddress.setText("127.0.0.1");
		txtAddress.setBounds(95, 20, 130, 26);
		frmVehicleGame.getContentPane().add(txtAddress);
		txtAddress.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBounds(22, 25, 78, 16);
		frmVehicleGame.getContentPane().add(lblIpAddress);
		
		JLabel lblOr = new JLabel("OR");
		lblOr.setBounds(371, 25, 31, 16);
		frmVehicleGame.getContentPane().add(lblOr);
		
		JButton btnHost = new JButton("Host");
		btnHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostGame();
			}
		});
		btnHost.setBounds(402, 20, 117, 29);
		frmVehicleGame.getContentPane().add(btnHost);
		
		lblVehicle = new JLabel("");
		lblVehicle.setIcon(new ImageIcon("/Users/leapkh/Desktop/img/motorbike.png"));
		lblVehicle.setBounds(214, 204, 71, 40);
		frmVehicleGame.getContentPane().add(lblVehicle);
		
		cbxVehicle = new JComboBox();
		cbxVehicle.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeVehicle();
			}
		});
		cbxVehicle.setModel(new DefaultComboBoxModel(new String[] {"Motorbike", "Car"}));
		cbxVehicle.setBounds(375, 72, 117, 27);
		frmVehicleGame.getContentPane().add(cbxVehicle);
		
		lblParterVehicle = new JLabel("");
		lblParterVehicle.setIcon(null);
		lblParterVehicle.setBounds(68, 204, 71, 40);
		frmVehicleGame.getContentPane().add(lblParterVehicle);
		
		ssdsf = new JLabel("Status:");
		ssdsf.setBounds(22, 76, 49, 16);
		frmVehicleGame.getContentPane().add(ssdsf);
		
		lblStatus = new JLabel("disconnected");
		lblStatus.setBounds(95, 76, 130, 16);
		frmVehicleGame.getContentPane().add(lblStatus);
		
		lblVehicle_1 = new JLabel("Vehicle:");
		lblVehicle_1.setBounds(296, 76, 58, 16);
		frmVehicleGame.getContentPane().add(lblVehicle_1);
	}
	
	private void changeVehicle() {
		if(cbxVehicle.getSelectedIndex() == 0) {
			lblVehicle.setIcon(new ImageIcon("/Users/leapkh/Desktop/img/motorbike.png"));
		} else {
			lblVehicle.setIcon(new ImageIcon("/Users/leapkh/Desktop/img/car.png"));
		}
		sendVehicleInfoToPartner();
	}
	
	private void moveVehicle(int x, int y) {
		lblVehicle.setBounds(x, y, lblVehicle.getWidth(), lblVehicle.getHeight());
		sendVehicleInfoToPartner();
	}
	
	private void hostGame() {
		Thread thread = new HostThread();
		thread.start();
	}
	
	private void connect() {
		Thread thread = new ConnectionThread();
		thread.start();
	}
	
	private void updateStatus(String status) {
		lblStatus.setText(status);
	}
	
	private void sendVehicleInfoToPartner() {
		if(connection != null && connection.isConnected()) {
			Thread thread = new SenderThread();
			thread.start();
		}
	}
	
	private void listenForPartnerVehicleInfo() {
		Thread thread = new ReceiverThread();
		thread.start();
	}
	
	// Classes
	private class HostThread extends Thread {
		
		@Override
		public void run() {
			
			try {
				// Bind port and wait for connection
				ServerSocket serverSocket = new ServerSocket(Constants.PORT);
				updateStatus("waiting...");
				connection = serverSocket.accept();
				updateStatus("connected");
				listenForPartnerVehicleInfo();
				sendVehicleInfoToPartner();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class ConnectionThread extends Thread {
		@Override
		public void run() {
			try {
				String partnerAddress = txtAddress.getText().toString();
				connection = new Socket(partnerAddress, Constants.PORT);
				updateStatus("connected");
				listenForPartnerVehicleInfo();
				sendVehicleInfoToPartner();
			} catch (IOException e) {
				e.printStackTrace();
				updateStatus("connection failed");
			}
		}
	}
	
	private class SenderThread extends Thread {
		@Override
		public void run() {
			String vehicleType = cbxVehicle.getSelectedIndex() == 0 ? "motorbike" : "car";
			int x = lblVehicle.getX();
			int y = lblVehicle.getY();
			String data = vehicleType + "#" + x + "#" + y + "\n";  // car#100#500
			try {
				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				writer.write(data);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private class ReceiverThread extends Thread {
		@Override
		public void run() {
			try {
				Scanner scanner = new Scanner(connection.getInputStream());
				while(true) {
					String data = scanner.nextLine();
					processData(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void processData(String data) {
			// car#100#500
			String[] parts = data.split("#");
			String vehicleType = parts[0];
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);
			lblParterVehicle.setIcon(new ImageIcon("/Users/leapkh/Desktop/img/" + vehicleType + ".png"));
			lblParterVehicle.setBounds(x, y, lblParterVehicle.getWidth(), lblParterVehicle.getHeight());
		}
		
	}
	
}




















