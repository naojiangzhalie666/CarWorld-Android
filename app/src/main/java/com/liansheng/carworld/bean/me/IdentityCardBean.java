package com.liansheng.carworld.bean.me;

import android.os.Parcel;
import android.os.Parcelable;

public class IdentityCardBean implements Parcelable {

    private String owner;
    private String num;
    private String positive;
    private String back;
    private String side;
    private String type;
    private String id;
    private String userId;
    private String employeeId;
    private String status;
    private String note;
    private String expiration;
    private boolean beforPass;
    private boolean expired;
    private String creation;

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

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public boolean isBeforPass() {
        return beforPass;
    }

    public void setBeforPass(boolean beforPass) {
        this.beforPass = beforPass;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
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
        dest.writeString(this.owner);
        dest.writeString(this.num);
        dest.writeString(this.positive);
        dest.writeString(this.back);
        dest.writeString(this.side);
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.employeeId);
        dest.writeString(this.status);
        dest.writeString(this.note);
        dest.writeString(this.expiration);
        dest.writeByte(this.beforPass ? (byte) 1 : (byte) 0);
        dest.writeByte(this.expired ? (byte) 1 : (byte) 0);
        dest.writeString(this.creation);
    }

    public void readFromParcel(Parcel source) {
        this.owner = source.readString();
        this.num = source.readString();
        this.positive = source.readString();
        this.back = source.readString();
        this.side = source.readString();
        this.type = source.readString();
        this.id = source.readString();
        this.userId = source.readString();
        this.employeeId = source.readString();
        this.status = source.readString();
        this.note = source.readString();
        this.expiration = source.readString();
        this.beforPass = source.readByte() != 0;
        this.expired = source.readByte() != 0;
        this.creation = source.readString();
    }

    public IdentityCardBean() {
    }

    protected IdentityCardBean(Parcel in) {
        this.owner = in.readString();
        this.num = in.readString();
        this.positive = in.readString();
        this.back = in.readString();
        this.side = in.readString();
        this.type = in.readString();
        this.id = in.readString();
        this.userId = in.readString();
        this.employeeId = in.readString();
        this.status = in.readString();
        this.note = in.readString();
        this.expiration = in.readString();
        this.beforPass = in.readByte() != 0;
        this.expired = in.readByte() != 0;
        this.creation = in.readString();
    }

    public static final Parcelable.Creator<IdentityCardBean> CREATOR = new Parcelable.Creator<IdentityCardBean>() {
        @Override
        public IdentityCardBean createFromParcel(Parcel source) {
            return new IdentityCardBean(source);
        }

        @Override
        public IdentityCardBean[] newArray(int size) {
            return new IdentityCardBean[size];
        }
    };
}