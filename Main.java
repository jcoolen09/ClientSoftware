/*
Last Updated: 05/10/2019
 */
package lashes;

import java.sql.SQLException;

/**
 *
 * @author James Coolen
 */
public class Main {

  
    public static void main(String[] args) throws SQLException {
        
        SQLHandler sql = new SQLHandler(); 
        GUI gui = new GUI();
        
        //Ask for password to database, and connect to datbase.  
        gui.displayLogin();
        
    }//end main method
    
    
    
    
    
    
}//end class
