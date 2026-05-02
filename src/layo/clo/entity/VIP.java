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
public class VIP extends Customer{
    private int discount=10; //10% off
    
    public VIP(){        
    }    
    
    public VIP(String account,String id, String name, String password) throws CLOException{
        super(account,id, name, password);
    }

    public VIP(String account,String id, String name, String password,int discount)  throws  CLOException{
        super(account,id, name, password);
        this.discount = discount;
    }

    public VIP(String account,String id, String name, String password, char gender, String email) throws CLOException {
        super(account,id, name, password, gender, email);
    }

    
    
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        if(discount>=0 && discount<91){
            this.discount = discount;
        }else{
            System.err.println("VIP折扣必須在0~90之間");
        }        
    }

    @Override
    public String getType() {
        return "VIP";
    }
    
    public String getDiscountString(){
        int discount = 100-this.discount;
        if(discount%10 == 0){
            return discount/10 + "折";
        }else{        
            return discount + "折";
        }
    }    

    @Override
    public String toString() {
        return super.toString() + ",\n享有折扣=" + discount + "% off"
                + ", " + getDiscountString();
    }
}
