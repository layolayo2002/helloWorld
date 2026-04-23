/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layo.clo.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class Customer {

    public static final char MALE = 'M'; //性別常數
    public static final char FEMALE = 'F';

    private String account;   //帳號 Pkey
    private String id; //ROC ID
    private String name;
    private String password;   //6~20字
    private char gender;   //'M'男生，'F'女生
    private String email;

    //以下為非必要
    private Date birthday;
    private String address;
    private String phone;
    private Boolean married = false;

    private String status;

    public Customer(String account, String id, String name, String password) throws CLOException {
        this.setAccount(account);
        this.setId(id);
        this.setName(name);
        this.setPassword(password);
    }

    public Customer(String account, String id, String name, String password,
            char gender, String email) throws CLOException {
        this(account, id, name, password);
        //自身類別的其他建構子
        this.setGender(gender);
        this.setEmail(email);

    }

    public Customer() {

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) throws CLOException {
        
        if(account != null && account.length()>=4 && account.length() <=16){
            this.account=account;
        }else{
            throw new CLOException("帳號輸入不正確");
        }
        
  
    }
   
    
 
    public String getId() {
        return id;
    }

    public void setId(String id) throws CLOException {
        if (this.checkID(id)) {
            this.id = id;
        } else {
            throw new CLOException("身分證號輸入不正確");
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws CLOException {
        if (name != null && (name = name.trim()).length() > 0) {
            this.name = name;
        } else {
            throw new CLOException("姓名輸入不正確");
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws CLOException {
        if (password != null && password.length() >= 6 && password.length() <= 20) {
            this.password = password;
        } else {
            throw new CLOException("密碼輸入不正確");
        }

    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) throws CLOException {
        if (gender == 'M' || gender == 'F') {
            this.gender = gender;
        } else {
            throw new CLOException("性別輸入不正確");
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws CLOException {
        if (email.matches("(([A-Za-z0-9]+\\.?)+([A-Za-z0-9]+_?)+)+[A-Za-z0-9]+@([a-zA-Z0-9]+\\.)+(com|edu|gov)(\\.(tw|ch|hk))?")) {
            this.email = email;
        } else {
            throw new CLOException("EMAIL輸入不正確");
        }
    }

    public Date getBirthday() {
        return birthday;
    }

     public String getBirthdayString(){ //2017/11/30 加入的新方法，為了將Date物件轉換為 web date string
        if(this.birthday!=null){
            return WEB_DATE_FORMAT.format(birthday);
        }else{
            return "";
        }
    }
    
    
    public void setBirthday(Date birthday) throws CLOException {
        if (birthday != null) {
            if (birthday.before(new Date())) {
                this.birthday = birthday;
            } else {
                throw new CLOException("生日不得大於今天");

            };
        } else {
            this.birthday = birthday;
        }

    }

    private static final DateFormat WEB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public void setBirthday(String date) throws CLOException {   //預設throw  RuntimeException.Error
        Date d = null;
        if (date != null && date.length() != 0) {
            date = date.replace('/', '-');
            try {
                d = WEB_DATE_FORMAT.parse(date);
                setBirthday(d);
            } catch (ParseException ex) {
                System.out.println("客戶生日日期格式錯誤!應為yyyy-MM-dd");
                throw new CLOException("客戶生日日期格式錯誤!應為yyyy-MM-dd");//拋出VGBException來通知前端
            }
        } else {
            setBirthday((Date) null);
        }
    }

    public void setBirthday(int year, int month, int day) {
        this.birthday = new GregorianCalendar(year, month - 1, day).getTime();
    }
//    public void setBirthday(String date){
//        
//    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = (address != null ? address : null);
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getMarried() {
        return married;
    }

    public void setMarried(Boolean married) {
        this.married = married;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private static final String ID_PATTERN = "[A-Z][12][0-9]{8}";
    private static final String firstCharString = "ABCDEFGHJKLMNPQRSTUVXYWZIO";

    public static boolean checkID(String id) {
        int response = 0;
        int total = 0;

        if (id != null && id.length() == 10 && id.matches(ID_PATTERN)) {
            //判斷字首
            if (id.charAt(0) > 64 && id.charAt(0) < 73) {
                response = id.charAt(0) - 55;
            } else if (id.charAt(0) == 73) {
                response = 34;
            } else if (id.charAt(0) > 73 && id.charAt(0) < 79) {
                response = id.charAt(0) - 56;
            } else if (id.charAt(0) == 79) {
                response = 35;
            } else if (id.charAt(0) > 79 && id.charAt(0) < 87) {
                response = id.charAt(0) - 57;
            } else if (id.charAt(0) == 87) {
                response = 32;
            } else if (id.charAt(0) > 87 && id.charAt(0) < 90) {
                response = id.charAt(0) - 58;
            } else if (id.charAt(0) == 90) {
                response = 33;
            } else {

                return false;
            }

            //身分證驗證數字
            total = response % 10 * 9 + response / 10;

            for (int i = 0; i < 8; i++) {
                total = total + ((id.charAt(i + 1)) - '0') * (8 - i);
            }
            total = total + id.charAt(9) - '0';

            if (total % 10 == 0) {

                return true;
            } else {

                return false;
            }

        } else {

            return false;
        }
    }

    public int getAge() {
        if (getBirthday() != null) {    //birthday為Date
            //1.取得今年
            Calendar calendar = Calendar.getInstance(); //取得現在的時間
            int year = calendar.get(Calendar.YEAR);    //現在時間的年分
            //2.取得客戶birthday年分
            calendar.setTime(getBirthday());    //將calendar翻到客戶出生的時間
            int birthYear = calendar.get(Calendar.YEAR);   //取得客戶的年分
            //3.計算年紀
            return year - birthYear - 1;

        } else {
            return 0;
        }

    }

    public String toString() {
        return "Customer{" + "account=" + account + "id=" + id + ", name=" + name + ", password=" + password + ", gender=" + gender + ", email=" + email + ", birthday=" + birthday + ", address=" + address + ", phone=" + phone + ", status=" + status + '}';
    }

//    @Override
//    public boolean equals(Object obj) {
//
//        if (this == obj) {
//            return true;
//        }
//        
//        if (obj instanceof Customer) {
//            if (this.id == ((Customer) obj).id) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
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
        final Customer other = (Customer) obj;
//        if (this.gender != other.gender) {
//            return false;
//        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
//        if (!Objects.equals(this.name, other.name)) {
//            return false;
//        }
        return true;
    }

    @Override
    public int hashCode() {
        int code = (id != null ? id.hashCode() : 0)
                + (name != null ? name.hashCode() : 0) + gender;

        return code;
    }//可做出型號.尺寸的功能

}
