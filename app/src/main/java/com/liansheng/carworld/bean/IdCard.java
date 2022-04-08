package com.liansheng.carworld.bean;

public class IdCard {

    /**
     * cardNumber : 21412412421
     * name : 张三
     * sex : 男
     * nation : 汉
     * birth : 1999-01-01
     * address : 地址
     */

    private String cardNumber;
    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
