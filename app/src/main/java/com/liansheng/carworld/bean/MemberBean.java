package com.liansheng.carworld.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberBean implements Parcelable {

    private String id;
    private String name;
    private String mobile;
    private String type;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.type);
        dest.writeString(this.userId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.mobile = source.readString();
        this.type = source.readString();
        this.type=source.readString();
    }

    public MemberBean() {
    }

    protected MemberBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.type = in.readString();
        this.userId=in.readString();
    }

    public static final Parcelable.Creator<MemberBean> CREATOR = new Parcelable.Creator<MemberBean>() {
        @Override
        public MemberBean createFromParcel(Parcel source) {
            return new MemberBean(source);
        }

        @Override
        public MemberBean[] newArray(int size) {
            return new MemberBean[size];
        }
    };
}
