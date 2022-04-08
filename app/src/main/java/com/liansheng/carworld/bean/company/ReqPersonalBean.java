package com.liansheng.carworld.bean.company;

public class ReqPersonalBean {
    private String id_card_positive;
    private String id_card_back;
    private String image_type;
    private String name;
    private String id_card_number;
    private String app;
    private String image;

    public String getId_card_positive() {
        return id_card_positive;
    }

    public void setId_card_positive(String id_card_positive) {
        this.id_card_positive = id_card_positive;
    }

    public String getId_card_back() {
        return id_card_back;
    }

    public void setId_card_back(String id_card_back) {
        this.id_card_back = id_card_back;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
