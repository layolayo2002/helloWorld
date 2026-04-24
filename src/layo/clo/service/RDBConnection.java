/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.service;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.CLOException;

/**
 *
 * @author Administrator
 */
public class RDBConnection {

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://G-21:3306/clothing?zeroDateTimeBehavior=convertToNull";      
    private static final String userid = "root";
    private static final String pwd = "1234";
    
    // Static initializer to load JDBC driver once at class load time (not per connection)
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RDBConnection.class.getName()).log(Level.SEVERE, "無法載入JDBC Driver:" + driver, ex);
            throw new ExceptionInInitializerError("Failed to load JDBC driver: " + driver);
        }
    }

    public static Connection getConnection() throws CLOException {
        try {
            // Driver already loaded in static initializer - just create connection
            Connection connection = DriverManager.getConnection(url, userid, pwd);
            return connection;                
        } catch (SQLException ex) {
            Logger.getLogger(RDBConnection.class.getName()).log(Level.SEVERE, "建立資料庫連線失敗", ex);
            throw new CLOException("建立資料庫連線失敗", ex);
        }
    }
}