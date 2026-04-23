/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.CLOException;
import layo.clo.entity.Customer;
import layo.clo.entity.Order;
import layo.clo.entity.Outlet;
import layo.clo.entity.Product;
import layo.clo.entity.VIP;

/**
 *
 * @author Administrator
 */
public class ProductsDAO {

    private final static String LAST_ONE_PRODUCT_ID = "select id from products order by id desc limit 1";

    public int getLastOneProductId() throws CLOException {
        try (Connection connection = RDBConnection.getConnection(); //1, 2. 取得Connection
                PreparedStatement pstmt = connection.prepareStatement(LAST_ONE_PRODUCT_ID);
                ResultSet rs = pstmt.executeQuery();) {
            int productid = 0;
            while (rs.next()) {
                productid = rs.getInt("id");
            }
            return productid;
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "取得最後一筆編號失敗", ex);
            throw new CLOException("取得最後一筆編號失敗", ex);
        }

    }

    private final static String INSERT_SQL = "INSERT INTO products (id,name,unit_price,stock,description"
            + ",url,newproduct,type,discount,recommanded,classification,detail) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    public void insert(Product p) throws CLOException {
        if (p == null) {
            throw new IllegalArgumentException("商品資料不得為null");
        }

        try (Connection connection = RDBConnection.getConnection(); //1, 2. 取得Connection
                PreparedStatement pstmt = connection.prepareStatement(INSERT_SQL); //3. 準備指令                
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, p.getId());//錯誤  須更正
            pstmt.setString(2, p.getName());
            pstmt.setDouble(3, (p instanceof Outlet) ? ((Outlet) p).getListPrice() : p.getUnitPrice());
            pstmt.setInt(4, 10);
            pstmt.setString(5, p.getDescription());
            pstmt.setString(6, p.getPhotoUrl());
            pstmt.setBoolean(7, p.isNewproduct());
            pstmt.setString(8, "Product");
            pstmt.setDouble(9, p instanceof Outlet ? ((Outlet) p).getDiscount() : 0);
            pstmt.setBoolean(10, false);
            pstmt.setString(11, p.getClassification().name());
            pstmt.setString(12, p.getDetail());

            //4. 執行指令
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "新增商品失敗", ex);
            throw new CLOException("新增商品失敗", ex);
        }
    }

    private static final String SELECT_PRODUCT_BY_ID_SQL = "SELECT id,name,unit_price,stock,description,"
            + "url,newproduct,type,discount,sizes,colors,recommanded,classification,detail "
            + "FROM products WHERE id = ?";

    public Product get(int id) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_PRODUCT_BY_ID_SQL); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, id);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {
                Product p = null;
                //5.處理resultset中的資料
                while (rs.next()) {
                    String type = rs.getString("type");
                    Boolean newone = rs.getBoolean("newproduct");

                    p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                    /*   if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                    p.setId(id);
                    p.setName(rs.getString("name"));
                    p.setUnitPrice(rs.getDouble("unit_price"));

                    p.setStock(rs.getInt("stock"));
                    p.setDescription(rs.getString("description"));
                    p.setPhotoUrl(rs.getString("url"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    p.setNewproduct(rs.getBoolean("newproduct"));
                    p.setRecommanded(rs.getBoolean("recommanded"));
                    p.setClassification(rs.getString("classification"));
                    p.setDetail(rs.getString("detail"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }

                    try {
                        if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                            p.setSizes(rs.getString("sizes").split(","));
                        }

                        if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                            p.setColors(rs.getString("colors").split(","));
                        }
                    } catch (NullPointerException ex) {
                        throw new CLOException("沒有尺寸或顏色");
                    }

                }
                return p;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "查詢商品失敗(id: " + id + ")", ex);
            throw new CLOException("查詢商品失敗(id: " + id + ")", ex);
        }
    }

    private static final String SELECT_RECOMMANDED_PRODUCTS_SQL = "SELECT id,name,unit_price,stock,description,"
            + "url,newproduct,type,discount,sizes,colors,recommanded,classification,detail  FROM products WHERE recommanded=true";

    public List<Product> getRecommandedProducts() throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_RECOMMANDED_PRODUCTS_SQL); //3.準備指令
                ResultSet rs = pstmt.executeQuery(); //4.執行指令
                ) {

            //5.處理ResultSet中的資料
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                Product p = null;
                String type = rs.getString("type");
                Boolean newone = rs.getBoolean("newproduct");

                p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setUnitPrice(rs.getDouble("unit_price"));

                p.setStock(rs.getInt("stock"));
                p.setDescription(rs.getString("description"));
                p.setPhotoUrl(rs.getString("url"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                p.setNewproduct(rs.getBoolean("newproduct"));
                p.setRecommanded(rs.getBoolean("recommanded"));
                p.setClassification(rs.getString("classification"));
                p.setDetail(rs.getString("detail"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                try {
                    if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                        p.setSizes(rs.getString("sizes").split(","));
                    }

                    if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                        p.setColors(rs.getString("colors").split(","));
                    }
                } catch (NullPointerException ex) {
                    throw new CLOException("沒有尺寸或顏色");
                }

                list.add(p);
            }
            return list;

        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢推薦商品失敗", ex);
            throw new CLOException("查詢推薦商品失敗", ex);
        }
    }

    private static final String SELECT_TOTAL_PRODUCTS_SQL = "SELECT id,name,unit_price,stock,description,"
            + "url,newproduct,type,discount,sizes,colors,recommanded,classification,detail  FROM products";

    public List<Product> getTotalProducts() throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_TOTAL_PRODUCTS_SQL); //3.準備指令
                ResultSet rs = pstmt.executeQuery(); //4.執行指令
                ) {

            //5.處理ResultSet中的資料
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                Product p = null;
                String type = rs.getString("type");
                Boolean newone = rs.getBoolean("newproduct");

                p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setUnitPrice(rs.getDouble("unit_price"));

                p.setStock(rs.getInt("stock"));
                p.setDescription(rs.getString("description"));
                p.setPhotoUrl(rs.getString("url"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                p.setNewproduct(rs.getBoolean("newproduct"));
                p.setRecommanded(rs.getBoolean("recommanded"));
                p.setClassification(rs.getString("classification"));
                p.setDetail(rs.getString("detail"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                try {
                    if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                        p.setSizes(rs.getString("sizes").split(","));
                    }

                    if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                        p.setColors(rs.getString("colors").split(","));
                    }
                } catch (NullPointerException ex) {
                    throw new CLOException("沒有尺寸或顏色");
                }

                list.add(p);
            }
            return list;

        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢全部商品失敗", ex);
            throw new CLOException("查詢全部商品失敗", ex);
        }
    }

    private static final String SELECT_NEW_PRODUCTS_SQL = "SELECT id,name,unit_price,stock,description,"
            + "url,newproduct,type,discount,sizes,colors,recommanded,classification,detail  FROM products WHERE newproduct=true";

    public List<Product> getNewProducts() throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_NEW_PRODUCTS_SQL); //3.準備指令
                ResultSet rs = pstmt.executeQuery(); //4.執行指令
                ) {

            //5.處理ResultSet中的資料
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                Product p = null;
                String type = rs.getString("type");
                Boolean newone = rs.getBoolean("newproduct");

                p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setUnitPrice(rs.getDouble("unit_price"));

                p.setStock(rs.getInt("stock"));
                p.setDescription(rs.getString("description"));
                p.setPhotoUrl(rs.getString("url"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                p.setNewproduct(rs.getBoolean("newproduct"));
                p.setRecommanded(rs.getBoolean("recommanded"));
                p.setClassification(rs.getString("classification"));
                p.setDetail(rs.getString("detail"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                try {
                    if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                        p.setSizes(rs.getString("sizes").split(","));
                    }

                    if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                        p.setColors(rs.getString("colors").split(","));
                    }
                } catch (NullPointerException ex) {
                    throw new CLOException("沒有尺寸或顏色");
                }

                list.add(p);
            }
            return list;

        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢新商品失敗", ex);
            throw new CLOException("查詢新商品失敗", ex);
        }
    }

    private static final String SELECT_PRODUCTS_BY_CLASSIFICATION_SQL = "SELECT id,name,unit_price,stock,description,"
            + " url,newproduct,type,discount,sizes,colors,recommanded,classification,detail "
            + "FROM products WHERE classification=?";

    public List<Product> getProductsByClassfication(String classification) throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_PRODUCTS_BY_CLASSIFICATION_SQL); //3.準備指令
                ) {

            pstmt.setString(1, classification);

            //5.處理ResultSet中的資料
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    Product p = null;
                    String type = rs.getString("type");
                    Boolean newone = rs.getBoolean("newproduct");

                    p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                    /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setUnitPrice(rs.getDouble("unit_price"));

                    p.setStock(rs.getInt("stock"));
                    p.setDescription(rs.getString("description"));
                    p.setPhotoUrl(rs.getString("url"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    p.setNewproduct(rs.getBoolean("newproduct"));
                    p.setRecommanded(rs.getBoolean("recommanded"));
                    p.setClassification(classification);
                    p.setDetail(rs.getString("detail"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    try {
                        if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                            p.setSizes(rs.getString("sizes").split(","));
                        }

                        if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                            p.setColors(rs.getString("colors").split(","));
                        }
                    } catch (NullPointerException ex) {
                        throw new CLOException("沒有尺寸或顏色");
                    }

                    list.add(p);
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢新商品失敗", ex);
            throw new CLOException("查詢新商品失敗", ex);
        }
    }

    private static final String SELECT_PRODUCTS_BY_DETAIL_SQL = "SELECT id,name,unit_price,stock,description,"
            + " url,newproduct,type,discount,sizes,colors,recommanded,classification,detail "
            + "FROM products WHERE detail=?";

    public List<Product> getProductsByDetail(String detail) throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_PRODUCTS_BY_DETAIL_SQL); //3.準備指令
                ) {

            pstmt.setString(1, detail);

            //5.處理ResultSet中的資料
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    Product p = null;
                    String type = rs.getString("type");
                    Boolean newone = rs.getBoolean("newproduct");

                    p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                    /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setUnitPrice(rs.getDouble("unit_price"));

                    p.setStock(rs.getInt("stock"));
                    p.setDescription(rs.getString("description"));
                    p.setPhotoUrl(rs.getString("url"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    p.setNewproduct(rs.getBoolean("newproduct"));
                    p.setRecommanded(rs.getBoolean("recommanded"));
                    p.setClassification(rs.getString("classification"));
                    p.setDetail(detail);
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    try {
                        if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                            p.setSizes(rs.getString("sizes").split(","));
                        }

                        if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                            p.setColors(rs.getString("colors").split(","));
                        }
                    } catch (NullPointerException ex) {
                        throw new CLOException("沒有尺寸或顏色");
                    }

                    list.add(p);
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢新商品失敗", ex);
            throw new CLOException("查詢新商品失敗", ex);
        }
    }

    private static final String SELECT_PRODUCT_BY_NAME_SQL = "SELECT id,name,unit_price,stock,description,"
            + "url,newproduct,type,discount,sizes,colors,recommanded,classification,detail FROM products "
            + "WHERE name LIKE ?";

    public List<Product> getProductsByName(String name) throws CLOException {
        //1, 2建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_PRODUCT_BY_NAME_SQL); //3.準備指令
                ) {
            //3.1傳入?的值
            pstmt.setString(1, '%' + name + '%');

            //4.執行指令
            try (ResultSet rs = pstmt.executeQuery()) {
                //5.處理ResultSet中的資料
                List<Product> list = new ArrayList<>();
                while (rs.next()) {
                    Product p = null;
                    String type = rs.getString("type");
                    p = (type.equals("Outlet") || type.equals("Book")) ? new Outlet() : new Product();
                    /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setUnitPrice(rs.getDouble("unit_price"));

                    p.setStock(rs.getInt("stock"));
                    p.setDescription(rs.getString("description"));
                    p.setPhotoUrl(rs.getString("url"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    p.setNewproduct(rs.getBoolean("newproduct"));
                    p.setRecommanded(rs.getBoolean("recommanded"));
                    p.setClassification(rs.getString("classification"));
                    p.setDetail(rs.getString("detail"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    try {
                        if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                            p.setSizes(rs.getString("sizes").split(","));
                        }

                        if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                            p.setColors(rs.getString("colors").split(","));
                        }
                    } catch (NullPointerException ex) {
                        throw new CLOException("沒有尺寸或顏色");
                    }

                    list.add(p);
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "依據名稱查詢商品失敗", ex);
            throw new CLOException("依據名稱查詢商品失敗: name-" + name, ex);
        }
    }

    private static final String SELECT_OUTLET_PRODUCTS_SQL = "SELECT id,name,unit_price,stock,description,"
            + "url,newproduct,type,discount,sizes,colors,recommanded,classification,detail  FROM products WHERE type='Outlet'";

    public List<Product> getOutletProducts() throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_OUTLET_PRODUCTS_SQL); //3.準備指令
                ResultSet rs = pstmt.executeQuery(); //4.執行指令
                ) {

            //5.處理ResultSet中的資料
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                Product p = null;
                String type = rs.getString("type");
                Boolean newone = rs.getBoolean("newproduct");

                p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                /*  if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setUnitPrice(rs.getDouble("unit_price"));

                p.setStock(rs.getInt("stock"));
                p.setDescription(rs.getString("description"));
                p.setPhotoUrl(rs.getString("url"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                p.setNewproduct(rs.getBoolean("newproduct"));
                p.setRecommanded(rs.getBoolean("recommanded"));
                p.setClassification(rs.getString("classification"));
                p.setDetail(rs.getString("detail"));
                if (p instanceof Outlet) {
                    ((Outlet) p).setDiscount(rs.getInt("discount"));
                }
                try {
                    if (rs.getString("sizes") != null && rs.getString("sizes").length() != 0) {
                        p.setSizes(rs.getString("sizes").split(","));
                    }

                    if (rs.getString("colors") != null && rs.getString("colors").length() != 0) {
                        p.setColors(rs.getString("colors").split(","));
                    }
                } catch (NullPointerException ex) {
                    throw new CLOException("沒有尺寸或顏色");
                }
                list.add(p);
            }
            return list;

        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢新商品失敗", ex);
            throw new CLOException("查詢新商品失敗", ex);
        }
    }

    private static final String SELECT_PRODUCTCOLORSIZE_BY_ID_SQL = "SELECT id,name,unit_price,products.stock,description, "
            + "url,newproduct,type,discount,recommanded,classification,detail,size,color,stock.stock "
            + "FROM products "
            + "LEFT JOIN stock ON productid=id " + "WHERE id = ?";

    public List<Product> getColorSize(int id) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(SELECT_PRODUCTCOLORSIZE_BY_ID_SQL); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, id);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {

                List<Product> plist = new ArrayList<>();
                //5.處理resultset中的資料
                while (rs.next()) {
                    Product p = new Product();
                    String type = rs.getString("type");
                    Boolean newone = rs.getBoolean("newproduct");
                    p = (type.equals("Outlet") || newone) ? new Outlet() : new Product();
                    /*   if (type.equals("Outlet") || type.equals("Book")) {
                        p = new Outlet();
                    } else {
                        p = new Product();
                    }*/
                    p.setId(id);
                    p.setName(rs.getString("name"));
                    p.setUnitPrice(rs.getDouble("unit_price"));

                    p.setDescription(rs.getString("description"));
                    p.setPhotoUrl(rs.getString("url"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    p.setNewproduct(rs.getBoolean("newproduct"));
                    p.setRecommanded(rs.getBoolean("recommanded"));
                    p.setClassification(rs.getString("classification"));
                    p.setDetail(rs.getString("detail"));
                    if (p instanceof Outlet) {
                        ((Outlet) p).setDiscount(rs.getInt("discount"));
                    }
                    p.setSize(getSize(rs.getInt("size")));
                    p.setColor(getColor(rs.getInt("color")));
                    p.setStock(rs.getInt("stock.stock"));
                    plist.add(p);
                }
                return plist;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "查詢商品顏色尺寸失敗(id: " + id + ")", ex);
            throw new CLOException("查詢商品顏色尺寸失敗(id: " + id + ")", ex);
        }
    }
    private static final String TOTAL_COLOR = "SELECT color FROM color";

    public List<String> getTotalColor() throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(TOTAL_COLOR); //3.準備指令
                ResultSet rs = pstmt.executeQuery(); //4.執行指令
                ) {

            //5.處理ResultSet中的資料
            List<String> colorList = new ArrayList<>();
            while (rs.next()) {
                colorList.add(rs.getString("color"));
            }
            return colorList;

        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢顏色失敗", ex);
            throw new CLOException("查詢顏色失敗", ex);
        }
    }

    private static final String FIND_COLOR = "SELECT idcolor,color FROM color WHERE idcolor=?";

    public String getColor(int idcolor) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(FIND_COLOR); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, idcolor);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {
                String color = null;

                //5.處理resultset中的資料
                while (rs.next()) {
                    //   String color=null;
                    color = rs.getString("color");
                }
                return color;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "查詢商品顏色名稱失敗", ex);
            throw new CLOException("查詢商品顏色名稱失敗", ex);
        }
    }
    private static final String FIND_COLORNO = "SELECT idcolor,color FROM color WHERE color=?";

    public int getColorNo(String color) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(FIND_COLORNO); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setString(1, color);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {
                int idcolor = 0;

                //5.處理resultset中的資料
                while (rs.next()) {
                    //   String color=null;
                    idcolor = rs.getInt("idcolor");
                }
                return idcolor;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "查詢商品顏色ID失敗", ex);
            throw new CLOException("查詢商品顏色ID失敗", ex);
        }
    }

    private static final String TOTAL_SIZE = "SELECT size FROM size";

    public List<String> getTotalSize() throws CLOException {
        //1,2. 取得連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(TOTAL_SIZE); //3.準備指令
                ResultSet rs = pstmt.executeQuery(); //4.執行指令
                ) {

            //5.處理ResultSet中的資料
            List<String> sizeList = new ArrayList<>();
            while (rs.next()) {
                sizeList.add(rs.getString("size"));
            }
            return sizeList;

        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE, "查詢尺寸失敗", ex);
            throw new CLOException("查詢尺寸失敗", ex);
        }
    }

    private static final String FIND_SIZENO = "SELECT idsize,size FROM size WHERE size=?";

    public int getSizeNo(String size) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(FIND_SIZENO); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setString(1, size);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {
                int idsize = 0;

                //5.處理resultset中的資料
                while (rs.next()) {
                    //   String color=null;
                    idsize = rs.getInt("idsize");
                }
                return idsize;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "查詢商品尺寸ID失敗", ex);
            throw new CLOException("查詢商品尺寸ID失敗", ex);
        }
    }

    private static final String FIND_SIZE = "SELECT idsize,size FROM size WHERE idsize=?";

    public String getSize(int idsize) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(FIND_SIZE); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, idsize);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {
                String size = null;

                //5.處理resultset中的資料
                while (rs.next()) {
                    //   String color=null;
                    size = rs.getString("size");
                }
                return size;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "查詢商品尺寸名稱失敗", ex);
            throw new CLOException("查詢商品尺寸名稱失敗", ex);
        }
    }

    private static final String FIND_STOCK_BY_COLOR_SIZE = "SELECT productid,idcolor,color.color,idsize,size.size,stock "
            + "FROM stock "
            + "LEFT JOIN color ON idcolor=stock.color "
            + "LEFT JOIN size ON idsize=stock.size "
            + "WHERE productid=?  AND color.color=?  AND size.size=? ";

    public int findStockByIdColorSize(int id, String color, String size) throws CLOException {
        //1.2 建立連結
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(FIND_STOCK_BY_COLOR_SIZE); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, id);
            pstmt.setString(2, color);
            pstmt.setString(3, size);
            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery();) {
                int stock = 0;

                //5.處理resultset中的資料
                while (rs.next()) {
                    stock = rs.getInt("stock");
                }
                return stock;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsDAO.class.getName()).log(Level.SEVERE,
                    "沒有庫存資料", ex);
            throw new CLOException("沒有庫存資料", ex);
        }
    }
    private static final String ADD_COLOR = "INSERT INTO color (color) VALUES (?)";

    public void addColor(String color) throws CLOException {
        if (color == null) {
            throw new IllegalArgumentException("顏色資料不得為空");
        }

        try (Connection connection = RDBConnection.getConnection(); //1, 2. 取得Connection
                PreparedStatement pstmt = connection.prepareStatement(ADD_COLOR); //3. 準備指令                
                ) {
            //3.1 傳入?的值
            List<String> totalColor = getTotalColor();
            for (String colors : totalColor) {
                if (color.equals(colors)) {
                    throw new CLOException("已有該顏色");
                }
            }

            pstmt.setString(1, color);

            //4. 執行指令
            pstmt.executeUpdate();

        } catch (CLOException ex) {
            throw new CLOException("" + ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "新增顏色失敗", ex);
            throw new CLOException("新增顏色失敗", ex);
        }
    }

    private static final String UPDATE_PRODUCT_STOCK = "UPDATE stock SET stock = ? WHERE productid=?  AND color=? AND size=?";

    public void updateStock(Product p) throws CLOException {
        if (p == null) {
            throw new IllegalArgumentException("修改庫存失敗，商品物件不得為null");
        }

        //1,2. 取得Connection
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_PRODUCT_STOCK); //3.準備指令
                ) {
            //3.1傳入?的值
            pstmt.setInt(1, p.getStock());
            pstmt.setInt(2, p.getId());
            pstmt.setInt(3, Integer.parseInt(p.getColor()));
            pstmt.setInt(4, Integer.parseInt(p.getSize()));

            //4.執行指令
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE,
                    "修改庫存失敗! id-" + p.getId(), ex);
            throw new CLOException("修改庫存失敗!", ex);
        }
    }

    private static final String INSERT_STOCK = "INSERT INTO stock (productid,color,size,stock) values (?,?,?,?)";

    public void insertStock(Product p) throws CLOException {

        if (p == null) {
            throw new CLOException("傳入p不得為null");
        }
        try (Connection connection = RDBConnection.getConnection(); //1, 2. 取得Connection
                PreparedStatement pstmt = connection.prepareStatement(INSERT_STOCK); //3. 準備指令                
                ) {
            //3.1 傳入?的值

            pstmt.setInt(1, p.getId());
            pstmt.setInt(2, Integer.parseInt(p.getColor()));
            pstmt.setInt(3, Integer.parseInt(p.getSize()));
            pstmt.setInt(4, p.getStock());
            //4. 執行指令
            pstmt.executeUpdate();

        } catch (CLOException ex) {
            throw new CLOException("" + ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "新增庫存失敗", ex);
            throw new CLOException("新增庫存失敗", ex);
        }

    }

    private static final String UPDATE_PRODUCT = "UPDATE products "
            + "SET name=?, unit_price=?, description=?, url=?, "
            + "type=?,discount=?,recommanded=? "
            + "WHERE id=?";

    public void update(Product p) throws CLOException {

        if (p == null) {
            throw new CLOException("傳入p不得為null");
        }
        try (Connection connection = RDBConnection.getConnection(); //1, 2. 取得Connection
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_PRODUCT); //3. 準備指令                
                ) {
            //3.1 傳入?的值

            pstmt.setString(1, p.getName());
            pstmt.setDouble(2,(p instanceof Outlet) ?((Outlet)p).getListPrice(): p.getUnitPrice());
            pstmt.setString(3, p.getDescription());
            pstmt.setString(4, p.getPhotoUrl());
            pstmt.setString(5, (p instanceof Outlet) ? "Outlet" : "Product");
            pstmt.setInt(6, (p instanceof Outlet) ? ((Outlet) p).getDiscount() : 0);
            pstmt.setBoolean(7, p.isRecommanded());
            pstmt.setInt(8, p.getId());
            //4. 執行指令
            pstmt.executeUpdate();

        } catch (CLOException ex) {
            throw new CLOException("" + ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "更新商品", ex);
            throw new CLOException("更新商品失敗", ex);
        }

    }

}
