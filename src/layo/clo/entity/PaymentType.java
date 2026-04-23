/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.entity;

/**
 *
 * @author Administrator
 */
public enum PaymentType  { //Enum    
    FACE("面交", ShippingType.FACE),
    ATM("ATM轉帳", ShippingType.HOME, ShippingType.STORE),
    HOME("貨到付款", 100, ShippingType.HOME),
    CARD("信用卡付款",new ShippingType[]{ShippingType.HOME, ShippingType.STORE}),
    STORE("超商付款",15,new ShippingType[]{ShippingType.STORE});
    
    
    private final String description; //
    private final double fee; //
    private final ShippingType[] shippingTypeArray; //
    
    private PaymentType(String description, ShippingType... shippingTypeArray) {
//        this.description = description;
//        this.fee = 0;
        this(description, 0, shippingTypeArray);
    }

    private PaymentType(String description, double fee, ShippingType... shippingTypeArray) {
        this.description = description;
        this.fee = fee;
        this.shippingTypeArray = shippingTypeArray;
    }

    public ShippingType[] getShippingTypeArray() {
        return shippingTypeArray.clone();
    }
    
    

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the fee
     */
    public double getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return description + (fee>0 ? ","+fee+'元' : "");
    }
    
    
}
