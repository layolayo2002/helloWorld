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
import layo.clo.service.OrderService;

/**
 *
 * @author Administrator
 */
public class TestOrderServiceGetOrderHistory {
    public static void main(String[] args) {
        try {
            OrderService service = new OrderService();
            //System.out.println(dao.getOrdersByCustomerId("A223456781"));
            
            Order order = service.get("layo20171121", 1);
            System.out.println("order = " + order);
            
        } catch (CLOException ex) {
            Logger.getLogger(TestOrderServiceGetOrderHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
