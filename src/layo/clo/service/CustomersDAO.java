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
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.entity.CLOException;
import layo.clo.entity.Customer;
import layo.clo.entity.VIP;

/**
 *
 * @author Administrator
 */
public class CustomersDAO {

    private final static String INSERT_SQL = "INSERT INTO customers"
            + "(account,id, name, password, gender, email,birthday,"
            + "address,phone,married,status,type,discount) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public void insert(Customer c) throws CLOException {
        if (c == null) {
            throw new IllegalArgumentException("註冊客戶不得為null");
        }

        try (Connection connection = RDBConnection.getConnection(); //1, 2. 取得Connection
                PreparedStatement pstmt = connection.prepareStatement(INSERT_SQL); //3. 準備指令                
                ) {
            //3.1 傳入?的值
            pstmt.setString(1, c.getAccount());
            pstmt.setString(2, c.getId());
            pstmt.setString(3, c.getName());
            pstmt.setString(4, c.getPassword());
            pstmt.setString(5, String.valueOf(c.getGender()));
            pstmt.setString(6, c.getEmail());

            if (c.getBirthday() != null) {
                pstmt.setDate(7, new java.sql.Date(c.getBirthday().getTime()));
            } else {
                pstmt.setDate(7, null);
            }
            pstmt.setString(8, c.getAddress());
            pstmt.setString(9, c.getPhone());
            pstmt.setBoolean(10, c.getMarried());

            pstmt.setString(11, c.getStatus());
            pstmt.setString(12, c.getType());
            if (c instanceof VIP) {
                pstmt.setInt(13, ((VIP) c).getDiscount());
            } else {
                pstmt.setInt(13, 0);
            }

            //4. 執行指令
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "註冊客戶失敗", ex);
            if (ex.getErrorCode() == 1062) {
                throw new CLOException("帳號已經重覆註冊", ex);
            }
            throw new CLOException("註冊客戶失敗", ex);
        }
    }

    private final static String SELECT_CUSTOMER_BY_ACCOUNT_SQL = "SELECT account, id, name, password,gender,email,birthday,"
            + "address,phone,married, status,type,discount FROM customers WHERE account=?";

    public Customer get(String account) throws CLOException {
        try (Connection connection = RDBConnection.getConnection(); //1, 2 取得連線
                PreparedStatement pstmt = connection.prepareStatement(SELECT_CUSTOMER_BY_ACCOUNT_SQL); //3. 準備指令
                ) {
            //3.1 傳入?的值
            pstmt.setString(1, account);

            //4.執行指令
            try (
                    ResultSet rs = pstmt.executeQuery();) {
                //5. 處理ResultSet
                Customer c = null;
                while (rs.next()) {
                    String type = rs.getString("type");
                    if (type != null && type.equals("VIP")) {
                        c = new VIP();
                    } else {
                        c = new Customer();
                    }
                    c.setAccount(rs.getString("account"));
                    c.setId(rs.getString("id"));
                    c.setName(rs.getString("name"));
                    c.setPassword(rs.getString("password"));
                    c.setGender(rs.getString("gender").charAt(0));
                    c.setEmail(rs.getString("email"));

                    c.setBirthday(rs.getDate("birthday"));
                    c.setAddress(rs.getString("address"));
                    c.setPhone(rs.getString("phone"));
                    c.setMarried(rs.getBoolean("married"));
                    c.setStatus(rs.getString("status"));

                    if (c instanceof VIP) {
                        ((VIP) c).setDiscount(rs.getInt("discount"));
                    }
                }
                if (c == null ||c.getAccount()==null ||c.getAccount().length()==0) {
                    throw new CLOException();
                }
                return c;
            }
        } catch (CLOException ex) {
            throw new CLOException("查詢客戶失敗!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE, "查詢客戶失敗! id-" + account, ex);
            throw new CLOException("查詢客戶失敗!", ex);
        }
    }

    private static final String UPDATE_SQL = "UPDATE customers "
            + "SET name=?,password=?,gender=?,email=?,birthday=?,"
            + "address=?,phone=?,married=?,type=?,discount=?,status=? "
            + "WHERE account=?";

    public void update(Customer c) throws CLOException {
        if (c == null) {
            throw new IllegalArgumentException("修改客戶失敗，客戶物件不得為null");
        }

        //1,2. 取得Connection
        try (Connection connection = RDBConnection.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(UPDATE_SQL); //3.準備指令
                ) {
            //3.1傳入?的值
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getPassword());
            pstmt.setString(3, String.valueOf(c.getGender()));
            pstmt.setString(4, c.getEmail());

            if (c.getBirthday() != null) {
                pstmt.setDate(5, new java.sql.Date(c.getBirthday().getTime()));
            } else {
                pstmt.setDate(5, null);
            }
            pstmt.setString(6, c.getAddress());
            pstmt.setString(7, c.getPhone());
            pstmt.setBoolean(8, c.getMarried());

            pstmt.setString(9, c.getType());

            if (c instanceof VIP) {
                pstmt.setInt(10, ((VIP) c).getDiscount());
            } else {
                pstmt.setInt(10, 0);
            }
            pstmt.setString(11,c.getStatus());
            pstmt.setString(12, c.getAccount());
            //4.執行指令
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CustomersDAO.class.getName()).log(Level.SEVERE,
                    "修改客戶失敗! id-" + c.getId(), ex);
            throw new CLOException("修改客戶失敗!", ex);
        }
    }

}
