/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.Arrays;
import layo.clo.entity.PaymentType;
import layo.clo.entity.ShippingType;

/**
 *
 * @author Administrator
 */
public class TestPaymentType {
    public static void main(String[] args) {
        System.out.println(PaymentType.ATM.toString());
        System.out.println(PaymentType.ATM.name());
        System.out.println(PaymentType.ATM.ordinal());//1
        
        PaymentType[] values = PaymentType.values();
        for(PaymentType pType:values){
            System.out.println("toString(): "+ pType.toString());
            System.out.println("name():" + pType.name());
            System.out.println("ordinal():" + pType.ordinal());
            System.out.println("getDescription():" + pType.getDescription());
            System.out.println("getFee():" + pType.getFee());
        }
        
        System.out.println("以下為ShippingType:");
        ShippingType[] shippingTypeValues = ShippingType.values();
        for(ShippingType shipType:shippingTypeValues){
            System.out.println(shipType.toString());
            System.out.println(shipType.name());
            System.out.println(shipType.ordinal());
            System.out.println(shipType.getDescription());
            System.out.println(shipType.getFee());
            
            ShippingType[] shipT= PaymentType.valueOf("ATM").getShippingTypeArray();
            
            
            System.out.println("*******************");
            System.out.println(Arrays.toString(shipT));
            
        }
    }
}
