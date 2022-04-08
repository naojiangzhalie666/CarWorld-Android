package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ReqSaleCarBean implements Parcelable {

    private String brand;
    private String brand_fct;
    private String brand_serie;
    private String registration_date;
    private String registration_city;
    private String volume;
    private String standard;
    private String location;
    private int mileage;
    private String color;
    private int guidance_price;
    private int sell_price;
    private int retail_price;
    private int transfer_times;
    private String note;
    private List<String> images;
    private String yearlyInspectionExpiration;
    private String compulsoryInsuranceExpiration;
    private String vin;
    private boolean isInWater;
    private boolean isInFire;
    private boolean isAccident;
    private boolean showMtnce;
    private boolean showInsurance;
    private boolean isCompleted;
    private String userCompanyId;
    private String car_verify_image;
    private boolean off;
    private String model;
    private int endurance;
    private String id;
    private String gearbox;
    private String negotiatedPrice;
    private int inside_price;

    public int getInside_price() {
        return inside_price;
    }

    public void setInside_price(int inside_price) {
        this.inside_price = inside_price;
    }

    public String getNegotiatedPrice() {
        return negotiatedPrice;
    }

    public void setNegotiatedPrice(String negotiatedPrice) {
        this.negotiatedPrice = negotiatedPrice;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(int retail_price) {
        this.retail_price = retail_price;
    }

    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isInWater() {
        return isInWater;
    }

    public void setInWater(boolean inWater) {
        isInWater = inWater;
    }

    public boolean isInFire() {
        return isInFire;
    }

    public void setInFire(boolean inFire) {
        isInFire = inFire;
    }

    public boolean isAccident() {
        return isAccident;
    }

    public void setAccident(boolean accident) {
        isAccident = accident;
    }

    public String getCar_verify_image() {
        return car_verify_image;
    }

    public void setCar_verify_image(String car_verify_image) {
        this.car_verify_image = car_verify_image;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public boolean isShowMtnce() {
        return showMtnce;
    }

    public void setShowMtnce(boolean showMtnce) {
        this.showMtnce = showMtnce;
    }

    public boolean isShowInsurance() {
        return showInsurance;
    }

    public void setShowInsurance(boolean showInsurance) {
        this.showInsurance = showInsurance;
    }

    public String getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    public String getYearlyInspectionExpiration() {
        return yearlyInspectionExpiration;
    }

    public void setYearlyInspectionExpiration(String yearlyInspectionExpiration) {
        this.yearlyInspectionExpiration = yearlyInspectionExpiration;
    }

    public String getCompulsoryInsuranceExpiration() {
        return compulsoryInsuranceExpiration;
    }

    public void setCompulsoryInsuranceExpiration(String compulsoryInsuranceExpiration) {
        this.compulsoryInsuranceExpiration = compulsoryInsuranceExpiration;
    }

    public String getRegistration_city() {
        return registration_city;
    }

    public void setRegistration_city(String registration_city) {
        this.registration_city = registration_city;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand_fct() {
        return brand_fct;
    }

    public void setBrand_fct(String brand_fct) {
        this.brand_fct = brand_fct;
    }

    public String getBrand_serie() {
        return brand_serie;
    }

    public void setBrand_serie(String brand_serie) {
        this.brand_serie = brand_serie;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getGuidance_price() {
        return guidance_price;
    }

    public void setGuidance_price(int guidance_price) {
        this.guidance_price = guidance_price;
    }

    public int getSell_price() {
        return sell_price;
    }

    public void setSell_price(int sell_price) {
        this.sell_price = sell_price;
    }

    public int getTransfer_times() {
        return transfer_times;
    }

    public void setTransfer_times(int transfer_times) {
        this.transfer_times = transfer_times;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brand);
        dest.writeString(this.brand_fct);
        dest.writeString(this.brand_serie);
        dest.writeString(this.registration_date);
        dest.writeString(this.registration_city);
        dest.writeString(this.volume);
        dest.writeString(this.standard);
        dest.writeString(this.location);
        dest.writeInt(this.mileage);
        dest.writeString(this.color);
        dest.writeInt(this.guidance_price);
        dest.writeInt(this.sell_price);
        dest.writeInt(this.retail_price);
        dest.writeInt(this.transfer_times);
        dest.writeString(this.note);
        dest.writeStringList(this.images);
        dest.writeString(this.yearlyInspectionExpiration);
        dest.writeString(this.compulsoryInsuranceExpiration);
        dest.writeString(this.vin);
        dest.writeByte(this.isInWater ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInFire ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAccident ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showMtnce ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showInsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.userCompanyId);
        dest.writeString(this.car_verify_image);
        dest.writeByte(this.off ? (byte) 1 : (byte) 0);
        dest.writeString(this.model);
        dest.writeInt(this.endurance);
        dest.writeString(this.gearbox);
        dest.writeString(this.negotiatedPrice);
        dest.writeInt(this.inside_price);
    }

    public void readFromParcel(Parcel source) {
        this.brand = source.readString();
        this.brand_fct = source.readString();
        this.brand_serie = source.readString();
        this.registration_date = source.readString();
        this.registration_city = source.readString();
        this.volume = source.readString();
        this.standard = source.readString();
        this.location = source.readString();
        this.mileage = source.readInt();
        this.color = source.readString();
        this.guidance_price = source.readInt();
        this.sell_price = source.readInt();
        this.retail_price = source.readInt();
        this.transfer_times = source.readInt();
        this.note = source.readString();
        this.images = source.createStringArrayList();
        this.yearlyInspectionExpiration = source.readString();
        this.compulsoryInsuranceExpiration = source.readString();
        this.vin = source.readString();
        this.isInWater = source.readByte() != 0;
        this.isInFire = source.readByte() != 0;
        this.isAccident = source.readByte() != 0;
        this.showMtnce = source.readByte() != 0;
        this.showInsurance = source.readByte() != 0;
        this.isCompleted = source.readByte() != 0;
        this.userCompanyId = source.readString();
        this.car_verify_image = source.readString();
        this.off = source.readByte() != 0;
        this.model = source.readString();
        this.endurance = source.readInt();
        this.gearbox = source.readString();
        this.negotiatedPrice = source.readString();
        this.inside_price = source.readInt();
    }

    public ReqSaleCarBean() {
    }

    protected ReqSaleCarBean(Parcel in) {
        this.brand = in.readString();
        this.brand_fct = in.readString();
        this.brand_serie = in.readString();
        this.registration_date = in.readString();
        this.registration_city = in.readString();
        this.volume = in.readString();
        this.standard = in.readString();
        this.location = in.readString();
        this.mileage = in.readInt();
        this.color = in.readString();
        this.guidance_price = in.readInt();
        this.sell_price = in.readInt();
        this.retail_price = in.readInt();
        this.transfer_times = in.readInt();
        this.note = in.readString();
        this.images = in.createStringArrayList();
        this.yearlyInspectionExpiration = in.readString();
        this.compulsoryInsuranceExpiration = in.readString();
        this.vin = in.readString();
        this.isInWater = in.readByte() != 0;
        this.isInFire = in.readByte() != 0;
        this.isAccident = in.readByte() != 0;
        this.showMtnce = in.readByte() != 0;
        this.showInsurance = in.readByte() != 0;
        this.isCompleted = in.readByte() != 0;
        this.userCompanyId = in.readString();
        this.car_verify_image = in.readString();
        this.off = in.readByte() != 0;
        this.model = in.readString();
        this.endurance = in.readInt();
        this.gearbox = in.readString();
        this.negotiatedPrice = in.readString();
        this.inside_price = in.readInt();
    }

    public static final Parcelable.Creator<ReqSaleCarBean> CREATOR = new Parcelable.Creator<ReqSaleCarBean>() {
        @Override
        public ReqSaleCarBean createFromParcel(Parcel source) {
            return new ReqSaleCarBean(source);
        }

        @Override
        public ReqSaleCarBean[] newArray(int size) {
            return new ReqSaleCarBean[size];
        }
    };
}
