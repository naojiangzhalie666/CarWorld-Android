package com.liansheng.carworld.bean.home;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentsDTO implements Parcelable {

    private String owner;
    private String ownerId;
    private String relation;
    private String relationId;
    private String text;



    protected CommentsDTO(Parcel in) {
        owner = in.readString();
        ownerId = in.readString();
        relation = in.readString();
        relationId = in.readString();
        text = in.readString();
    }

    public static final Creator<CommentsDTO> CREATOR = new Creator<CommentsDTO>() {
        @Override
        public CommentsDTO createFromParcel(Parcel in) {
            return new CommentsDTO(in);
        }

        @Override
        public CommentsDTO[] newArray(int size) {
            return new CommentsDTO[size];
        }
    };

    public CommentsDTO() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(owner);
        parcel.writeString(ownerId);
        parcel.writeString(relation);
        parcel.writeString(relationId);
        parcel.writeString(text);
    }
}
