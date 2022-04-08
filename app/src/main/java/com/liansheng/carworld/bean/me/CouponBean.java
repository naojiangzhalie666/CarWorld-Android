package com.liansheng.carworld.bean.me;

import android.os.Parcel;
import android.os.Parcelable;

public class CouponBean implements Parcelable {

    private int denomination;
    private String userId;
    private String type;
    private String usedDate;
    private boolean used;
    private String id;
    private String creation;
    private String expireDate;

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
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
        dest.writeInt(this.denomination);
        dest.writeString(this.userId);
        dest.writeString(this.type);
        dest.writeString(this.usedDate);
        dest.writeByte(this.used ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.creation);
        dest.writeString(this.expireDate);
    }

    public void readFromParcel(Parcel source) {
        this.denomination = source.readInt();
        this.userId = source.readString();
        this.type = source.readString();
        this.usedDate = source.readString();
        this.used = source.readByte() != 0;
        this.id = source.readString();
        this.creation = source.readString();
        this.expireDate = source.readString();
    }

    public CouponBean() {
    }

    protected CouponBean(Parcel in) {
        this.denomination = in.readInt();
        this.userId = in.readString();
        this.type = in.readString();
        this.usedDate = in.readString();
        this.used = in.readByte() != 0;
        this.id = in.readString();
        this.creation = in.readString();
        this.expireDate = in.readString();
    }

    public static final Parcelable.Creator<CouponBean> CREATOR = new Parcelable.Creator<CouponBean>() {
        @Override
        public CouponBean createFromParcel(Parcel source) {
            return new CouponBean(source);
        }

        @Override
        public CouponBean[] newArray(int size) {
            return new CouponBean[size];
        }
    };
}
