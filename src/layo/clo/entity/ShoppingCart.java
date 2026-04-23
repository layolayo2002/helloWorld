/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.entity;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class ShoppingCart {

    private Customer member;
    private Map<Product, Integer> cart = new HashMap<>();

    /**
     * @return the member
     */
    public Customer getMember() {
        return member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(Customer member) {
        this.member = member;
    }

    //以下為map: cart的add, update, remove, 皆為mutators
    public void add(Product p) {
        add(p, 1);
    }

    public void add(Product p, int q) {
        if (q > 0) {
            Integer oldQuantity = cart.get(p);

            if (oldQuantity == null || oldQuantity.intValue() == 0) {
                cart.put(p, q);
            } else {
                cart.put(p, q + oldQuantity);
            }
        } else {
            System.out.println("購買數量不正確:" + q);
        }
    }

    public void update(Product p, int q) {
        if (q > 0) {
            cart.put(p, q);
        } else {
            cart.remove(q);
        }
    }

    public void remove(Product p) {
        cart.remove(p);
    }

    //以下為map: cart的accessers
    public Set<Product> getProductSet() { //delegate method
        return cart.keySet();
    }

    public int getQuantity(Product p) {//delegate method
        Integer q = cart.get(p);
        return q != null ? q : 0;
    }

    public int getSize() {//delegate method
        return cart.size();
    }

    public boolean isEmpty() {//delegate method
        return cart.isEmpty();
    }

    public int getTotalQuantity() { //business method: 總購買件數
        int sum = 0;
        for (Integer q : cart.values()) {
            if (q != null && q > 0) {
                sum += q;
            }
        }
        return sum;
    }


    public double getTotalAmount() {//business method: 總購買金額

        double total = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product p = entry.getKey();
            double amount = p.getUnitPrice() * entry.getValue();
            if (member instanceof VIP && !(p instanceof Outlet)) {
                amount = amount * (100 - ((VIP) member).getDiscount()) / 100;
            }
            total += amount;
        }
        return total;
    }

    public double getOriginalTotalAmount() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            double amount = entry.getKey().getUnitPrice() * entry.getValue();
            total += amount;
        }
        return total;
    }

    @Override
    public String toString() {

        return "ShoppingCart{" + "member=" + member + ", cart=" + cart + '}'
                + "共" + this.getSize() + "種,"
                + "共" + this.getTotalQuantity() + "件,"
                + "總金額共" + this.getTotalAmount() + "元";
    }

}
