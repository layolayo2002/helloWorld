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

    public static Connection getConnection() throws CLOException {
//         java.net.InetAddress ip;
//        try {
//            ip = java.net.InetAddress.getLocalHost();
//             String myIp = ip.getHostAddress();
//             url="jdbc:mysql://G-21:3306/clothing?zeroDateTimeBehavior=convertToNull"; 
//             System.out.println(url);
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(RDBConnection.class.getName()).log(Level.SEVERE, null, ex);
//        }
           
        try {
            //1. 載入JDBC Driver
            Class.forName(driver);            
            try {
                //2. 建立Connection
                Connection connection = DriverManager.getConnection(url, userid, pwd);
                return connection;                
            } catch (SQLException ex) {
                Logger.getLogger(RDBConnection.class.getName()).log(Level.SEVERE, "建立資料庫連線失敗", ex);
                throw new CLOException("建立資料庫連線失敗", ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RDBConnection.class.getName()).log(Level.SEVERE, "無法載入JDBC Driver:" + driver, ex);
            throw new CLOException("無法載入JDBC Driver:" + driver, ex);
        }        
    }
}