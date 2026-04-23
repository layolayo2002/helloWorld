/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.Product;
import layo.clo.entity.CLOException;
import layo.clo.service.ProductsDAO;

/**
 *
 * @author Administrator
 */
public class TestProductsDAO {

    public static void main(String[] args) {
        try {
            ProductsDAO dao = new ProductsDAO();

//測試get(1):查詢商品代號為123456705的商品
//           Product p = dao.get(123456705);
//            System.out.println(p);
//測試getAll():查詢全部商品的清單
//            List<Product> list = dao.getNewProducts();            
//            System.out.println(list);
//            System.out.println(list.size());
//            
//測試getAll():查詢商品名稱中有[色鉛筆]的商品清單
//          List<Product> list2 = dao.getProductsByName("褲");            
//            System.out.println(list2);
//            System.out.println(list2.size());


//測試getProductsByClassification("COAT")
//            List<Product> list2 = dao.getProductsByClassfication("COAT");
//            System.out.println(list2);
//            System.out.println(list2.size());

//         List<Product> list2 = dao.getProductsByDetail("七分/長袖");
//            System.out.println(list2);
//            System.out.println(list2.size());
//            List<Product> list = dao.getOutletProducts();            
//            System.out.println(list);
//            System.out.println(list.size());
            
//測試getidcolorsize

        List<Product> plist = dao.getColorSize(123456731);
            System.out.println(plist);
                



            
            
        } catch (CLOException ex) {
            Logger.getLogger(TestProductsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
