package com.liansheng.carworld.bean.me;

public class ReqAuthBean {
    private String type;
    private String positive;
    private String back;
    private String owner;
    private String num;
    private String id;

    public ReqAuthBean(String type, String positive,String id) {
        this.type = type;
        this.positive = positive;
        this.id=id;
    }

    public ReqAuthBean(String type, String positive) {
        this.type = type;
        this.positive = positive;
    }

    public ReqAuthBean(String type, String positive, String back, String owner, String num) {
        this.type = type;
        this.positive = positive;
        this.back = back;
        this.owner = owner;
        this.num = num;
    }

    public ReqAuthBean(String type, String positive, String back, String owner, String num,String id) {
        this.type = type;
        this.positive = positive;
        this.back = back;
        this.owner = owner;
        this.num = num;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
