/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.service.RDBConnection;

public class TestRDBConnection {
    public static void main(String[] args) {        
        try (Connection connection = RDBConnection.getConnection()){
            System.out.println("已成功取得連線: " + connection.getCatalog());
        } catch (Exception ex) {
            Logger.getLogger(TestRDBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
