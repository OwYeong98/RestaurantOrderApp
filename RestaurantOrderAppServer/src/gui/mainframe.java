package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import servercomponent.Server;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;



public class mainframe extends JFrame {

	private JPanel contentPane;
	private JTextArea Serverlog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainframe frame = new mainframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainframe() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 601, 461);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		redirectSystemStreams();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 565, 340);
		contentPane.add(scrollPane);
		
		Serverlog = new JTextArea();
		scrollPane.setViewportView(Serverlog);
		Serverlog.setEditable(false);
		Serverlog.setBackground(Color.WHITE);
		
		JLabel lblServerLog = new JLabel("Server Log");
		lblServerLog.setBounds(24, 11, 86, 14);
		contentPane.add(lblServerLog);
		
		
		JButton btnStartServer = new JButton("Start server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean sqlopened=false;
				try {
            		Class.forName("com.mysql.jdbc.Driver");
        			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
        			//here restaurantorderapp is database name, root is username and password 
        			
        			Statement stmt=con.createStatement();  
        			ResultSet rs=stmt.executeQuery("select * from userid");
        			
        			sqlopened=true;

            	}catch(Exception ex)
            	{
            		sqlopened=false;
            	}
				
				if(sqlopened==false)
				{
					JOptionPane.showMessageDialog(contentPane, "SQL did not found! please open mySQL");
				}else
				{
					if(Server.serverstarted==false)
					{
						Thread thread = new Thread() {
						    public void run() {
						    	Server.startserver();
						    }
						};
						thread.start();
						
					}else
					{
						JOptionPane.showMessageDialog(contentPane, "Server already started!");
					}
				}
				
				
			}
		});
		btnStartServer.setBounds(10, 382, 123, 34);
		contentPane.add(btnStartServer);
		
		JButton btnstopserver = new JButton("Stop server");
		btnstopserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.stopserver();
			}
		});
		btnstopserver.setBounds(143, 382, 135, 34);
		contentPane.add(btnstopserver);
		
		JButton btnRegister = new JButton("Register privillage account");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean sqlopened=false;
				try {
            		Class.forName("com.mysql.jdbc.Driver");
        			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
        			//here restaurantorderapp is database name, root is username and password 
        			
        			Statement stmt=con.createStatement();  
        			ResultSet rs=stmt.executeQuery("select * from userid");
        			
        			sqlopened=true;

            	}catch(Exception ex)
            	{
            		sqlopened=false;
            	}
				
				if(sqlopened==false)
				{
					JOptionPane.showMessageDialog(contentPane, "SQL did not found! please open mySQL");
				}else
				{
					registerscreen r= new registerscreen();
					r.setVisible(true);
					dispose();
				}
				
				
			}
		});
		btnRegister.setBounds(372, 382, 203, 34);
		contentPane.add(btnRegister);
		
		
	}
	
	private void updateserverlog(final String text) {
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	        Serverlog.append(text);
	      }
	    });
	  }
	
	
	public void redirectSystemStreams() {
	    OutputStream out = new OutputStream() {
	      @Override
	      public void write(int b) throws IOException {
	        updateserverlog(String.valueOf((char) b));
	      }
	 
	      @Override
	      public void write(byte[] b, int off, int len) throws IOException {
	        updateserverlog(new String(b, off, len));
	      }
	 
	      @Override
	      public void write(byte[] b) throws IOException {
	        write(b, 0, b.length);
	      }
	    };
	 
	    System.setOut(new PrintStream(out, true));
	    System.setErr(new PrintStream(out, true));
	  }

}
