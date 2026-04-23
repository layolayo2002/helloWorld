/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.entity;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class Product extends Object {

    private int id;
    private String name;
    private double unitPrice; //售價，也是原價
    private int stock;
    private String description;
    private String photoUrl;
    private boolean newproduct = false; //新商品預設為0
    private boolean recommanded = true;
    private String[] sizes;
    private String[] colors;
    private String size;   //客人給的
    private String color;
    private Classification classification;
    private String detail;

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String[] getSizes() {
        return sizes;
    }

    public void setSizes(String[] sizes) {
        this.sizes = sizes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Product() {
    }

    public Product(int id, String name, double unitPrice) {
        this.id = id;
        this.name = name;
        this.setUnitPrice(unitPrice);
    }

    public Product(int idData, String name,
            double unitPrice, int stock) {
        this(idData, name, unitPrice);
        this.stock = stock;
    }

    public void setUnitPrice(double value) {//修改原價(售價)
        if (value >= 0) {
            unitPrice = value;
        } else {
            System.out.println("unitPrice必須大於等於0");
        }
    }

    public double getUnitPrice() { //查詢售價(原價)
        return unitPrice;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id 參數id的值將會指派給屬性id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the free
     */
    /**
     * @param free the free to set
     */
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the photoUrl
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * @param photoUrl the photoUrl to set
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isNewproduct() {
        return newproduct;
    }

    public void setNewproduct(boolean newproduct) {
        this.newproduct = newproduct;
    }

    public boolean isRecommanded() {
        return recommanded;
    }

    public void setRecommanded(boolean recommanded) {
        this.recommanded = recommanded;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public void setClassification(String classification) throws CLOException {
        if (classification != null && classification.length() > 0) {
            try {
                setClassification(Classification.valueOf(classification));
            } catch (RuntimeException ex) {
                throw new CLOException("分類資料不正確:" + classification);
            }
        } else {
            setClassification((Classification) null);
        }
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public Product clone() {
        Product p = new Product();
        p.id = this.id;
        p.name = this.name;
        p.unitPrice = this.unitPrice;
        p.classification = this.classification;
        p.detail = this.detail;
        return p;
    }

//    @Override
//    public String toString() {
//        return "id:" + id + ", name:" + name + ",\n" 
//                + "單價:" + unitPrice + ",庫存:" + stock + ",贈品:" + free + ",\n"
//                + "描述: " + description + ",\n"
//                + "圖檔url:" + photoUrl; 
//    }
    @Override
    public String toString() {
        return "\n" + "Product{" + "id=" + id + ", name=" + name + ", unitPrice=" + unitPrice + ", stock=" + stock + ", description=" + description
                + ", photoUrl=" + photoUrl + ", newproduct=" + newproduct + ", recommanded=" + recommanded + ", sizes=" + Arrays.toString(sizes) + ", size=" + size + ", colors=" + Arrays.toString(colors) + ", color=" + color + ", classification=" + classification + ", detail=" + detail + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + this.id;
        hash = 31 * hash + Objects.hashCode(this.size);
        hash = 31 * hash + Objects.hashCode(this.color);
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
        final Product other = (Product) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.size, other.size)) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        return true;
    }

 

}
