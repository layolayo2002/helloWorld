/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.service;

import layo.clo.entity.CLOException;
import layo.clo.entity.Customer;

/**
 *
 * @author Administrator
 */

    public class CustomerService {

    private CustomersDAO dao = new CustomersDAO();

    public Customer login(String account, String pwd) throws CLOException {
        if (account == null || pwd == null) {
            throw new IllegalArgumentException("帳號或密碼不得為null!");
        }

        Customer c = dao.get(account);
        if (c != null) {
            if (pwd.equals(c.getPassword())) {
                return c;
            }
        }

        throw new CLOException("登入失敗，帳號或密碼不正確!");
    }

    public void register(Customer c) throws CLOException {
        dao.insert(c);
    }

    //以下為delegate method
    public Customer get(String account) throws CLOException {
        return dao.get(account);
    }

    public void update(Customer c) throws CLOException {
        dao.update(c);
    }
}
