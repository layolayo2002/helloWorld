/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.CLOException;
import layo.clo.entity.Customer;
import layo.clo.entity.Product;
import layo.clo.entity.ShoppingCart;
import layo.clo.service.CustomerService;
import layo.clo.service.ProductService;


/**
 *
 * @author Administrator
 */
public class TestShoppingCart {
    public static void main(String[] args) {
        try {
            CustomerService cService = new CustomerService();
            Customer c1 = cService.login("layo20171121", "a55667788");
            System.out.println("c1:" + c1);
            
            ProductService service = new ProductService();
            Product p1 = service.get(123456701); //1,
            
            ShoppingCart cart = new ShoppingCart();
            cart.setMember(c1);
            cart.add(p1);
            
            System.out.println(cart);
//            System.out.println(cart.getSize());
//            System.out.println(cart.getTotalQuantity());
//            System.out.println(cart.getTotalAmount());            
            
            Product p3 = service.get(123456703); //2
            cart.add(p3, 1);
            System.out.println("*********************************");
            System.out.println(cart);
//            System.out.println(cart.getSize());
//            System.out.println(cart.getTotalQuantity());
//            System.out.println(cart.getTotalAmount());
            
            Product p2 = service.get(123456704); //2
            cart.add(p2, 2);
            System.out.println("*********************************");
            System.out.println(cart);
//            System.out.println(cart.getSize());
//            System.out.println(cart.getTotalQuantity());
//            System.out.println(cart.getTotalAmount());
            
            
        } catch (CLOException ex) {
            Logger.getLogger(TestShoppingCart.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
}
