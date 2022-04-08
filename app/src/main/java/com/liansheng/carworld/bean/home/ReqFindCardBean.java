package com.liansheng.carworld.bean.home;

public class ReqFindCardBean {

    private String brand;
    private String brand_fct;
    private String brand_serie;
    private String appearance;
    private String interior;
    private String licenseLocation;
    private String pickCarDistance;
    private String area;
    private int minPrice;
    private int maxPrice;
    private String note;
    private String mileage;
    private String year;
    private String expirationDay;
    private boolean isInWater;
    private boolean isInFire;
    private boolean isAccident;
    private boolean isCompleted;
    private String userCompanyId;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(String userCompanyId) {
        this.userCompanyId = userCompanyId;
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

    public String getExpirationDay() {
        return expirationDay;
    }

    public void setExpirationDay(String expirationDay) {
        this.expirationDay = expirationDay;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
