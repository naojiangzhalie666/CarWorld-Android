package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CircleBean implements Parcelable {

    private List<CommentsDTO> comments;
    private UserDTO user;
    private int commentCounts;
    private String city;
    private String userId;
    private String type;
    private int price;
    private String priceType;
    private String text;
    private List<String> images;
    private String id;
    private String creation;

    protected CircleBean(Parcel in) {
        comments = in.createTypedArrayList(CommentsDTO.CREATOR);
        user = in.readParcelable(UserDTO.class.getClassLoader());
        commentCounts = in.readInt();
        city = in.readString();
        userId = in.readString();
        type = in.readString();
        price = in.readInt();
        priceType = in.readString();
        text = in.readString();
        images = in.createStringArrayList();
        id = in.readString();
        creation = in.readString();
    }

    public static final Creator<CircleBean> CREATOR = new Creator<CircleBean>() {
        @Override
        public CircleBean createFromParcel(Parcel in) {
            return new CircleBean(in);
        }

        @Override
        public CircleBean[] newArray(int size) {
            return new CircleBean[size];
        }
    };

    public List<CommentsDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentsDTO> comments) {
        this.comments = comments;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(comments);
        parcel.writeParcelable(user, i);
        parcel.writeInt(commentCounts);
        parcel.writeString(city);
        parcel.writeString(userId);
        parcel.writeString(type);
        parcel.writeInt(price);
        parcel.writeString(priceType);
        parcel.writeString(text);
        parcel.writeStringList(images);
        parcel.writeString(id);
        parcel.writeString(creation);
    }
}
