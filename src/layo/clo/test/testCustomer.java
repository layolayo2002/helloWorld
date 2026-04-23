/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import layo.clo.entity.CLOException;
import layo.clo.entity.Customer;

/**
 *
 * @author Administrator
 */
public class testCustomer {
    public static void main(String[] args) throws CLOException {
        Customer c =new Customer("1234","A123456789","123","123456",'F',"layo@gmail.com");
        System.out.println(c);
    }
}
