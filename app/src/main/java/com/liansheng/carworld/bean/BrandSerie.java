package com.liansheng.carworld.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BrandSerie implements Parcelable {

    private String fctId;
    private String brand;
    private String fct;
    private String name;
    private String price;
    private String status;
    private String year;
    private String min_year;
    private String max_year;
    private double liter;
    private String liter_type;
    private String gear_type;
    private String discharge_standard;
    private boolean is_green;
    private String id;
    private String creation;

    public String getFctId() {
        return fctId;
    }

    public void setFctId(String fctId) {
        this.fctId = fctId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFct() {
        return fct;
    }

    public void setFct(String fct) {
        this.fct = fct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMin_year() {
        return min_year;
    }

    public void setMin_year(String min_year) {
        this.min_year = min_year;
    }

    public String getMax_year() {
        return max_year;
    }

    public void setMax_year(String max_year) {
        this.max_year = max_year;
    }

    public double getLiter() {
        return liter;
    }

    public void setLiter(double liter) {
        this.liter = liter;
    }

    public String getLiter_type() {
        return liter_type;
    }

    public void setLiter_type(String liter_type) {
        this.liter_type = liter_type;
    }

    public String getGear_type() {
        return gear_type;
    }

    public void setGear_type(String gear_type) {
        this.gear_type = gear_type;
    }

    public String getDischarge_standard() {
        return discharge_standard;
    }

    public void setDischarge_standard(String discharge_standard) {
        this.discharge_standard = discharge_standard;
    }

    public boolean isIs_green() {
        return is_green;
    }

    public void setIs_green(boolean is_green) {
        this.is_green = is_green;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fctId);
        dest.writeString(this.brand);
        dest.writeString(this.fct);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.status);
        dest.writeString(this.year);
        dest.writeString(this.min_year);
        dest.writeString(this.max_year);
        dest.writeDouble(this.liter);
        dest.writeString(this.liter_type);
        dest.writeString(this.gear_type);
        dest.writeString(this.discharge_standard);
        dest.writeByte(this.is_green ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.creation);
    }

    public void readFromParcel(Parcel source) {
        this.fctId = source.readString();
        this.brand = source.readString();
        this.fct = source.readString();
        this.name = source.readString();
        this.price = source.readString();
        this.status = source.readString();
        this.year = source.readString();
        this.min_year = source.readString();
        this.max_year = source.readString();
        this.liter = source.readDouble();
        this.liter_type = source.readString();
        this.gear_type = source.readString();
        this.discharge_standard = source.readString();
        this.is_green = source.readByte() != 0;
        this.id = source.readString();
        this.creation = source.readString();
    }

    public BrandSerie() {
    }

    protected BrandSerie(Parcel in) {
        this.fctId = in.readString();
        this.brand = in.readString();
        this.fct = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.status = in.readString();
        this.year = in.readString();
        this.min_year = in.readString();
        this.max_year = in.readString();
        this.liter = in.readDouble();
        this.liter_type = in.readString();
        this.gear_type = in.readString();
        this.discharge_standard = in.readString();
        this.is_green = in.readByte() != 0;
        this.id = in.readString();
        this.creation = in.readString();
    }

    public static final Parcelable.Creator<BrandSerie> CREATOR = new Parcelable.Creator<BrandSerie>() {
        @Override
        public BrandSerie createFromParcel(Parcel source) {
            return new BrandSerie(source);
        }

        @Override
        public BrandSerie[] newArray(int size) {
            return new BrandSerie[size];
        }
    };
}
