package com.androideradev.www.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceReview implements Parcelable {

    private String tipTime;
    private String tipText;
    private String tipRatting;
    private String userName;
    private Photo userPhoto;

    public PlaceReview(String tipTime, String tipText, String tipRatting, String userName, Photo userPhoto) {
        this.tipTime = tipTime;
        this.tipText = tipText;
        this.tipRatting = tipRatting;
        this.userName = userName;
        this.userPhoto = userPhoto;
    }

    public String getTipTime() {
        return tipTime;
    }

    public void setTipTime(String tipTime) {
        this.tipTime = tipTime;
    }

    public String getTipText() {
        return tipText;
    }

    public void setTipText(String tipText) {
        this.tipText = tipText;
    }

    public String getTipRatting() {
        return tipRatting;
    }

    public void setTipRatting(String tipRatting) {
        this.tipRatting = tipRatting;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Photo getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Photo userPhoto) {
        this.userPhoto = userPhoto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tipTime);
        dest.writeString(this.tipText);
        dest.writeString(this.tipRatting);
        dest.writeString(this.userName);
        dest.writeParcelable(this.userPhoto, flags);
    }

    protected PlaceReview(Parcel in) {
        this.tipTime = in.readString();
        this.tipText = in.readString();
        this.tipRatting = in.readString();
        this.userName = in.readString();
        this.userPhoto = in.readParcelable(Photo.class.getClassLoader());
    }

    public static final Creator<PlaceReview> CREATOR = new Creator<PlaceReview>() {
        @Override
        public PlaceReview createFromParcel(Parcel source) {
            return new PlaceReview(source);
        }

        @Override
        public PlaceReview[] newArray(int size) {
            return new PlaceReview[size];
        }
    };
}
