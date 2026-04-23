/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.service;

import layo.clo.entity.Order;
import layo.clo.entity.CLOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.Customer;
import layo.clo.entity.OrderItem;
import layo.clo.entity.PaymentType;
import layo.clo.entity.Product;
import layo.clo.entity.ShippingType;

/**
 *
 * @author Administrator
 */
public class OrdersDAO {

    private static final String UPDATE_PRODUCT_STOCK = "UPDATE stock SET stock = (stock-?) WHERE productid=? AND stock>=? AND color=? AND size=?";
    private static final String INSERT_ORDERS_SQL = "INSERT INTO orders "
            + "(id,customer_account,order_time,"
            + "payment_type,payment_fee,shipping_type,shipping_fee,"
            + "receiver_name,receiver_email,receiver_phone,receiver_address,status) "
            + "VALUES(0,?,?,?,?,?,?,?,?,?,?,0);";
    private static final String INSERT_ORDER_ITEMS_SQL = "INSERT INTO order_items"
            + "(order_id,product_id,price,quantity,color,size) VALUES(?,?,?,?,?,?)";

    public void insert(Order order) throws CLOException {
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt0 = connection.prepareStatement(UPDATE_PRODUCT_STOCK); //
                PreparedStatement pstmt1 = connection.prepareStatement(INSERT_ORDERS_SQL, Statement.RETURN_GENERATED_KEYS); //3.準備2個指令
                PreparedStatement pstmt2 = connection.prepareStatement(INSERT_ORDER_ITEMS_SQL);) {
            //關閉autoCommit
            connection.setAutoCommit(false);

            try {
                //先更改庫存
                ProductService pservice = new ProductService();

                for (OrderItem item : order.getOrderItemSet()) {
                    pstmt0.setInt(1, item.getQuantity());
                    pstmt0.setInt(2, item.getProduct().getId());
                    pstmt0.setInt(3, item.getQuantity());
                    pstmt0.setInt(4, pservice.getColorNo(item.getProduct().getColor()));       
                    pstmt0.setInt(5, pservice.getSizeNo(item.getProduct().getSize()));
                    int row = pstmt0.executeUpdate();
                    if (row < 1) {
                        throw new CLOException("產品庫存量不足",item.getProduct());
                    }
                }

                //pstmt1 3.1 傳入?的值
                pstmt1.setString(1, order.getCustomer().getAccount());
                pstmt1.setTimestamp(2, new java.sql.Timestamp(order.getOrderTime().getTime()));
                pstmt1.setString(3, order.getPaymentType().name());
                pstmt1.setDouble(4, order.getPaymentFee());
                pstmt1.setString(5, order.getShippingType().name());
                pstmt1.setDouble(6, order.getShippingFee());
                pstmt1.setString(7, order.getReceiverName());
                pstmt1.setString(8, order.getReceiverEmail());
                pstmt1.setString(9, order.getReceiverPhone());
                pstmt1.setString(10, order.getReceiverAddress());

                //pstmt1 4. 執行指令
                pstmt1.executeUpdate();

                //讀取 key: id 值
                try (ResultSet rs = pstmt1.getGeneratedKeys()) {
                    while (rs.next()) {
                        order.setId(rs.getInt(1));
                    }
                }

                for (OrderItem item : order.getOrderItemSet()) {
                    item.setOrderId(order.getId());

                    //pstmt2 3.1傳入?的值
                    pstmt2.setInt(1, item.getOrderId());
                    pstmt2.setInt(2, item.getProduct().getId());
                    pstmt2.setDouble(3, item.getPrice());
                    pstmt2.setInt(4, item.getQuantity());
                    pstmt2.setString(5, item.getProduct().getColor());
                    pstmt2.setString(6, item.getProduct().getSize());

                    //pstmt2 4. 執行指令
                    pstmt2.executeUpdate();
                }
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(OrdersDAO.class.getName()).log(Level.SEVERE, "新增訂單失敗", ex);
            throw new CLOException("新增訂單失敗", ex);
        }
    }

    private static final String SELECT_ORDERS_BY_CUSTOMER_ACCOUNT = "SELECT orders.id, orders.customer_account, order_time, "
            + "payment_type, payment_fee, payment_note,"
            + "shipping_type,shipping_fee, shipping_note,"
            + "receiver_name,receiver_email,receiver_phone,receiver_address,orders.status,"
            + "SUM(price*quantity) as total_amount "
            + "FROM orders "
            + "LEFT JOIN order_items  ON orders.id = order_items.order_id "
            + "WHERE customer_account=? GROUP BY orders.id;";

