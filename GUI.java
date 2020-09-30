/*
 This class handles all GUI. 
 */
package lashes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class GUI {
    
    SQLHandler sql ;
    Toolkit screen;
    Font mainFont;
    Font inputFont;
    
    int screenWidth ; 
    int screenHeight;
    
    public boolean clientStatus = true ;
    
    public GUI() throws SQLException {
        sql = new SQLHandler();
        screen = Toolkit.getDefaultToolkit();
        screenWidth = (int)screen.getScreenSize().getWidth();
        screenHeight = (int)screen.getScreenSize().getHeight();
        mainFont = new Font(Font.SERIF, Font.PLAIN, 30);
        inputFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);
        
    }
    
    //This will ask user for password.
    public void displayLogin() {
        
        JFrame frame = new JFrame();
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        
        //Labels etc.. 
        JLabel title = new JLabel("Lei e Bella");
        JLabel password = new JLabel("PASSWORD:");
        JLabel status = new JLabel(""); 
        JPasswordField input = new JPasswordField();
        JButton loginButton = new JButton("Login");
       
        Font titleFont = new Font(Font.SERIF, Font.BOLD, 50);
        Font passwordFont = new Font(Font.SERIF, Font.PLAIN, 30);
        Font errorFont = new Font(Font.SERIF, Font.PLAIN, 20);
        
        System.out.println(screenWidth);
        System.out.println(screenHeight);
        
        //Set up frame.
        frame.setTitle("Lei e Bella");
        frame.setLayout(layout);
        frame.setSize(screenWidth, screenHeight);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.PINK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Decorate and Alignment
        title.setFont(titleFont);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        password.setFont(passwordFont);
        password.setAlignmentX(Component.CENTER_ALIGNMENT);
        input.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        input.setMaximumSize(new Dimension(200, 50));
        loginButton.setMaximumSize(new Dimension(150,70));
        loginButton.setFont(passwordFont);
        status.setAlignmentY(Component.CENTER_ALIGNMENT);
        status.setFont(errorFont);
        status.setForeground(Color.RED);
        
        //Add to frame. 
        frame.add(title);
        frame.add(Box.createRigidArea(new Dimension(0,screenHeight/2)));
        frame.add(password);
        frame.add(input);
        frame.add(loginButton);
        frame.add(status);
        
        
        //Show frame.
        frame.setVisible(true);
         
        //Action Listener for when login is pressed. 
        loginButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                char[] enteredPass = input.getPassword();
                sql.getConnection(enteredPass);
                if(sql.isConnected() == true) {
                    //Load main gui here.
                    frame.setVisible(false);
                    displayMain();
                    status.setText("Connected!");
                }else {
                    status.setText(sql.getLoginError());
                }
            }
        });
              
        
    }//end displayLogin()
    
    //This will show the main screen of our program. 
    public void displayMain() {
        
        JFrame frame = new JFrame(); 
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15,15,15,15); //spacing. 
        
        //Font
        Font font = new Font(Font.SERIF, Font.PLAIN, 25);
       
         //Set up frame.
        frame.getContentPane().add(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenWidth, screenHeight); 
        frame.setTitle("Maddie's Lashes");
        frame.getContentPane().setBackground(Color.pink);
        panel.setBackground(Color.pink);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Labels - Buttons - Constraints - Adding to frame. 
        
        JButton allClients_Button = new JButton("All Clients");
        c.gridx = 0; 
        c.gridy = 0;
        allClients_Button.setFont(font);
        panel.add(allClients_Button,c);
        
        //allClients_Button Action.
        allClients_Button.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                try {
                    sql.getClients();
                } catch (SQLException ex) {
                    System.out.println("Error:" + ex.getMessage());
                }
            }
        });
        
        JButton newClient = new JButton("Add Client"); 
        c.gridx = 1; 
        c.gridy = 0;
        newClient.setFont(font);
        panel.add(newClient, c);
        //newClient Action
        newClient.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
               display_AddClient(); 
            }
        });
         
        //EDIT CLIENT Action
        JButton editClientBTN = new JButton("Edit Client"); 
        editClientBTN.setFont(font);
        c.gridx = 2; 
        c.gridy = 0; 
        editClientBTN.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent ae) {
                displayEditClient();
            }
        });
        
        panel.add(editClientBTN, c);
        
        JButton clientNotes = new JButton("Client Notes"); 
        clientNotes.setFont(font);
        c.gridx = 3; 
        c.gridy =0;
        clientNotes.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                //*******************************
            }
        });
        
        panel.add(clientNotes,c);
        
        JButton clientNumberSearch = new JButton("Search Client By Phone");
        clientNumberSearch.setFont(font);
        c.gridx = 0; 
        c.gridy = -1;
        clientNumberSearch.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                displaySearchPhone(); 
            }
        });
        
        panel.add(clientNumberSearch,c);
        
        JButton searchClientBTN = new JButton("Search Client Transactions"); 
        searchClientBTN.setFont(font);
        c.gridx = 1; 
        c.gridy = -1; 
        searchClientBTN.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                
              //Call method to display client search window.
              displaySearchTran();
               
            }
        });
        panel.add(searchClientBTN, c);
        
        JButton monthTransBTN = new JButton("Monthly Transactions");
        monthTransBTN.setFont(font);
        c.gridx= 2; 
        c.gridy = -1; 
        monthTransBTN.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                 displayMonthlyTrans();
            }
        });
        
        panel.add(monthTransBTN, c);
        
        JButton stats = new JButton("Statistics");
        stats.setFont(font);
        c.gridx = 3 ;
        c.gridy= -1;
        stats.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                //**************************
            }
        });

        panel.add(stats,c);
        
        //Set Visible.
        frame.setVisible(true);
    
    
    
    }//end displayMain()
    
    //This method will show our new frame for when Add Client button is pressed. It will also pass the parameters to our SQL class. 
    public void display_AddClient() {
        
        JFrame frame = new JFrame(); 
        JPanel panel = new JPanel(new GridBagLayout()); 
        GridBagConstraints c = new GridBagConstraints();
        Dimension fieldSize = new Dimension(250,40);
        Font statusFont = new Font(Font.SERIF, Font.PLAIN, 25);
        JLabel statusText = new JLabel();
        
        //Set up frame. 
        c.insets = new Insets(30,0,0,0); //spacing. 
        frame.getContentPane().add(panel, BorderLayout.NORTH); 
        frame.setSize(850, 600);
        frame.setTitle("New Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        
        //Add to panel. 
        JLabel fnameLabel = new JLabel("First Name: ");
        fnameLabel.setFont(mainFont);
        c.gridx = 0; 
        c.gridy = 0; 
        
        panel.add(fnameLabel, c);
        
        JTextField fNameText = new JTextField();
        fNameText.setPreferredSize(fieldSize);
        fNameText.setFont(inputFont);
        c.gridx = 1;
        c.gridy = 0;
        
        panel.add(fNameText, c);
        
        JLabel lnameLabel = new JLabel("Last Name: "); 
        lnameLabel.setFont(mainFont);
        c.gridx = 0; 
        c.gridy = -1; 
        
        panel.add(lnameLabel, c);
        
        frame.setVisible(true);
        
        JTextField lNameText = new JTextField();
        lNameText.setPreferredSize(new Dimension(fieldSize));
        lNameText.setFont(inputFont);
        c.gridx = 1;
        c.gridy = 1;
        
        panel.add(lNameText, c);
        
        JLabel phoneLabel = new JLabel("Phone: ");
        phoneLabel.setFont(mainFont);
        c.gridx= 0; 
        c.gridy = -2;
        
        panel.add(phoneLabel, c); 
        
        JTextField phoneText = new JTextField(); 
        phoneText.setPreferredSize(fieldSize);
        phoneText.setFont(inputFont);
        c.gridx=1; 
        c.gridy= -2;
        
        panel.add(phoneText, c);
        
        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(mainFont);
        c.gridx=0; 
        c.gridy= -3;
        
        panel.add(emailLabel, c); 
        
        JTextField emailText = new JTextField(); 
        emailText.setPreferredSize(new Dimension(450,40));
        emailText.setFont(inputFont);
        c.gridx=1; 
        c.gridy = -3;
        
        panel.add(emailText,c); 
        
        JLabel notesLabel = new JLabel("Notes:"); 
        notesLabel.setFont(mainFont);
        c.gridx = 0; 
        c.gridy = -4; 
        
        panel.add(notesLabel, c);
        
        JTextField noteText = new JTextField(); 
        noteText.setPreferredSize(new Dimension(600,40));
        noteText.setFont(inputFont);
        c.gridx = 1; 
        c.gridy = -4;
        
        panel.add(noteText, c);
        
        JButton submitButton = new JButton("Submit"); 
        submitButton.setFont(mainFont);
        c.gridx = 0; 
        c.gridy = -5; 
        c.gridwidth = 200;
        
        submitButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                
                try {
                    if(fNameText.getText().isEmpty() == false && lNameText.getText().isEmpty() == false) {
                         sql.addClient(fNameText.getText().trim(), lNameText.getText().trim(), phoneText.getText().trim(), emailText.getText().trim());
                        statusText.setText("Client Added Successfully!");
                    }else {
                        statusText.setText("First and Last Name cannot both be empty.");
                    }
                    
                } catch (SQLException ex) {
                    statusText.setText("Error:" + ex.getMessage());
                }
            }
        });
        
        panel.add(submitButton, c);
        
        
        statusText.setFont(statusFont);
        statusText.setForeground(Color.RED);
        c.gridx=0; 
        c.gridy= -6; 
        
        panel.add(statusText, c); 
        
    }//end display_AddClient()
    
    //This method will bring up our JTable in a new window showing the requested data. 
    public void displayData(String[][] data, String[] columnNames) {
        
        JFrame frame = new JFrame(); 
        JTable table = new JTable(data, columnNames);
        JScrollPane jp = new JScrollPane(table);
        
        //Set up frame. 
        table.setRowHeight(40);
        table.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        table.setForeground(Color.BLACK);
        table.setDefaultEditor(Object.class, null);
        frame.add(jp);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenWidth, screenHeight);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);      
        
    }//end displayData()
    
    
    //This method displays a new frame after the Search Client By Phone is pressed.
    public void displaySearchPhone() {
        
        JFrame frame = new JFrame(); 
        JPanel panel = new JPanel(new GridBagLayout()); 
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20,20,20,20);
        
        //Set Up Frame
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Add to frame. 
        frame.add(panel, BorderLayout.NORTH);
        JLabel label = new JLabel("Enter Phone Number with No Spaces or Dashes.");
        label.setFont(new Font(Font.SERIF, Font.BOLD,20));
        c.gridx = 0; 
        c.gridy = 0; 
        panel.add(label, c);
        
        JTextField phoneField = new JTextField("Phone Number..."); 
        phoneField.setPreferredSize(new Dimension(320, 50));
        phoneField.setFont(new Font(Font.DIALOG_INPUT, Font.ITALIC, 18));
        c.gridx = 0 ;
        c.gridy = -1; 
        panel.add(phoneField, c);
        
        JButton searchBTN = new JButton("Search"); 
        searchBTN.setFont(mainFont);
        c.gridx = 0; 
        c.gridy = -2; 
        searchBTN.addActionListener(new ActionListener() {
       
            public void actionPerformed(ActionEvent ae) {
                try {
                    sql.searchClientPhone(phoneField.getText().trim());
                    
                } catch (SQLException ex) {
                    System.out.print(ex.getMessage());
                }
            }
        });
        panel.add(searchBTN, c); 
              
        frame.setVisible(true);
        
    }//end displaySearchPhone
    
    //This method displays a new frame after the button Search Client Transactions Is Pressed. 
    public void displaySearchTran() {
        JFrame frame = new JFrame(); 
        JPanel panel = new JPanel(new GridBagLayout()); 
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(30,0,0,0); //spacing. 
        
        frame.getContentPane().add(panel);
        
        JTextField fNameField = new JTextField("First Name..."); 
        fNameField.setFont(new Font(Font.DIALOG_INPUT, Font.ITALIC, 18));
        fNameField.setPreferredSize(new Dimension(320, 50));
        c.gridx = 0; 
        c.gridy = 0; 
        
        panel.add(fNameField,c);
        
        JTextField lNameField = new JTextField("Last Name..."); 
        lNameField.setFont(new Font(Font.DIALOG_INPUT,Font.ITALIC, 18));
        lNameField.setPreferredSize(new Dimension(320,50));
        c.gridx = 0; 
        c.gridy = -1;
        
        panel.add(lNameField,c);
        
        JButton searchBTN = new JButton("Search"); 
        searchBTN.setFont(mainFont);
        c.gridx = 0;
        c.gridy= -2; 
        searchBTN.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent ae) {
                try {
                    sql.searchClientTrans(fNameField.getText().trim(), lNameField.getText().trim());
                    System.out.println(getClientStatus());
                    //If we find no client(query returns empty set.)
                    if(getClientStatus() == false) {
                        JFrame frame = new JFrame();
                        JPanel panel = new JPanel(new GridBagLayout());
                        frame.getContentPane().add(panel);
                        JLabel label = new JLabel("No Client Found");
                        label.setForeground(Color.red);
                        panel.add(label);
                        frame.setSize(200,150);
                        frame.setResizable(false);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                    }//end if()
                    
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        
        panel.add(searchBTN, c);
         
        
        //Set up frame 
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        
        
    }//end displaySearchTran()

    //This method will be called when "Edit Client" button is pressed in main display. 
    public void displayEditClient() {
        
        JFrame frame = new JFrame(); 
        JPanel panel = new JPanel(new GridBagLayout()); 
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(30,0,0,0); //spacing
        
        frame.getContentPane().add(panel);
        
        JLabel l1 = new JLabel("Edit Client");
        l1.setFont(mainFont);
        c.gridx = 0; 
        c.gridy = 0; 
        panel.add(l1, c); 
        
        JTextField clientID_Field = new JTextField("The ID of the client you wish to edit..."); 
        clientID_Field.setFont(new Font(Font.DIALOG_INPUT, Font.ITALIC, 18));
        clientID_Field.setPreferredSize(new Dimension(350,50)); 
        c.gridx = 0; 
        c.gridy = -1; 
        panel.add(clientID_Field, c);
        
        JTextField fNameField = new JTextField("First Name..."); 
        fNameField.setFont(new Font(Font.DIALOG_INPUT, Font.ITALIC, 18));
        fNameField.setPreferredSize(new Dimension(320, 50));
        c.gridx = 0; 
        c.gridy = -2; 
        
        panel.add(fNameField,c);
        
        JTextField lNameField = new JTextField("Last Name..."); 
        lNameField.setFont(new Font(Font.DIALOG_INPUT,Font.ITALIC, 18));
        lNameField.setPreferredSize(new Dimension(320,50));
        c.gridx = 0; 
        c.gridy = -3;
        
        panel.add(lNameField,c);
        
        //Set up frame 
        frame.setSize(600,400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        
    }//end displayEditClient()
    
    //This method is called when the Monthly Transaction button is pressed.
    public void displayMonthlyTrans() {
        
        JFrame frame = new JFrame(); 
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        
        frame.getContentPane().add(panel,BorderLayout.NORTH);
        
        //Set up frame
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        
        
    }//end displayMonthlyTrans()
    
    
    //This method sets the status of our status boolean, which controls whether or not our status text shows in seperate methods.
    public void setClientStatus(boolean s) {
        
        clientStatus = s; 
        System.out.println("setclient called");
        System.out.println("egkoe " + clientStatus);
        
    }//end setStatus()
    
    
    public boolean getClientStatus() {
        return clientStatus; 
    }//end getStatus()
    
}//end GUI 
