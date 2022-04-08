package com.liansheng.carworld.bean.me;

import android.os.Parcel;
import android.os.Parcelable;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class CompanyBean implements IPickerViewData, Parcelable {

    /**
     * id : 637541920150231502
     * verifyStatus : verifying
     * name : 国鸿
     * address : {"province":"浙江省","city":"温州市","county":"龙港市","name":"名豪大酒店","street":"龙港大道锦港嘉园1号楼","longitude":"120.524473","latitude":"27.577485"}
     * type : AutoShop
     * frontDoorPhoto : null
     * secondHandPriority : false
     * businessLicense : {"photo":"/upload/2021-04-16/4a621560e7fa69ce.jpeg","mainBrand":null}
     */

    private long id;
    private String status;
    private String name;
    private AddressBean address;
    private String type;
    private String note;
    private String frontDoorPhoto;
    private boolean secondHandPriority;
    private String businessLicense;
    private String mainBrand;
    private List<String> frontDoorPhotos;

    public List<String> getFrontDoorPhotos() {
        return frontDoorPhotos;
    }

    public void setFrontDoorPhotos(List<String> frontDoorPhotos) {
        this.frontDoorPhotos = frontDoorPhotos;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrontDoorPhoto() {
        return frontDoorPhoto;
    }

    public void setFrontDoorPhoto(String frontDoorPhoto) {
        this.frontDoorPhoto = frontDoorPhoto;
    }

    public boolean isSecondHandPriority() {
        return secondHandPriority;
    }

    public void setSecondHandPriority(boolean secondHandPriority) {
        this.secondHandPriority = secondHandPriority;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getMainBrand() {
        return mainBrand;
    }

    public void setMainBrand(String mainBrand) {
        this.mainBrand = mainBrand;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.status);
        dest.writeString(this.name);
        dest.writeParcelable(this.address, flags);
        dest.writeString(this.type);
        dest.writeString(this.note);
        dest.writeString(this.frontDoorPhoto);
        dest.writeByte(this.secondHandPriority ? (byte) 1 : (byte) 0);
        dest.writeString(this.businessLicense);
        dest.writeString(this.mainBrand);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.status = source.readString();
        this.name = source.readString();
        this.address = source.readParcelable(AddressBean.class.getClassLoader());
        this.type = source.readString();
        this.note = source.readString();
        this.frontDoorPhoto = source.readString();
        this.secondHandPriority = source.readByte() != 0;
        this.businessLicense = source.readString();
        this.mainBrand = source.readString();
    }

    public CompanyBean() {
    }

    protected CompanyBean(Parcel in) {
        this.id = in.readLong();
        this.status = in.readString();
        this.name = in.readString();
        this.address = in.readParcelable(AddressBean.class.getClassLoader());
        this.type = in.readString();
        this.note = in.readString();
        this.frontDoorPhoto = in.readString();
        this.secondHandPriority = in.readByte() != 0;
        this.businessLicense = in.readString();
        this.mainBrand = in.readString();
    }

    public static final Parcelable.Creator<CompanyBean> CREATOR = new Parcelable.Creator<CompanyBean>() {
        @Override
        public CompanyBean createFromParcel(Parcel source) {
            return new CompanyBean(source);
        }

        @Override
        public CompanyBean[] newArray(int size) {
            return new CompanyBean[size];
        }
    };
}
