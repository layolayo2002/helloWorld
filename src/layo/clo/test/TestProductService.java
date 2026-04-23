/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import layo.clo.entity.CLOException;
import layo.clo.entity.Product;
import layo.clo.service.ProductService;

/**
 *
 * @author Administrator
 */
public class TestProductService {

    public static void main(String[] args) throws CLOException {
        ProductService service = new ProductService();
//        int stock=service.findStockByIdColorSize(123456731,"灰色","S");
//        System.out.println("stock = " + stock);
        
        //測試拿取最後一個productid
        int id=service.getLastOneProductId();
        System.out.println("id = " + id);
        
        
      /*  String[] pcolor =new String[6];
        String[] psize = new String[6];

        int id = 123456757;
        List<Product> plist = new ArrayList<>();
        plist = service.getColorSize(id);

        //   System.out.println(plist);
         int i=0;
        for (Product pColorSize : plist) {
            if(i==0){
             pcolor[i]=pColorSize.getColor();
             i++;
             continue;
            }
           for(int j=i-1;j>=0;j--){
               if(pColorSize.getColor().equals(pcolor[j])){
                   break;//跳到外層迴圈
               }else if(j==0){
                   pcolor[i]=pColorSize.getColor();
                   i++;
               }
           }
        }
        
     i=0;
         for (Product pColorSize : plist) {
            if(i==0){
               
             psize[i]=pColorSize.getSize();
             i++;
             continue;
            }
           for(int j=i-1;j>=0;j--){
               if(pColorSize.getSize().equals(psize[j])){
                   break;//跳到外層迴圈
               }else if(j==0){
                   psize[i]=pColorSize.getSize();
                   i++;
               }
           }
        }*/

    
        
       
    }
}
