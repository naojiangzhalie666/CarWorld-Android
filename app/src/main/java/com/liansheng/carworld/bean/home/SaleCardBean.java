package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SaleCardBean implements Parcelable {

    private String id;
    private String userId;
    private String userCompanyId;
    private String passedDate;
    private boolean off;
    private boolean sold;
    private String rejectMessage;
    private String creation;
    private String brand;
    private String brand_fct;
    private String brand_serie;
    private String registration_date;
    private String registration_city;
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
    private int retail_price;
    private int transfer_times;
    private String note;
    private List<String> images;
    private String car_verify_image;
    private boolean isInWater;
    private boolean isInFire;
    private boolean isAccident;
    private boolean isCompleted;
    private boolean showMtnce;
    private boolean showInsurance;
    private boolean checked;
    private String model;
    private int endurance;
    private int visits;
    private String updateDate;
    private boolean newCar;
    private String negotiatedPrice;
    private boolean todayUpdated;
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

    public boolean isNewCar() {
        return newCar;
    }

    public void setNewCar(boolean newCar) {
        this.newCar = newCar;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
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

    public String getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    public String getPassedDate() {
        return passedDate;
    }

    public void setPassedDate(String passedDate) {
        this.passedDate = passedDate;
    }

    public boolean isOff() {
        return off;
    }

    public void setOff(boolean off) {
        this.off = off;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
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

    public int getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(int retail_price) {
        this.retail_price = retail_price;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public boolean isTodayUpdated() {
        return todayUpdated;
    }

    public void setTodayUpdated(boolean todayUpdated) {
        this.todayUpdated = todayUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.userCompanyId);
        dest.writeString(this.passedDate);
        dest.writeByte(this.off ? (byte) 1 : (byte) 0);
        dest.writeByte(this.sold ? (byte) 1 : (byte) 0);
        dest.writeString(this.rejectMessage);
        dest.writeString(this.creation);
        dest.writeString(this.brand);
        dest.writeString(this.brand_fct);
        dest.writeString(this.brand_serie);
        dest.writeString(this.registration_date);
        dest.writeString(this.registration_city);
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
        dest.writeInt(this.retail_price);
        dest.writeInt(this.transfer_times);
        dest.writeString(this.note);
        dest.writeStringList(this.images);
        dest.writeString(this.car_verify_image);
        dest.writeByte(this.isInWater ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInFire ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAccident ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCompleted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showMtnce ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showInsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.model);
        dest.writeInt(this.endurance);
        dest.writeInt(this.visits);
        dest.writeByte(this.newCar ? (byte) 1 : (byte) 0);
        dest.writeString(this.negotiatedPrice);
        dest.writeByte(this.todayUpdated ? (byte) 1 : (byte) 0);
        dest.writeInt(this.inside_price);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.userId = source.readString();
        this.userCompanyId = source.readString();
        this.passedDate = source.readString();
        this.off = source.readByte() != 0;
        this.sold = source.readByte() != 0;
        this.rejectMessage = source.readString();
        this.creation = source.readString();
        this.brand = source.readString();
        this.brand_fct = source.readString();
        this.brand_serie = source.readString();
        this.registration_date = source.readString();
        this.registration_city = source.readString();
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
        this.retail_price = source.readInt();
        this.transfer_times = source.readInt();
        this.note = source.readString();
        this.images = source.createStringArrayList();
        this.car_verify_image = source.readString();
        this.isInWater = source.readByte() != 0;
        this.isInFire = source.readByte() != 0;
        this.isAccident = source.readByte() != 0;
        this.isCompleted = source.readByte() != 0;
        this.showMtnce = source.readByte() != 0;
        this.showInsurance = source.readByte() != 0;
        this.checked = source.readByte() != 0;
        this.model = source.readString();
        this.endurance = source.readInt();
        this.visits = source.readInt();
        this.newCar = source.readByte() != 0;
        this.negotiatedPrice = source.readString();
        this.todayUpdated = source.readByte() != 0;
        this.inside_price = source.readInt();
    }

    public SaleCardBean() {
    }

    protected SaleCardBean(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.userCompanyId = in.readString();
        this.passedDate = in.readString();
        this.off = in.readByte() != 0;
        this.sold = in.readByte() != 0;
        this.rejectMessage = in.readString();
        this.creation = in.readString();
        this.brand = in.readString();
        this.brand_fct = in.readString();
        this.brand_serie = in.readString();
        this.registration_date = in.readString();
        this.registration_city = in.readString();
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
        this.retail_price = in.readInt();
        this.transfer_times = in.readInt();
        this.note = in.readString();
        this.images = in.createStringArrayList();
        this.car_verify_image = in.readString();
        this.isInWater = in.readByte() != 0;
        this.isInFire = in.readByte() != 0;
        this.isAccident = in.readByte() != 0;
        this.isCompleted = in.readByte() != 0;
        this.showMtnce = in.readByte() != 0;
        this.showInsurance = in.readByte() != 0;
        this.checked = in.readByte() != 0;
        this.model = in.readString();
        this.endurance = in.readInt();
        this.visits = in.readInt();
        this.newCar = in.readByte() != 0;
        this.negotiatedPrice = in.readString();
        this.todayUpdated = in.readByte() != 0;
        this.inside_price = in.readInt();
    }

    public static final Parcelable.Creator<SaleCardBean> CREATOR = new Parcelable.Creator<SaleCardBean>() {
        @Override
        public SaleCardBean createFromParcel(Parcel source) {
            return new SaleCardBean(source);
        }

        @Override
        public SaleCardBean[] newArray(int size) {
            return new SaleCardBean[size];
        }
    };
}
