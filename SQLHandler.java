/*
 This class handles all actions that interact with the database. 
 */
package lashes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLHandler {

    private String url;
    private String username;
    private String password;
    private String driver;

    public boolean connected;
    public String errorText;

    Connection conn;
    Statement s;
    ResultSet rs;

    public SQLHandler() throws SQLException {
        connected = false;
    }

    public Connection getConnection(char[] pass) {
        //Convert pass to a string 
        password = "";
        int passwordSize = pass.length;

        for (int i = 0; i < passwordSize; i++) {
            password = password + pass[i];
        }

        url = "jdbc:mysql://localhost:3306/lashes";
        username = "root";
        driver = "com.mysql.cj.jdbc.Driver";

        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection etablished");
            s = conn.createStatement();
            connected = true;
            return conn;

        } catch (Exception e) {
            connected = false;
            errorText = e.getMessage();
        }

        return null;

    }//end getConnection()

    //When the Show All Clients button is pressed, this method executes.
    public void getClients() throws SQLException {
        int xIndex = 0;
        int yIndex = 0;
        String[] columnHeaders = {"First Name", "Last Name", "Phone", "Email", "Revenue"};
        String[][] data;

        //First we need to get size of array by finding out how many clients are in the database. 
        rs = s.executeQuery("SELECT COUNT(clientID) FROM Client");
        rs.next();
        int size = Integer.parseInt(rs.getString("count(clientID)"));
        //Our data array will have "size" elements, and each array will have 6 elements.
        data = new String[size][5];

        rs = s.executeQuery("SELECT fname,lname,phone,email,sum(round(price,2)) FROM Client, Service WHERE clientID = clientID_FK GROUP BY clientID ORDER BY sum(round(price,2)) DESC");

        //xIndex controls what index our data[] is at, and yIndex controls what element the array inside of our data is at.
        //This loads the data into our data array. 
        while (rs.next()) {

            data[xIndex][yIndex] = rs.getString("fname");
            yIndex++;
            data[xIndex][yIndex] = rs.getString("lname");
            yIndex++;
            data[xIndex][yIndex] = rs.getString("phone");
            yIndex++;
            data[xIndex][yIndex] = rs.getString("email");
            yIndex++;
            data[xIndex][yIndex] = "$" + rs.getString("sum(round(price,2))");
            xIndex++;
            yIndex = 0;

        }//end while

        //Show Results.
        GUI gui = new GUI();
        gui.displayData(data, columnHeaders);

    }//end getClients()

    //This method is called when the Add Client button is pressed.
    public void addClient(String firstName, String lastName, String phoneNum, String emailAddress) throws SQLException {
        String query = "INSERT INTO Client(lname,fname,phone,email) " + "VALUES(" + ("\"" + lastName.toUpperCase() + "\"" + "," + "\"" + firstName.toUpperCase()
                + "\"" + "," + "\"" + phoneNum + "\"" + ","
                + "\"" + emailAddress + "\"" + ")");

        s.execute(query);
        //Create an empty data row in the service table that assoiates with this client id. 
        query = "INSERT INTO Service(price, clientID_FK) " + "VALUES(0 " + ",\"" + getRecentClientID() + "\"" + ")";
        s.execute(query);

    }//end addClient()

    public void searchClientPhone(String num) throws SQLException {

        String query = "SELECT clientID,fname,lname,phone,email, sum(round(price,2)) from client"
                + " JOIN SERVICE ON clientID = clientID_FK WHERE phone = " + num;
        int xIndex = 0;
        int yIndex = 0;
        String[] columnHeaders = {"ClientID", "First Name", "Last Name", "Phone", "Email", "Revenue"};
        String[][] data;
        //Find size of data. 
        rs = s.executeQuery("SELECT count(clientID) FROM Client where phone = " + num);
        rs.next();
        int size = Integer.parseInt(rs.getString("count(clientID)"));
        //Declare data size. 
        data = new String[size][6];

        rs = s.executeQuery(query);

        if (size > 0) {

            while (rs.next()) {

                data[xIndex][yIndex] = rs.getString("clientID");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("fname");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("lname");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("phone");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("email");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("sum(round(price,2))");
                yIndex++;

                xIndex++;
                yIndex = 0;

            }//end while()

            //Display results. 
            GUI gui = new GUI();
            gui.displayData(data, columnHeaders);

        }else {
            
            
        }

    }//end searchClientPhone()
    
    //Updates existing client with new parameters. 
    public void editClient(int id, String fname, String lname, String phoneNum, String email) throws SQLException {
        
        String query = "UPDATE Client SET fname=" + "\""  +  fname + "\"," + "lname=" + "\""  +  lname + "\"," + "phone=" + "\"" + phoneNum + "\","
                + "email=" + "\"" + email + "\"" + " WHERE clientID =" + id;
        System.out.println(query);
        s.execute(query);
        
    }

    public void searchClientTrans(String fname, String lname) throws SQLException {
        GUI gui = new GUI();
        int xIndex = 0;
        int yIndex = 0;
        String[] columnHeaders = {"ClientID", "First Name", "Last Name", "Phone", "Date", "Service", "Price"};
        String[][] data;
        //Find size of data. 
        rs = s.executeQuery("SELECT count(clientID)" + " FROM Client JOIN Service s ON clientID = s.clientID_FK"
                + " WHERE fname=" + "\"" + fname + "\"" + " AND lname=" + "\"" + lname + "\"" + " ORDER BY s.service_date desc");
        rs.next();
        int size = Integer.parseInt(rs.getString("count(clientID)"));
        //Declare data size. 
        data = new String[size][7];
        String query = "SELECT clientID, fname, lname, phone, s.service_date, s.name, s.price " + ""
                + " FROM Client JOIN Service s ON clientID = s.clientID_FK"
                + " WHERE fname=" + "\"" + fname + "\"" + " AND lname="
                + "\"" + lname + "\"" + " ORDER BY s.service_date desc";

        rs = s.executeQuery(query);

        //if our size is 0, then we have empty set, meaning there is no match of that last name on record. 
        if (size > 0) {

            gui.setClientStatus(true);
            System.out.println("True called");
            
            while (rs.next()) {

                data[xIndex][yIndex] = rs.getString("clientID");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("fname");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("lname");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("phone");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("service_date");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("name");
                yIndex++;
                data[xIndex][yIndex] = rs.getString("price");
                yIndex++;

                xIndex++;
                yIndex = 0;

            }//end while()

            //Display results.    
            gui.displayData(data, columnHeaders); 

        } else {

            //No client exists. 
            gui.setClientStatus(false);
            System.out.println("False called");
        }

    }//end searchClientLastname

    public void getMonthlyTrans() {

    }//getMonthlyTrans()

    public void setUsername(String u) {
        username = u;
    }

    public void setURL(String u) {
        url = u;
    }

    public void setDriver(String d) {
        driver = d;
    }

    public String getUsername() {
        return username;
    }

    public String getURL() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getLoginError() {
        return errorText;
    }

    //Executes a query to find the clientID of the recently made client. 
    public String getRecentClientID() throws SQLException {
        String query = "SELECT MAX(clientID) FROM Client";
        ResultSet rs;
        rs = s.executeQuery(query);
        rs.next();
        String recentID = rs.getString("MAX(clientID)");
        return recentID;
    }

    //returns whether we are connected to database or not. 
    public boolean isConnected() {
        return connected;
    }

}//end class
