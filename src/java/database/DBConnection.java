/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ANKIT
 */
public class DBConnection {
    
    static Connection con=null;
    
    public static Connection getDBConnection() {
        
        try {
            String dbhost, dbname, dbuser, dbpass;
//            dbhost = "jws-app-mysql:3306";
            dbhost = "localhost:3306";
            dbname = "captagram";
            dbuser = "user";
            dbpass = "password";

            Class.forName("com.mysql.jdbc.Driver");

            String dbURL = "jdbc:mysql://" + dbhost + "/" + dbname;

            con = DriverManager.getConnection(dbURL, dbuser, dbpass);

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("change connection to localhost for local system\nDriver not found");
        }

        return con;
    }
    
}
