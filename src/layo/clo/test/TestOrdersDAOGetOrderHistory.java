/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.Order;
import layo.clo.entity.CLOException;
import layo.clo.service.OrdersDAO;

/**
 *
 * @author Administrator
 */
public class TestOrdersDAOGetOrderHistory {
    public static void main(String[] args) {
        try {
            OrdersDAO dao = new OrdersDAO();
            //System.out.println(dao.getOrdersByCustomerId("A223456781"));
            
            Order order = dao.get(1);
            System.out.println("order = " + order);
            
        } catch (CLOException ex) {
            Logger.getLogger(TestOrdersDAOGetOrderHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
