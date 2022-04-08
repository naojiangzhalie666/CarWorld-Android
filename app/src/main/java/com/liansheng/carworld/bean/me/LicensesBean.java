package com.liansheng.carworld.bean.me;

import android.os.Parcel;
import android.os.Parcelable;

public class LicensesBean implements Parcelable {
    /**
     * id : 0
     * owner : string
     * num : string
     * positive : string
     * back : string
     * side : string
     * status : verifying
     * note : string
     * type : IdentityCard
     * expiration : 2021-04-16T07:47:35.964Z
     */

    private String id;
    private String owner;
    private String num;
    private String positive;
    private String back;
    private String side;
    private String status;
    private String note;
    private String type;
    private String expiration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.owner);
        dest.writeString(this.num);
        dest.writeString(this.positive);
        dest.writeString(this.back);
        dest.writeString(this.side);
        dest.writeString(this.status);
        dest.writeString(this.note);
        dest.writeString(this.type);
        dest.writeString(this.expiration);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.owner = source.readString();
        this.num = source.readString();
        this.positive = source.readString();
        this.back = source.readString();
        this.side = source.readString();
        this.status = source.readString();
        this.note = source.readString();
        this.type = source.readString();
        this.expiration = source.readString();
    }

    public LicensesBean() {
    }

    protected LicensesBean(Parcel in) {
        this.id = in.readString();
        this.owner = in.readString();
        this.num = in.readString();
        this.positive = in.readString();
        this.back = in.readString();
        this.side = in.readString();
        this.status = in.readString();
        this.note = in.readString();
        this.type = in.readString();
        this.expiration = in.readString();
    }

    public static final Creator<LicensesBean> CREATOR = new Creator<LicensesBean>() {
        @Override
        public LicensesBean createFromParcel(Parcel source) {
            return new LicensesBean(source);
        }

        @Override
        public LicensesBean[] newArray(int size) {
            return new LicensesBean[size];
        }
    };
}
