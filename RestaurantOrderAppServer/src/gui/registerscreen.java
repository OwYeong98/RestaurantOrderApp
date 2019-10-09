package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class registerscreen extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtName;
	private JTextField txtAge;
	private JTextField txtemailaddress;
	private JComboBox genderdropdown;
	private JComboBox privillagedropdown;
	
	JLabel lblUsernameerror;
	JLabel lblConfirmPassword;
	JLabel lblPasswordError;
	JLabel lblconfirmpasswordError;
	JLabel lblnameError;
	JLabel lblageError;
	JLabel lblemailError;
	
	private boolean usernamevalid;
	private boolean namevalid;
	private boolean agevalid;
	private boolean emailaddressvalid;
	private boolean passwordvalid;
	private boolean confirmpasswordvalid;
	private JPasswordField txtconfirmpassword;
	
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					registerscreen frame = new registerscreen();
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
	public registerscreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 561, 568);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblRegisterform = new JLabel("Register Privillage account");
		lblRegisterform.setFont(new Font("Sitka Heading", Font.BOLD, 20));
		lblRegisterform.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegisterform.setBounds(124, 11, 274, 62);
		contentPane.add(lblRegisterform);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(82, 111, 63, 26);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(82, 148, 63, 26);
		contentPane.add(lblPassword);
		
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(79, 215, 66, 26);
		contentPane.add(lblName);
		
		JLabel lblAge = new JLabel("Age:");
		lblAge.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAge.setBounds(82, 252, 63, 26);
		contentPane.add(lblAge);
		
		JLabel lblEmailAddress = new JLabel("Email Address:");
		lblEmailAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmailAddress.setBounds(33, 289, 112, 26);
		contentPane.add(lblEmailAddress);
		
		JLabel lblGender = new JLabel("Gender:");
		lblGender.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGender.setBounds(82, 326, 63, 26);
		contentPane.add(lblGender);
		
		JLabel lblPrivillage = new JLabel("Privillage:");
		lblPrivillage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrivillage.setBounds(79, 363, 66, 26);
		contentPane.add(lblPrivillage);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(190, 114, 206, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtUsername.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {

		    	
		    	
		    }
		    public void removeUpdate(DocumentEvent e) {
		       //whatever you want
		    	checkusername();
		    }
		    public void insertUpdate(DocumentEvent e) {
		    	checkusername();
		    }
		    
		    public void checkusername()
		    {
		    	//whatever you want
		    	String input=txtUsername.getText().toString();
		        
		    	if(input!=null)
                {
                    if(input.length()<5||input.length()>15)
                    {
                        usernamevalid=false;
                        lblUsernameerror.setText("The username must be atleast 5 character and at most 15 character");
                        lblUsernameerror.setVisible(true);
                    }else
                    if(input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                            ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                            ||input.contains("#")||input.contains("@")||input.contains("!")||input.contains("`")||input.contains("~")
                            ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains(".")||input.contains("?")
                            ||input.contains("{")||input.contains("}")||input.contains("[")||input.contains("]")||input.contains("|"))
                    {
                        usernamevalid=false;
                        lblUsernameerror.setText("username should not contain symbols");
                        lblUsernameerror.setVisible(true);
                    }else
                    {
                    	boolean usernameexist=false;
                    	try {
                    		Class.forName("com.mysql.jdbc.Driver");
                			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
                			//here restaurantorderapp is database name, root is username and password 
                			
                			Statement stmt=con.createStatement();  
                			ResultSet rs=stmt.executeQuery("select username from userid");
                			
                			while(rs.next())
                			{
                    			if(rs.getString(1).toLowerCase().equals(input.toLowerCase()))
                    			{
                    				usernameexist=true;
                    			}
                			}
                			
                			if(usernameexist==true)
                			{
                				usernamevalid=false;
                				lblUsernameerror.setText("username already exist!!");
                                lblUsernameerror.setVisible(true);
                			}else
                			{
                				usernamevalid=true;
                				lblUsernameerror.setVisible(false);
                			}
                			
                    	}catch(Exception ex)
                    	{
                    		
                    	}
                    	
                    }//end else
                }else//else for input null
                {
                    usernamevalid=false;
                    lblUsernameerror.setText("You have not entered anything!");
                    lblUsernameerror.setVisible(true);
                }
		    }
		  }
		);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(190, 151, 206, 20);
		contentPane.add(txtPassword);
		
		txtPassword.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	
		    	
		    }
		    public void removeUpdate(DocumentEvent e) {
		       //whatever you want
		    	checkpassword();
		    }
		    public void insertUpdate(DocumentEvent e) {
		      //whatever you want
		    	checkpassword();
		    }
		    
		    public void checkpassword()
		    {
		    	String input=txtPassword.getText().toString();
		        
                boolean gotuppercase=!input.equals(input.toLowerCase());
                boolean gotnumber=input.contains("1")||input.contains("2")||input.contains("3")||input.contains("4")||input.contains("5")
                                  ||input.contains("6")||input.contains("7")||input.contains("8")||input.contains("9")||input.contains("0");


                if(input.length()<8||input.length()>16)
                {
                	passwordvalid=false;
                	lblPasswordError.setText("password length must be at least 8 character and at most 16 character!!");
                    lblPasswordError.setVisible(true);
                }else if(gotuppercase==true && input.contains("@")&&gotnumber==true)
                {
                    passwordvalid=true;
                    lblPasswordError.setVisible(false);
                }else
                {
                	passwordvalid=false;
                	lblPasswordError.setText("Please set a harder password! Password must contains Uppercase,number, and a @ symbol");
                    lblPasswordError.setVisible(true);
                }
		    }
		  }
		);
		
		
		txtName = new JTextField();
		txtName.setBounds(190, 216, 206, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtName.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	
		    	
		    }
		    public void removeUpdate(DocumentEvent e) {
		       //whatever you want
		    	checkname();
		    }
		    public void insertUpdate(DocumentEvent e) {
		      //whatever you want
		    	checkname();
		    }
		    
		    public void checkname()
		    {
		    	String input=txtName.getText().toString();
		        
		    	boolean gotsymbol=input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                        ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                        ||input.contains("#")||input.contains("@")||input.contains("!")||input.contains("`")||input.contains("~")
                        ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains(".")||input.contains("?")
                        ||input.contains("{")||input.contains("}")||input.contains("[")||input.contains("]")||input.contains("|");

                boolean gotnumber=input.contains("1")||input.contains("2")||input.contains("3")||input.contains("4")||input.contains("5")
                        ||input.contains("6")||input.contains("7")||input.contains("8")||input.contains("9")||input.contains("0");

                if(input.length()<6||input.length() >15)
                {
                	namevalid=false;
                	lblnameError.setText("The name must have at least 6 character and at most 15 character");
                    lblnameError.setVisible(true);
         
                }else if(gotnumber==true)
                {
                	namevalid=false;
                	lblnameError.setText("Name should not contain number");
                    lblnameError.setVisible(true);
       
                }else if (gotsymbol==true)
                {
                	namevalid=false;
                	lblnameError.setText("Name should not contain symbol");
                    lblnameError.setVisible(true);

                }else
                {
                	namevalid=true;
                	lblnameError.setVisible(false);
                    
                }
		    }
		  }
		);
		
		
		txtAge = new JTextField();
		txtAge.setBounds(190, 252, 206, 20);
		contentPane.add(txtAge);
		txtAge.setColumns(10);
		
		txtAge.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	
		    }
		    public void removeUpdate(DocumentEvent e) {
		       //whatever you want
		    	checkage();
		    }
		    public void insertUpdate(DocumentEvent e) {
		      //whatever you want
		    	checkage();
		    }
		    
		    public void checkage()
		    {
		    	String input=txtAge.getText().toString();
		    	int inputnum=0;
		    	try {
		    		inputnum=Integer.parseInt(input);
		    	}catch(Exception ex)
		    	{
		    		agevalid=false;
		    		lblageError.setText("Please put number only!!");
                    lblageError.setVisible(true);
		    	}
		        
		    	

                if(inputnum<1 || inputnum>100)
                {
                	agevalid=false;
		    		lblageError.setText("Please enter valid age!!");
                    lblageError.setVisible(true);
                }else
                {
                	agevalid=true;
                    lblageError.setVisible(false);
                }
		    }
		  }
		);
		
		txtemailaddress = new JTextField();
		txtemailaddress.setText("");
		txtemailaddress.setBounds(190, 289, 206, 20);
		contentPane.add(txtemailaddress);
		txtemailaddress.setColumns(10);
		
		txtemailaddress.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	
		    }
		    public void removeUpdate(DocumentEvent e) {
		       //whatever you want
		    	checkemail();
		    }
		    public void insertUpdate(DocumentEvent e) {
		      //whatever you want
		    	checkemail();
		    }
		    
		    public void checkemail()
		    {
		    	String input=txtemailaddress.getText().toString();
		    	
		    	boolean gotsymbol=input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                        ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                        ||input.contains("#")||input.contains("|")||input.contains("!")||input.contains("`")||input.contains("~")
                        ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains("]")||input.contains("?")
                        ||input.contains("{")||input.contains("}")||input.contains("[");

                if(input.length()<10||input.length()>50)
                {
                	emailaddressvalid=false;
		    		lblemailError.setText("Email should not be shorter than 10 character or longer than 50 character");
                    lblemailError.setVisible(true);
                   
                }else if(gotsymbol==true)
                {
                	emailaddressvalid=false;
		    		lblemailError.setText("Email should not contain symbol");
                    lblemailError.setVisible(true);
      
                }else if(input.contains(".com")&&input.contains("@"))
                {
                	try {
                		//email format all is correct here
                        //but we need to validate with server to see whether it is already used
                    	Class.forName("com.mysql.jdbc.Driver");
            			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
            			//here restaurantorderapp is database name, root is username and password 
            			
            			Statement stmt=con.createStatement();  
            			ResultSet rs=stmt.executeQuery("select emailaddr from userid");
            			
            			boolean emailexist=false;
            			
            			while(rs.next())
            			{
                			if(rs.getString(1).toLowerCase().equals(input.toLowerCase()))
                			{
                				emailexist=true;
                			}
            			}
            			
            			if(emailexist==true)
            			{
            				emailaddressvalid=false;
        		    		lblemailError.setText("username already exist!!");
                            lblemailError.setVisible(true);
            				
            			}else
            			{
            				emailaddressvalid=true;
                            lblemailError.setVisible(false);
            				
            			}
                	}catch(Exception ex)
                	{
                		
                	}
                    

                }else
                {
                	emailaddressvalid=false;
		    		lblemailError.setText("Please use valid email format!");
                    lblemailError.setVisible(true);
                }
		    }
		  }
		);
		
		txtconfirmpassword = new JPasswordField();
		txtconfirmpassword.setBounds(190, 185, 206, 20);
		contentPane.add(txtconfirmpassword);
		
		txtconfirmpassword.getDocument().addDocumentListener(new DocumentListener() {
		    public void changedUpdate(DocumentEvent e) {
		    	
		    }
		    public void removeUpdate(DocumentEvent e) {
		       //whatever you want
		    	checkconfirmpassword();
		    }
		    public void insertUpdate(DocumentEvent e) {
		      //whatever you want
		    	checkconfirmpassword();
		    }
		    
		    public void checkconfirmpassword()
		    {
		    	String input=txtconfirmpassword.getText().toString();
		    	
		    	if(input.equals(txtPassword.getText().toString())==false)
                {
		    		confirmpasswordvalid=false;
		    		lblconfirmpasswordError.setText("The Password is not same");
		    		lblconfirmpasswordError.setVisible(true);
                   
                }else
                {
                	confirmpasswordvalid=true;
                	lblconfirmpasswordError.setVisible(false);
                }
		    }
		  }
		);
		
		
		
		String[] genderlist={"male","female"};
		
		genderdropdown = new JComboBox(genderlist);
		genderdropdown.setBounds(190, 329, 83, 23);
		contentPane.add(genderdropdown);
		
		String[] privillagelist={"admin","employee","customer"};
		
		privillagedropdown = new JComboBox(privillagelist);
		privillagedropdown.setBounds(190, 366, 83, 20);
		contentPane.add(privillagedropdown);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean validinformation=usernamevalid&&namevalid&&agevalid&&emailaddressvalid&&passwordvalid&&confirmpasswordvalid;
				
				if(validinformation==true)
				{
					try
			    	{  
						String username=txtUsername.getText().toString();
						String password=txtPassword.getText().toString();
						String name=txtName.getText().toString();
						String email=txtemailaddress.getText().toString();
						int age=Integer.parseInt(txtAge.getText().toString());
						String gender=String.valueOf(genderdropdown.getSelectedItem());
						String privillage=String.valueOf(privillagedropdown.getSelectedItem());
						
			    		Class.forName("com.mysql.jdbc.Driver");
			    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			    		//here restaurantorderapp is database name, root is username and password 
			    		
			    		Statement stmt=con.createStatement();  
			    		stmt.executeUpdate("insert into userid(username,password,name,age,emailaddr,gender,privillage) values('"+username+"','"+password+"','"+name+"',"+age+",'"+email+"','"+gender+"','"+privillage+"');");

			    		
			    		con.close();
			    		
			    		JOptionPane.showMessageDialog(contentPane, "Successfully created!");
			    		
			    		mainframe m= new mainframe();
			    		m.setVisible(true);
			    		
			    		dispose();


			    	}catch(Exception ex)
					{
			    		

			    	}
					
				}else
				{
					JOptionPane.showMessageDialog(contentPane, "Please make sure all information is corrected!");
				}
						

			}
		});
		
		
		
		
		btnRegister.setBounds(184, 431, 135, 33);
		contentPane.add(btnRegister);
		
		lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConfirmPassword.setBounds(10, 184, 135, 20);
		contentPane.add(lblConfirmPassword);
		
		
		
		lblUsernameerror = new JLabel("Username already used!");
		lblUsernameerror.setForeground(Color.RED);
		lblUsernameerror.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblUsernameerror.setBounds(190, 96, 346, 14);
		contentPane.add(lblUsernameerror);
		
		lblPasswordError = new JLabel("Password must contain Uppercase,/nlowercase,number,and a @ symbol");
		lblPasswordError.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPasswordError.setForeground(Color.RED);
		lblPasswordError.setBounds(190, 133, 346, 14);
		contentPane.add(lblPasswordError);
		
		lblconfirmpasswordError = new JLabel("Password not match with password entered");
		lblconfirmpasswordError.setForeground(Color.RED);
		lblconfirmpasswordError.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblconfirmpasswordError.setBounds(190, 168, 346, 14);
		contentPane.add(lblconfirmpasswordError);
		
		
		lblnameError = new JLabel("Name should not contain symbol");
		lblnameError.setForeground(Color.RED);
		lblnameError.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblnameError.setBounds(190, 204, 346, 14);
		contentPane.add(lblnameError);
		
		lblageError = new JLabel("Age must be larger than 1");
		lblageError.setForeground(Color.RED);
		lblageError.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblageError.setBounds(190, 239, 346, 14);
		contentPane.add(lblageError);
		
		lblemailError = new JLabel("Email must contain @ and .com");
		lblemailError.setForeground(Color.RED);
		lblemailError.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblemailError.setBounds(190, 277, 346, 14);
		contentPane.add(lblemailError);
		
		
		
		lblUsernameerror.setVisible(false);
		lblPasswordError.setVisible(false);
		lblconfirmpasswordError.setVisible(false);
		lblnameError.setVisible(false);
		lblageError.setVisible(false);
		lblemailError.setVisible(false);
	}
}
