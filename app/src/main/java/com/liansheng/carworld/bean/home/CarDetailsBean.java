package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.IdentityCardBean;

import java.util.List;

public class CarDetailsBean implements Parcelable {

    private String id;
    private String userId;
    private IdentityCardBean identityCard;
    private CompanyBean company;
    private String brand;
    private String brand_fct;
    private String brand_serie;
    private String registration_date;
    private String registration_city;
    private String production_date;
    private String yearlyInspectionExpiration;
    private String compulsoryInsuranceExpiration;
    private String volume;
    private String standard;
    private String location;
    private int mileage;
    private String color;
    private String vin;
    private int guidance_price;
    private int sell_price;
    private int transfer_times;
    private String note;
    private List<String> images;
    private String car_verify_image;
    private boolean showMtnce;
    private boolean showInsurance;
    private boolean isInWater;
    private boolean isInFire;
    private boolean isAccident;
    private boolean isCompleted;
    private boolean collected;
    private String model;
    private int endurance;
    private boolean readOnly;
    private int retail_price;
    private String updateDate;
    private String number;
    private String negotiatedPrice;
    private String gearbox;
    private int inside_price;
    private UserInfo user;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public IdentityCardBean getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(IdentityCardBean identityCard) {
        this.identityCard = identityCard;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
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

    public String getRegistration_city() {
        return registration_city;
    }

    public void setRegistration_city(String registration_city) {
        this.registration_city = registration_city;
    }

    public String getProduction_date() {
        return production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
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

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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

    public String getCar_verify_image() {
        return car_verify_image;
    }

    public void setCar_verify_image(String car_verify_image) {
        this.car_verify_image = car_verify_image;
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
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

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(int retail_price) {
        this.retail_price = retail_price;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public int getInside_price() {
        return inside_price;
    }

    public void setInside_price(int inside_price) {
        this.inside_price = inside_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeParcelable(this.identityCard, flags);
        dest.writeParcelable(this.company, flags);
        dest.writeString(this.brand);
        dest.writeString(this.brand_fct);
        dest.writeString(this.brand_serie);
        dest.writeString(this.registration_date);
        dest.writeString(this.registration_city);
        dest.writeString(this.production_date);
        dest.writeString(this.yearlyInspectionExpiration);
        dest.writeString(this.compulsoryInsuranceExpiration);
        dest.writeString(this.volume);
        dest.writeString(this.standard);
        dest.writeString(this.location);
        dest.writeInt(this.mileage);
        dest.writeString(this.color);
        dest.writeString(this.vin);
        dest.writeInt(this.guidance_price);
        dest.writeInt(this.sell_price);
        dest.writeInt(this.transfer_times);
        dest.writeString(this.note);
        dest.writeStringList(this.images);
        dest.writeString(this.car_verify_image);
        dest.writeByte(this.showMtnce ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showInsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInWater ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInFire ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAccident ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCompleted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.collected ? (byte) 1 : (byte) 0);
        dest.writeString(this.model);
        dest.writeInt(this.endurance);
        dest.writeByte(this.readOnly ? (byte) 1 : (byte) 0);
        dest.writeInt(this.retail_price);
        dest.writeString(this.updateDate);
        dest.writeString(this.number);
        dest.writeString(this.negotiatedPrice);
        dest.writeString(this.gearbox);
        dest.writeInt(this.inside_price);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.userId = source.readString();
        this.identityCard = source.readParcelable(IdentityCardBean.class.getClassLoader());
        this.company = source.readParcelable(CompanyBean.class.getClassLoader());
        this.brand = source.readString();
        this.brand_fct = source.readString();
        this.brand_serie = source.readString();
        this.registration_date = source.readString();
        this.registration_city = source.readString();
        this.production_date = source.readString();
        this.yearlyInspectionExpiration = source.readString();
        this.compulsoryInsuranceExpiration = source.readString();
        this.volume = source.readString();
        this.standard = source.readString();
        this.location = source.readString();
        this.mileage = source.readInt();
        this.color = source.readString();
        this.vin = source.readString();
        this.guidance_price = source.readInt();
        this.sell_price = source.readInt();
        this.transfer_times = source.readInt();
        this.note = source.readString();
        this.images = source.createStringArrayList();
        this.car_verify_image = source.readString();
        this.showMtnce = source.readByte() != 0;
        this.showInsurance = source.readByte() != 0;
        this.isInWater = source.readByte() != 0;
        this.isInFire = source.readByte() != 0;
        this.isAccident = source.readByte() != 0;
        this.isCompleted = source.readByte() != 0;
        this.collected = source.readByte() != 0;
        this.model = source.readString();
        this.endurance = source.readInt();
        this.readOnly = source.readByte() != 0;
        this.retail_price = source.readInt();
        this.updateDate = source.readString();
        this.number = source.readString();
        this.negotiatedPrice = source.readString();
        this.gearbox = source.readString();
        this.inside_price = source.readInt();
    }

    public CarDetailsBean() {
    }

    protected CarDetailsBean(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.identityCard = in.readParcelable(IdentityCardBean.class.getClassLoader());
        this.company = in.readParcelable(CompanyBean.class.getClassLoader());
        this.brand = in.readString();
        this.brand_fct = in.readString();
        this.brand_serie = in.readString();
        this.registration_date = in.readString();
        this.registration_city = in.readString();
        this.production_date = in.readString();
        this.yearlyInspectionExpiration = in.readString();
        this.compulsoryInsuranceExpiration = in.readString();
        this.volume = in.readString();
        this.standard = in.readString();
        this.location = in.readString();
        this.mileage = in.readInt();
        this.color = in.readString();
        this.vin = in.readString();
        this.guidance_price = in.readInt();
        this.sell_price = in.readInt();
        this.transfer_times = in.readInt();
        this.note = in.readString();
        this.images = in.createStringArrayList();
        this.car_verify_image = in.readString();
        this.showMtnce = in.readByte() != 0;
        this.showInsurance = in.readByte() != 0;
        this.isInWater = in.readByte() != 0;
        this.isInFire = in.readByte() != 0;
        this.isAccident = in.readByte() != 0;
        this.isCompleted = in.readByte() != 0;
        this.collected = in.readByte() != 0;
        this.model = in.readString();
        this.endurance = in.readInt();
        this.readOnly = in.readByte() != 0;
        this.retail_price = in.readInt();
        this.updateDate = in.readString();
        this.number = in.readString();
        this.negotiatedPrice = in.readString();
        this.gearbox = in.readString();
        this.inside_price = in.readInt();
    }

    public static final Parcelable.Creator<CarDetailsBean> CREATOR = new Parcelable.Creator<CarDetailsBean>() {
        @Override
        public CarDetailsBean createFromParcel(Parcel source) {
            return new CarDetailsBean(source);
        }

        @Override
        public CarDetailsBean[] newArray(int size) {
            return new CarDetailsBean[size];
        }
    };
}
