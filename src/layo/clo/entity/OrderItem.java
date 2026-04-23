package layo.clo.entity;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.service.ProductsDAO;




public class OrderItem {
    private int orderId;
    private Product product;
    private double price;
    private int quantity;
   

    public String getColor() {
        return (product==null)?product.getColor():"";
    }



    public String getSize() {
        return (product==null)?product.getSize():"";
    }

  
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {        
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity)throws CLOException {
        if(quantity>0){
            this.quantity = quantity;
        }else{
            throw new CLOException("訂購數量必須大於0");
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.orderId;
        hash = 41 * hash + Objects.hashCode(this.product);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderItem other = (OrderItem) obj;
        if (this.orderId != other.orderId) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
 
        
        
        return "訂單=" + orderId + ", 產品=" + product 
                + ", 交易價=" + price + ", 數量=" + quantity + '}';
    }

    
    
    
    
}
