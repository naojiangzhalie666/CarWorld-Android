package com.liansheng.carworld.bean.me;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressBean implements Parcelable {
    /**
     * province : string
     * city : string
     * county : string
     * name : string
     * street : string
     * longitude : string
     * latitude : string
     */

    private String province;
    private String city;
    private String county;
    private String name;
    private String street;
    private String longitude;
    private String latitude;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress(){
        return this.province+this.city+this.county+this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.county);
        dest.writeString(this.name);
        dest.writeString(this.street);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
    }

    public void readFromParcel(Parcel source) {
        this.province = source.readString();
        this.city = source.readString();
        this.county = source.readString();
        this.name = source.readString();
        this.street = source.readString();
        this.longitude = source.readString();
        this.latitude = source.readString();
    }

    public AddressBean() {
    }

    public AddressBean(String name,String longitude, String latitude,String province, String city, String county,  String street) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.province = province;
        this.city = city;
        this.county = county;
        this.street = street;
    }

    protected AddressBean(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.county = in.readString();
        this.name = in.readString();
        this.street = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}