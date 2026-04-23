package layo.clo.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import layo.clo.service.CustomersDAO;

public class Order {

    private int id;
    private Customer customer;
    private Set<OrderItem> orderItemSet = new HashSet<>();
    private Date orderTime = new Date();

    private PaymentType paymentType;
    private double paymentFee;
    private String paymentNote;

    private ShippingType shippingType;
    private double shippingFee;
    private String shippingNote;

    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private String receiverAddress;

    private int status = 0; //{0:新訂單,1:已通知, 2:已入帳,3:已出貨};

    public Order() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the orderTime
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * @param orderTime the orderTime to set
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * @return the paymentType
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the paymentFee
     */
    public double getPaymentFee() {
        return paymentFee;
    }

    /**
     * @param paymentFee the paymentFee to set
     */
    public void setPaymentFee(double paymentFee) {
        this.paymentFee = paymentFee;
    }

    /**
     * @return the paymentNote
     */
    public String getPaymentNote() {
        return paymentNote;
    }

    /**
     * @param paymentNote the paymentNote to set
     */
    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }

    /**
     * @return the shippingType
     */
    public ShippingType getShippingType() {
        return shippingType;
    }

    /**
     * @param shippingType the shippingType to set
     */
    public void setShippingType(ShippingType shippingType) {
        this.shippingType = shippingType;
    }

    /**
     * @return the shippingFee
     */
    public double getShippingFee() {
        return shippingFee;
    }

    /**
     * @param shippingFee the shippingFee to set
     */
    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * @return the shippingNote
     */
    public String getShippingNote() {
        return shippingNote;
    }

    /**
     * @param shippingNote the shippingNote to set
     */
    public void setShippingNote(String shippingNote) {
        this.shippingNote = shippingNote;
    }

    /**
     * @return the receiverName
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * @param receiverName the receiverName to set
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * @return the receiverEmail
     */
    public String getReceiverEmail() {
        return receiverEmail;
    }

    /**
     * @param receiverEmail the receiverEmail to set
     */
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    /**
     * @return the receiverPhone
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * @param receiverPhone the receiverPhone to set
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    /**
     * @return the receiverAddress
     */
    public String getReceiverAddress() {
        return receiverAddress;
    }

    /**
     * @param receiverAddress the receiverAddress to set
     */
    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    //orderItemSet's mutators: add
    public void add(OrderItem item) { //for DAO讀取資料表格時使用
        if (item != null) {
            orderItemSet.add(item);
        } else {
            System.out.println("訂單明細項目不得為null");
        }
    }

    public void add(ShoppingCart cart) throws CLOException {//for 結帳Servlet將購物車直接加入訂單
        for (Product p : cart.getProductSet()) {
            OrderItem item = new OrderItem();
           
            item.setProduct(p);
            if (customer instanceof VIP && !(p instanceof Outlet)) {
                double amount = p.getUnitPrice() * (100 - ((VIP) customer).getDiscount()) / 100;
                item.setPrice(amount);
            } else {
                item.setPrice(p.getUnitPrice());
            }
            item.setQuantity(cart.getQuantity(p));
            orderItemSet.add(item);
        }
    }

    //orderItemSet's accessor: 
    public Set<OrderItem> getOrderItemSet() {
        return new HashSet<>(this.orderItemSet); //回傳原來Set的複本
    }

    private double totalAmount;

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    //business method:
    public double getTotalAmount() {
        if (orderItemSet == null || orderItemSet.size() == 0) {
            return totalAmount;
        }

        double amount = 0;
        for (OrderItem item : orderItemSet) {
            amount = amount + item.getPrice() * item.getQuantity();
        }


        return amount;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
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
        final Order other = (Order) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public double getTotalAmountWithFee() {
        if (getTotalAmount() < 1000) {
            return getTotalAmount() + this.getPaymentFee() + this.getShippingFee();
        } else {
            return getTotalAmount();
        }
    }

    @Override
    public String toString() {
   

        return "Order{" + "編號=" + id + ", 訂貨人=" + customer
                + ",\n 明細=" + orderItemSet + ",\n 時間=" + orderTime
                + ",\n 付款方式=" + paymentType + ", 手續費=" + paymentFee + ", 備註=" + paymentNote
                + ",\n 貨運方式=" + shippingType + ", 手續費=" + shippingFee + ", 備註=" + shippingNote
                + ",\n 收件人=" + receiverName + ", Email=" + receiverEmail + ", 電話=" + receiverPhone + ", 地址" + receiverAddress
                + ",\n 狀態=" + status + ", 總金額=" + getTotalAmount() + '}';
    }

}
