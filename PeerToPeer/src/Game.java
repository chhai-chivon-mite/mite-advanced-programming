import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Game {

	private JFrame frmMiteGame;
	private JTextField txtAddress;
	private JLabel lblStatus;
	private JComboBox cbxVehicle;
	private JLabel lblPartnerVehicle;
	private JLabel lblVehicle;
	
	private Socket connection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game window = new Game();
					window.frmMiteGame.setVisible(true);
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
		frmMiteGame = new JFrame();
		frmMiteGame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				moveVehicle(e.getX(), e.getY());
			}
		});
		frmMiteGame.setTitle("MITE Game");
		frmMiteGame.setBounds(100, 100, 584, 411);
		frmMiteGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMiteGame.getContentPane().setLayout(null);
		
		txtAddress = new JTextField();
		txtAddress.setText("127.0.0.1");
		txtAddress.setBounds(16, 17, 130, 26);
		frmMiteGame.getContentPane().add(txtAddress);
		txtAddress.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectToHost();
			}
		});
		btnConnect.setBounds(158, 17, 117, 29);
		frmMiteGame.getContentPane().add(btnConnect);
		
		JButton btnHostGame = new JButton("Host Game");
		btnHostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hostGame();
			}
		});
		btnHostGame.setBounds(430, 17, 117, 29);
		frmMiteGame.getContentPane().add(btnHostGame);
		
		JLabel lblOr = new JLabel("Or");
		lblOr.setBounds(338, 22, 61, 16);
		frmMiteGame.getContentPane().add(lblOr);
		
		JLabel sdfsdf = new JLabel("Status:");
		sdfsdf.setBounds(16, 73, 61, 16);
		frmMiteGame.getContentPane().add(sdfsdf);
		
		lblStatus = new JLabel("disconnected");
		lblStatus.setBounds(85, 73, 154, 16);
		frmMiteGame.getContentPane().add(lblStatus);
		
		JLabel sdfsdfsdfs = new JLabel("Vehicle:");
		sdfsdfsdfs.setBounds(16, 112, 61, 16);
		frmMiteGame.getContentPane().add(sdfsdfsdfs);
		
		cbxVehicle = new JComboBox();
		cbxVehicle.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				changeVehicle();
			}
		});
		
		cbxVehicle.setModel(new DefaultComboBoxModel(new String[] {"Car", "Motorbike"}));
		cbxVehicle.setBounds(74, 108, 136, 27);
		frmMiteGame.getContentPane().add(cbxVehicle);
		
		lblVehicle = new JLabel("");
		lblVehicle.setIcon(new ImageIcon("./asset/car.png"));
		lblVehicle.setBounds(260, 221, 71, 32);
		frmMiteGame.getContentPane().add(lblVehicle);
		
		lblPartnerVehicle = new JLabel("");
		lblPartnerVehicle.setBounds(16, 160, 71, 32);
		frmMiteGame.getContentPane().add(lblPartnerVehicle);
	}
	
	private void updateStatus(String status) {
		lblStatus.setText(status);
	}
	
	private void hostGame() {
		Thread thread = new HostThread();
		thread.start();
	}
	
	private void connectToHost() {
		Thread thread = new ConnectThread();
		thread.start();
	}
	
	private void moveVehicle(int x, int y) {
		lblVehicle.setBounds(x, y, lblVehicle.getWidth(), lblVehicle.getHeight());
		sendVehicleTypeAndPositionToPartner();
	}
	
	private void changeVehicle() {
		String vehicleType = cbxVehicle.getSelectedItem().toString();
		lblVehicle.setIcon(new ImageIcon("./asset/" + vehicleType + ".png"));
		sendVehicleTypeAndPositionToPartner();
	}
	
	private void sendVehicleTypeAndPositionToPartner() {
		if(connection == null) {
			return;
		}
		Thread thread = new SenderThread();
		thread.start();
	}
	
	private void startReceiveData() {
		Thread thread = new ReceiverThread();
		thread.start();
	}
	
	private class HostThread extends Thread {
		
		@Override
		public void run() {
			
			try {
				updateStatus("waiting...");
				ServerSocket serverSocket = new ServerSocket(Constants.PORT);
				connection = serverSocket.accept();
				updateStatus("connected");
				startReceiveData();
				sendVehicleTypeAndPositionToPartner();
			} catch (IOException e) {
				updateStatus("failed");
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class ConnectThread extends Thread {
		
		@Override
		public void run() {
			
			String address = txtAddress.getText().toString();
			try {
				updateStatus("connecting...");
				connection = new Socket(address, Constants.PORT);
				updateStatus("connected");
				startReceiveData();
				sendVehicleTypeAndPositionToPartner();
			} catch (IOException e) {
				updateStatus("failed");
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class SenderThread extends Thread {
		
		@Override
		public void run() {
			
			try {
				String vehicleType = cbxVehicle.getSelectedItem().toString();
				int x = lblVehicle.getX();
				int y = lblVehicle.getY();
				String data = vehicleType + "#" + x + "#" + y;	// car#100#200
				
				PrintWriter writer = new PrintWriter(connection.getOutputStream());
				writer.write(data + "\n");
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
			// car#100#200
			String[] parts = data.split("#");
			String vehicleType = parts[0];
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);
			lblPartnerVehicle.setIcon(new ImageIcon("./asset/" + vehicleType + ".png"));
			lblPartnerVehicle.setBounds(x, y, lblPartnerVehicle.getWidth(), lblPartnerVehicle.getHeight());
		}
		
	}
	
}
























