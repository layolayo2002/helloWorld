/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.service;

import java.util.List;
import layo.clo.entity.Customer;
import layo.clo.entity.Order;
import layo.clo.entity.Outlet;
import layo.clo.entity.Product;
import layo.clo.entity.CLOException;
import layo.clo.entity.VIP;

/**
 *
 * @author Administrator
 */
public class OrderService {
    private OrdersDAO dao = new OrdersDAO();

    public void insert(Order order) throws CLOException {
        dao.insert(order);
    }

    public List<Order> getOrdersByCustomerAccount(String customerAccount) throws CLOException {
        return dao.getOrdersByCustomerAccount(customerAccount);
    }

    public Order get(int id) throws CLOException {
        return dao.get(id);
    }
    
    public Order get(String customerAccount, int id) throws CLOException {
        Order o = dao.get(id);
        if(o==null) return null;
        if(o.getCustomer().getAccount().equals(customerAccount)){
            return o;
        }else{
            throw new CLOException("無權檢視此訂單");
        }        
    }
    
    //OCPJP mod10 Demo用
    public double order(Customer c, Product p, int quantity){        
        double amount = p.getUnitPrice() * quantity;
        if(c instanceof VIP && !(p instanceof Outlet)){ //p.getClass()==Product.class
            VIP v = (VIP)c;
            amount = amount * (100 - v.getDiscount())/100;
        }
        
        return amount;
    }
    
    public void sendPaid(int orderId, String customerId, String bank, String lastCode, double amount, String transDateTime) 
            throws CLOException{
        String paymentNote="轉帳銀行: " + bank;
        paymentNote+=", 後5碼: " + lastCode;
        paymentNote+=", 金額: " + transDateTime;
        paymentNote+=", 時間: " + transDateTime;
        dao.sendPaid(orderId, customerId, paymentNote);
    }
    
    
     public void authenticateCard(int orderId, String authCode, String cardStr, String merchantTradeNo, String tradeNo, 
                String rtnCode, double parseDouble) throws CLOException {
        String paymentNote = "授權碼:"+authCode + ",信用卡號:" + cardStr + " ,付款金額:" + parseDouble + " ,RtnCode:" + rtnCode
                + "\n,OPay訂單號:" + merchantTradeNo + ",OPay授權單號:" + tradeNo ;
        System.out.println("paymentNote = " + paymentNote);
        dao.authConfirmCard(orderId, paymentNote);        
    }    

}
