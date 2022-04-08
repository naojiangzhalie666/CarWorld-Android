package com.liansheng.carworld.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.liansheng.carworld.bean.me.AddressBean;
import com.liansheng.carworld.bean.me.CompanyBean;
import com.liansheng.carworld.bean.me.LicensesBean;

import java.util.List;

public class UserInfo {

    /**
     * id : 0
     * userType : consumer
     * headphoto : string
     * mobile : string
     * address : {"province":"string","city":"string","county":"string","name":"string","street":"string","longitude":"string","latitude":"string"}
     * licenses : [{"id":0,"owner":"string","num":"string","positive":"string","back":"string","side":"string","status":"verifying","note":"string","type":"IdentityCard","expiration":"2021-04-14T09:04:15.385Z"}]
     * vip : {"level":"ordinary","beginning":"2021-04-14T09:04:15.385Z","expiration":"2021-04-14T09:04:15.385Z"}
     * pendingOrderCounts : 0
     * processingOrderCounts : 0
     * completedOrderCounts : 0
     * receivedOrderCounts : 0
     * balance : 0
     * score : 0
     * points : 0
     * invitationLink : string
     * authStatus : 0
     * creation : 2021-04-14T09:04:15.385Z
     */
    private String id;
    private String headphoto;
    private String mobile;
    private String name;
    private AddressBean address;
    //身份证、营业执照、货物保险、商业保险、强制保险、驾驶执照
    // IdentityCard, BusinessLicense, CargoInsurance, CommercialInsurance, CompulsoryInsurance, DrivingLicense
    private List<LicensesBean> licenses;
    private VipBean vip;
    private String pendingOrderCounts;
    private String processingOrderCounts;
    private String completedOrderCounts;
    private String receivedOrderCounts;
    private String balance;
    private String score;
    private String points;
    private String token;
    private String invitationLink;
    private String creation;
    private String directoryCollectionCounts;
    private String carResourceCollectionCounts;
    private String totalCollectionCounts;
    private List<CompanyBean> companys;
    private boolean hasPassword;
    private UserCarBean userCar;
    private List<String> types;
    private String lastLoginDate;

    public String getCarResourceCollectionCounts() {
        return carResourceCollectionCounts;
    }

    public void setCarResourceCollectionCounts(String carResourceCollectionCounts) {
        this.carResourceCollectionCounts = carResourceCollectionCounts;
    }

    public String getTotalCollectionCounts() {
        return totalCollectionCounts;
    }

    public void setTotalCollectionCounts(String totalCollectionCounts) {
        this.totalCollectionCounts = totalCollectionCounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserCarBean getUserCar() {
        return userCar;
    }

    public void setUserCar(UserCarBean userCar) {
        this.userCar = userCar;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getDirectoryCollectionCounts() {
        return directoryCollectionCounts;
    }

    public void setDirectoryCollectionCounts(String directoryCollectionCounts) {
        this.directoryCollectionCounts = directoryCollectionCounts;
    }

    public List<CompanyBean> getCompanys() {
        return companys;
    }

    public void setCompanys(List<CompanyBean> companys) {
        this.companys = companys;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public List<LicensesBean> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<LicensesBean> licenses) {
        this.licenses = licenses;
    }

    public VipBean getVip() {
        return vip;
    }

    public void setVip(VipBean vip) {
        this.vip = vip;
    }

    public String getPendingOrderCounts() {
        return pendingOrderCounts;
    }

    public void setPendingOrderCounts(String pendingOrderCounts) {
        this.pendingOrderCounts = pendingOrderCounts;
    }

    public String getProcessingOrderCounts() {
        return processingOrderCounts;
    }

    public void setProcessingOrderCounts(String processingOrderCounts) {
        this.processingOrderCounts = processingOrderCounts;
    }

    public String getCompletedOrderCounts() {
        return completedOrderCounts;
    }

    public void setCompletedOrderCounts(String completedOrderCounts) {
        this.completedOrderCounts = completedOrderCounts;
    }

    public String getReceivedOrderCounts() {
        return receivedOrderCounts;
    }

    public void setReceivedOrderCounts(String receivedOrderCounts) {
        this.receivedOrderCounts = receivedOrderCounts;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getInvitationLink() {
        return invitationLink;
    }

    public void setInvitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public static class VipBean {
        /**
         * level : ordinary
         * beginning : 2021-04-14T09:04:15.385Z
         * expiration : 2021-04-14T09:04:15.385Z
         */

        private String level;
        private String beginning;
        private String expiration;
        private boolean expired;

        public boolean isExpired() {
            return expired;
        }

        public void setExpired(boolean expired) {
            this.expired = expired;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getBeginning() {
            return beginning;
        }

        public void setBeginning(String beginning) {
            this.beginning = beginning;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }
    }

}
