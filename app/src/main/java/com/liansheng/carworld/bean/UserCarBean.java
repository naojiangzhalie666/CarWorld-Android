package com.liansheng.carworld.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserCarBean implements Parcelable {

    private String id;
    private boolean bigCar;
    private String truckRequire;
    private String number;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBigCar() {
        return bigCar;
    }

    public void setBigCar(boolean bigCar) {
        this.bigCar = bigCar;
    }

    public String getTruckRequire() {
        return truckRequire;
    }

    public void setTruckRequire(String truckRequire) {
        this.truckRequire = truckRequire;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.bigCar ? (byte) 1 : (byte) 0);
        dest.writeString(this.truckRequire);
        dest.writeString(this.number);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.bigCar = source.readByte() != 0;
        this.truckRequire = source.readString();
        this.number = source.readString();
    }

    public UserCarBean() {
    }

    protected UserCarBean(Parcel in) {
        this.id = in.readString();
        this.bigCar = in.readByte() != 0;
        this.truckRequire = in.readString();
        this.number = in.readString();
    }

    public static final Parcelable.Creator<UserCarBean> CREATOR = new Parcelable.Creator<UserCarBean>() {
        @Override
        public UserCarBean createFromParcel(Parcel source) {
            return new UserCarBean(source);
        }

        @Override
        public UserCarBean[] newArray(int size) {
            return new UserCarBean[size];
        }
    };
}
