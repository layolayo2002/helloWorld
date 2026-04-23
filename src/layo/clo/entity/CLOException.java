/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.entity;

/**
 *
 * @author Administrator
 */
public class CLOException extends Exception {

    /**
     * Creates a new instance of <code>CLOException</code> without detail
     * message.
     */
    public CLOException() {
    }

    /**
     * Constructs an instance of <code>CLOException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CLOException(String msg) {
        super(msg);
    }
    
    public CLOException(String msg,Product p) {
        super(String.valueOf(p.getId())+","+p.getColor()+","+p.getSize());
    }
    
    
     public CLOException(String message, Throwable cause) {
        super(message, cause);
    }
}
