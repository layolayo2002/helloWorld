/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.Customer;
import layo.clo.entity.Order;
import layo.clo.entity.CLOException;
import layo.clo.service.CustomerService;
import layo.clo.service.OrderService;
import layo.clo.service.OrdersDAO;

/**
 *
 * @author Administrator
 */
public class TestOrderService_SendPaid1 {

    public static void main(String[] args) {
        try {
            CustomerService cService = new CustomerService();
            Customer member = cService.login("layo20171121", "a55667788");

            OrderService oService = new OrderService();
            Order order = oService.get(member.getAccount(), 1);

            oService.sendPaid(order.getId(), member.getAccount(), "恆逸銀行", "12345", 1768, "2017-12-14 11:00-11:59");
        } catch (CLOException ex) {
            Logger.getLogger(TestOrderService_SendPaid1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
