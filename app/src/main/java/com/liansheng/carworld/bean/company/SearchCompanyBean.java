package com.liansheng.carworld.bean.company;

import android.os.Parcel;
import android.os.Parcelable;

import com.liansheng.carworld.bean.me.IdentityCardBean;

public class SearchCompanyBean implements Parcelable {

    private String id;
    private String company;
    private IdentityCardBean identityCard;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public IdentityCardBean getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(IdentityCardBean identityCard) {
        this.identityCard = identityCard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.company);
        dest.writeParcelable(this.identityCard, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.company = source.readString();
        this.identityCard = source.readParcelable(IdentityCardBean.class.getClassLoader());
    }

    public SearchCompanyBean() {
    }

    protected SearchCompanyBean(Parcel in) {
        this.id = in.readString();
        this.company = in.readString();
        this.identityCard = in.readParcelable(IdentityCardBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchCompanyBean> CREATOR = new Parcelable.Creator<SearchCompanyBean>() {
        @Override
        public SearchCompanyBean createFromParcel(Parcel source) {
            return new SearchCompanyBean(source);
        }

        @Override
        public SearchCompanyBean[] newArray(int size) {
            return new SearchCompanyBean[size];
        }
    };
}
