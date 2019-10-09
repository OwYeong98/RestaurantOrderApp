package gui;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class loginscreen {

	private JFrame frame;
	private JTextField username;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginscreen window = new loginscreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public loginscreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		username = new JTextField();
		username.setBounds(189, 109, 147, 20);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		JLabel lblLoginToThe = new JLabel("Login to the system");
		lblLoginToThe.setFont(new Font("Yu Gothic Medium", Font.BOLD, 17));
		lblLoginToThe.setBounds(112, 22, 189, 48);
		frame.getContentPane().add(lblLoginToThe);
		
		password = new JPasswordField();
		password.setBounds(189, 158, 147, 20);
		frame.getContentPane().add(password);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(86, 109, 66, 20);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(86, 158, 66, 20);
		frame.getContentPane().add(lblPassword);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String usernameinput=username.getText();
				String passwordinput=password.getText();
				
				if(usernameinput.equals("admin") && passwordinput.equals("admin"))
				{
					JOptionPane.showMessageDialog(frame, "Successfully logged in!");
					
					mainframe mainframe=new mainframe();
					mainframe.setVisible(true);
					
					frame.dispose();
					
					
				}else
				{
					JOptionPane.showMessageDialog(frame, "Username or password entered is wrong!");
				}
			}
		});
		btnLogin.setBounds(175, 215, 89, 23);
		frame.getContentPane().add(btnLogin);

	}
}
