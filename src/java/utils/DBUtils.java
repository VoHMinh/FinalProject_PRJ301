/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtils { 
        private static final String DB_Name="Final_assignment";
        private static final String DB_Username="sa";
        private static final String DB_Password="12345";
        
   
    public static Connection getConnection() throws ClassNotFoundException,SQLException{
            Connection conn=null;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://localhost:1433;databaseName="+DB_Name;
            conn=DriverManager.getConnection(url,  DB_Username, DB_Password);
            return conn;
        }
        public static void main(String[] args) {
        try {
            Connection c= getConnection();
            System.out.println(c);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
