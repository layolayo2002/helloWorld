/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.service;

import java.util.List;
import layo.clo.entity.Product;
import layo.clo.entity.CLOException;

/**
 *
 * @author Administrator
 */
public class ProductService {
    private ProductsDAO dao = new ProductsDAO();

    public Product get(int id) throws CLOException {
        return dao.get(id);
    }

    
    public List<Product> getColorSize(int id) throws CLOException {
        return dao.getColorSize(id);
    }
    public List<Product> getRecommandedProducts() throws CLOException {
        return dao.getRecommandedProducts();
    }

    public List<Product> getTotalProducts() throws CLOException {
        return dao.getTotalProducts();
    }
    
    
      public List<Product> getNewProducts() throws CLOException {
        return dao.getNewProducts();
    }
       public List<Product> getOutletProducts() throws CLOException {
        return dao.getOutletProducts();
    }
      
        public List<Product> getProductsByClassification(String classification) throws CLOException {
        return dao.getProductsByClassfication(classification);
    }
        
         public List<Product> getProductsByDetail(String detail) throws CLOException {
        return dao.getProductsByDetail(detail);
    }

    public List<Product> getProductsByName(String name) throws CLOException {
        return dao.getProductsByName(name);
    }
   
    
    
   public int getColorNo(String color) throws CLOException{
       return dao.getColorNo(color);
   }
    
   public int getSizeNo(String size) throws CLOException{
       return dao.getSizeNo(size);
   }
    //OCPJP mod05 : pass by value, not business method
    public void addPrice(double price){
        price = price + 100;
    }
    
    //OCPJP mod05 : pass by value, not business method
    public void addPrice(Product p){
        double price = p.getUnitPrice()+100;
        p.setUnitPrice(price);        
    }
    
    
    public int findStockByIdColorSize(int id,String color,String size) throws CLOException{
        return dao.findStockByIdColorSize(id, color, size);
    }
    
    
//    public double addPrice(final double price){
//        double rtn = price + 100;
//        return rtn;
//    }
//    
//    public double addPrice(final Product p){
//        double price = p.getUnitPrice()+100;
//        //p.setUnitPrice(price);        
//        return price;
//    }    

    public int getLastOneProductId() throws CLOException {
        return dao.getLastOneProductId();
    }

    public void insert(Product p) throws CLOException {
        dao.insert(p);
 
    }

    public List<String> getTotalColor() throws CLOException {
        return dao.getTotalColor();
    }

    public List<String> getTotalSize() throws CLOException {
        return dao.getTotalSize();
    }

    public void addColor(String color) throws CLOException {
        dao.addColor(color);
    }

    public void updateStock(Product p) throws CLOException {
        dao.updateStock(p);
    }

    public void insertStock(Product p) throws CLOException {
        dao.insertStock(p);
    }

    public void update(Product p) throws CLOException {
        dao.update(p);
    }
    
    
    
}
