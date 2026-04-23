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
public class Outlet extends Product {

    private int discount = 21;

    public Outlet() {
    }

    public Outlet(int id, String name, double unitPrice) {
        super(id, name, unitPrice);
    }

    public Outlet(int idData, String name, double unitPrice, int stock) {
        super(idData, name, unitPrice, stock);
    }
    
    

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDiscountString() {
        int discount = 100 - this.discount;
        if (discount % 10 == 0) {
            return discount / 10 + "折";
        } else {
            return discount + "折";
        }
    }

    @Override
    public double getUnitPrice() { //查詢售價        
        return super.getUnitPrice() * (100D - discount) / 100; // * (100D-discount) / 100;
    }

    public double getListPrice() {//查詢原價
        return super.getUnitPrice();
    }

    @Override
    public String toString() {
        return super.toString() + ",\n享有折扣=" + discount + "% off"
                + ", " + getDiscountString() + "售價:" + this.getUnitPrice();
    }
}
