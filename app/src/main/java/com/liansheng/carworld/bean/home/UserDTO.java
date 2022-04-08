package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDTO implements Parcelable {
    private String mobile;
    private String company;
    private String photo;
    private String name;

    protected UserDTO(Parcel in) {
        mobile = in.readString();
        company = in.readString();
        photo = in.readString();
        name = in.readString();
    }

    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mobile);
        parcel.writeString(company);
        parcel.writeString(photo);
        parcel.writeString(name);
    }
}
