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
import layo.clo.entity.PaymentType;
import layo.clo.entity.Product;
import layo.clo.entity.ShippingType;
import layo.clo.entity.ShoppingCart;
import layo.clo.entity.CLOException;
import layo.clo.service.CustomerService;
import layo.clo.service.OrderService;
import layo.clo.service.ProductService;

/**
 *
 * @author Administrator
 */
public class TestOrderServiceInsert {

    public static void main(String[] args) {
        try {
            CustomerService cService = new CustomerService();
            Customer c1 = cService.login("layo20171121", "a55667788");
            System.out.println("c1:" + c1);

            ProductService service = new ProductService();
            Product p1 = service.get(123456757); //1,
            p1.setColor("灰色");
            p1.setSize("S");
            System.out.println("p1 = " + p1);
            ShoppingCart cart = new ShoppingCart();
            
            cart.setMember(c1);
            cart.add(p1);
           System.out.println(cart);

            Product p3 = service.get(123456757); //2
            p3.setColor("深藍");
            p3.setSize("M");
            cart.add(p3, 1);
            System.out.println("*********************************");
            System.out.println(cart);
            
            Product p2 = service.get(123456757); //2
            p2.setColor("灰色");
            p2.setSize("M");
            cart.add(p2, 1);
            System.out.println("*********************************");
            System.out.println(cart);   
            
            Order order = new Order();
            order.setCustomer(cart.getMember());
            order.add(cart);
            
            order.setPaymentType(PaymentType.ATM);
            order.setPaymentFee(PaymentType.ATM.getFee());
            
            order.setShippingType(ShippingType.HOME);
            order.setShippingFee(ShippingType.HOME.getFee());
            
            order.setReceiverName(c1.getName());
            order.setReceiverEmail(c1.getEmail());
            order.setReceiverPhone(c1.getPhone()!=null?c1.getPhone():"0225149191");
            order.setReceiverAddress(c1.getAddress()!=null?c1.getAddress():"台北市復興北路99號1F");
            System.out.println(order); //編號: 0
            
            //insert order
            OrderService orderService = new OrderService();
            orderService.insert(order);
            
            System.out.println("*****************************************************");
            System.out.println(order); //編號: ?

        } catch (CLOException ex) {
            Logger.getLogger(TestOrderServiceInsert.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
