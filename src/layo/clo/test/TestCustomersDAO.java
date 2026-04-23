/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.Customer;
import layo.clo.entity.CLOException;
import layo.clo.entity.VIP;
import layo.clo.service.CustomersDAO;

/**
 *
 * @author Administrator
 */
public class TestCustomersDAO {
    public static void main(String[] args) {
        try {
             CustomersDAO dao = new CustomersDAO();
            //新增測試
//           Customer c = new VIP("a22557633","A223456772", "吳慈仁", "123456");
//            c.setGender(Customer.FEMALE);
//            c.setEmail("nobudy.wu.tw@gmail.com");
//            if(c instanceof VIP){
//                ((VIP)c).setDiscount(20);
//            }
//            dao.insert(c);   
//           System.out.println("c = " + c);

            //查詢測試
//           Customer c2 = dao.get("layo2017112");
//            System.out.println("c2 = " + c2);
            
            //修改
            
          
    
            Customer c = dao.get("aabbccc");
            
            System.out.println(c);
            
//            c.setName("張三丰");
//            c.setBirthday(1999, 10, 10);
//            c.setStatus("1");
//            dao.update(c);
//            
//            System.out.println(c);
            
            
        } catch (CLOException ex) {
            Logger.getLogger(TestCustomersDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
