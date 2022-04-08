package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansheng.carworld.bean.UserInfo;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.IdentityCardBean;

public class FindCardBean {

    private String id;
    private String brand;
    private String brand_fct;
    private String brand_serie;
    private String appearance;
    private String interior;
    private String licenseLocation;
    private String pickCarDistance;
    private String mileage;
    private String area;
    private String expiration;
    private String minPrice;
    private String maxPrice;
    private String note;
    private String mobile;
    private String logo;
    private String year;
    private String creation;
    private boolean isInWater;
    private boolean isInFire;
    private boolean isAccident;
    private boolean isCompleted;
    private IdentityCardBean identityCard;
    private CompanyBean company;
    private UserInfo user;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public String getLicenseLocation() {
        return licenseLocation;
    }

    public void setLicenseLocation(String licenseLocation) {
        this.licenseLocation = licenseLocation;
    }

    public String getPickCarDistance() {
        return pickCarDistance;
    }

    public void setPickCarDistance(String pickCarDistance) {
        this.pickCarDistance = pickCarDistance;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
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

}
