/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import layo.clo.entity.PaymentType;
import layo.clo.entity.ShippingType;

/**
 *
 * @author Administrator
 */
public class TestShippingType {
    public static void main(String[] args) {
        
        PaymentType ptype=PaymentType.ATM;
        ShippingType[] shippingtype= ptype.getShippingTypeArray();
        
        for(ShippingType stype:shippingtype){
            System.out.println(stype.getFee());
        }
      
    }
}