    /**
     * 給已登入的客戶查詢自己的歷史訂單
     *
     * @param customerId
     * @return
     * @throws VGBException
     */
    public List<Order> getOrdersByCustomerAccount(String customerAccount) throws CLOException {
        try (Connection connection = RDBConnection.getConnection(); //2.建立連線
                PreparedStatement pstmt = connection.prepareStatement(SELECT_ORDERS_BY_CUSTOMER_ACCOUNT) //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setString(1, customerAccount);

            //4. 執行指令
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Order> list = new ArrayList<>();
                while (rs.next()) { //5. 處理結果
                    Order order = new Order();
                    order.setId(rs.getInt("id"));

                    String cAccount = rs.getString("customer_account");
                    Customer c = new Customer();
                    c.setAccount(cAccount);
                    order.setCustomer(c);

                    order.setOrderTime(rs.getTimestamp("order_time"));

                    String pType = rs.getString("payment_type");
                    order.setPaymentType(PaymentType.valueOf(pType));
                    order.setPaymentFee(rs.getDouble("payment_fee"));
                    order.setPaymentNote(rs.getString("payment_note"));

                    String shType = rs.getString("shipping_type");
                    order.setShippingType(ShippingType.valueOf(shType));
                    order.setShippingFee(rs.getDouble("shipping_fee"));
                    order.setShippingNote(rs.getString("shipping_note"));

                    order.setReceiverName(rs.getString("receiver_name"));
                    order.setReceiverEmail(rs.getString("receiver_email"));
                    order.setReceiverPhone(rs.getString("receiver_phone"));
                    order.setReceiverAddress(rs.getString("receiver_address"));

                    order.setStatus(rs.getInt("status"));
                    order.setTotalAmount(rs.getDouble("total_amount"));

                    list.add(order);
                }
                return list;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdersDAO.class.getName()).log(Level.SEVERE, "查詢客戶歷史訂單失敗", ex);
            throw new CLOException("查詢客戶歷史訂單失敗", ex);
        }
    }

    private static final String SELECT_ORDER_BY_ID = "SELECT orders.id, orders.customer_account, order_time,"
            + "payment_type, payment_fee, payment_note,"
            + "shipping_type,shipping_fee, shipping_note,"
            + "receiver_name,receiver_email,receiver_phone,receiver_address,orders.status,"
            + "product_id,name,price,quantity,color,size,url "
            + "FROM orders "
            + "LEFT OUTER JOIN order_items ON orders.id = order_items.order_id "
            + "LEFT JOIN products ON order_items.product_id = products.id "
            + "WHERE orders.id=?";

    public Order get(int id) throws CLOException {
        try (Connection connection = RDBConnection.getConnection(); //2.建立連線
                PreparedStatement pstmt = connection.prepareStatement(SELECT_ORDER_BY_ID); //3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setInt(1, id);

            //4.執行指令
            try (ResultSet rs = pstmt.executeQuery()) {
                Order order = null;
                while (rs.next()) {
                    if (order == null) {
                        order = new Order();

                        //指派Order物件的屬性值
                        order.setId(id);
                        String cAccount = rs.getString("customer_account");
                        Customer c = new Customer();
                        c.setAccount(cAccount);
                        order.setCustomer(c);
                        order.setOrderTime(rs.getTimestamp("order_time"));

                        String pType = rs.getString("payment_type");
                        order.setPaymentType(PaymentType.valueOf(pType));
                        order.setPaymentFee(rs.getDouble("payment_fee"));
                        order.setPaymentNote(rs.getString("payment_note"));

                        String shType = rs.getString("shipping_type");
                        order.setShippingType(ShippingType.valueOf(shType));
                        order.setShippingFee(rs.getDouble("shipping_fee"));
                        order.setShippingNote(rs.getString("shipping_note"));

                        order.setReceiverName(rs.getString("receiver_name"));
                        order.setReceiverEmail(rs.getString("receiver_email"));
                        order.setReceiverPhone(rs.getString("receiver_phone"));
                        order.setReceiverAddress(rs.getString("receiver_address"));
                        order.setStatus(rs.getInt("status"));
                    }

                    if (rs.getString("quantity") != null) {
                        OrderItem item = new OrderItem();
                        item.setOrderId(id);
                        item.setPrice(rs.getDouble("price"));
                        item.setQuantity(rs.getInt("quantity"));
                        Product p = new Product();
                        p.setId(rs.getInt("product_id"));
                        p.setName(rs.getString("name"));
                        p.setColor(rs.getString("color"));
                        p.setSize(rs.getString("size"));
                        p.setPhotoUrl(rs.getString("url"));
                        item.setProduct(p);
                        order.add(item);
                    }
                }
                return order;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdersDAO.class.getName()).log(Level.SEVERE, "查詢訂單失敗:id-" + id, ex);
            throw new CLOException("查詢訂單失敗:id-" + id, ex);
        }
    }

    private static final String UPDATE_ORDER_NEW_TO_PAID = "UPDATE orders "
            + "SET status=1, payment_note=? "
            + "WHERE id=? AND customer_account=? AND status=0 AND payment_type='" + PaymentType.ATM.name() + "'";

    public void sendPaid(int id, String customerAccount, String paymentNote) throws CLOException {
        try (Connection connection = RDBConnection.getConnection();//2.建立連線
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_ORDER_NEW_TO_PAID);//3.準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setString(1, paymentNote);
            pstmt.setInt(2, id);
            pstmt.setString(3, customerAccount);

            //4. 執行指令
            int row = pstmt.executeUpdate();

            if (row == 0) {
                throw new CLOException("訂單狀態不正確!請重新查詢(訂單編號為:" + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdersDAO.class.getName()).log(Level.SEVERE, "訂單通知付款失敗:id-" + id, ex);
            throw new CLOException("訂單通知付款失敗:id-" + id, ex);
        }
    }
    
    
    
    
        private static final String UPDATE_ORDER_TO_AUTH_CONFIRM = "UPDATE orders "
            + "SET payment_note=?, status =2 "
            + "WHERE id=? AND status=0 AND payment_type='" + PaymentType.CARD.name() + "'";

    public void authConfirmCard(int orderId, String paymentNote) throws CLOException {
        try ( Connection connection = RDBConnection.getConnection();              //1. 建立connection                
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_ORDER_TO_AUTH_CONFIRM);) {//2.準備指令
            //3.1. 傳入?的值
            pstmt.setString(1, paymentNote);
            pstmt.setInt(2, orderId);

            //3.2. 執行
            int row = pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new CLOException("信用卡授權失敗", ex);
        }
    }

}
