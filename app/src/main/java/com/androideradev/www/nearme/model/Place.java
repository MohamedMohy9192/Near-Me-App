package com.androideradev.www.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Place implements Parcelable {

    private String placeId;
    private String name;
    private String phone;
    private String address;
    private String crossStreet;
    private double lat;
    private double lng;
    private String placeType;
    private String ratting;
    private List<Photo> placePhotos;
    private List<PlaceReview> placeReviews;
    private String placeStatus;
    private boolean isOpen;


    public Place(String placeId, String name, String phone, String address, String crossStreet, double lat, double lng, String placeType) {
        this.placeId = placeId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.crossStreet = crossStreet;
        this.lat = lat;
        this.lng = lng;
        this.placeType = placeType;
    }

    public Place(String name, String phone, String address, String crossStreet, String placeType, String ratting, List<Photo> photos, List<PlaceReview> placeReviews, String placeStatus, boolean isOpen) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.crossStreet = crossStreet;
        this.placeType = placeType;
        this.ratting = ratting;
        this.placePhotos = photos;
        this.placeReviews = placeReviews;
        this.placeStatus = placeStatus;
        this.isOpen = isOpen;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    public List<Photo> getPlacePhotos() {
        return placePhotos;
    }

    public void setPlacePhotos(List<Photo> placePhotos) {
        this.placePhotos = placePhotos;
    }

    public List<PlaceReview> getPlaceReviews() {
        return placeReviews;
    }

    public void setPlaceReviews(List<PlaceReview> placeReviews) {
        this.placeReviews = placeReviews;
    }

    public String getPlaceStatus() {
        return placeStatus;
    }

    public void setPlaceStatus(String placeStatus) {
        this.placeStatus = placeStatus;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeId);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.crossStreet);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.placeType);
        dest.writeString(this.ratting);
        dest.writeList(this.placePhotos);
        dest.writeTypedList(this.placeReviews);
        dest.writeString(this.placeStatus);
        dest.writeByte(this.isOpen ? (byte) 1 : (byte) 0);
    }

    protected Place(Parcel in) {
        this.placeId = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.crossStreet = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.placeType = in.readString();
        this.ratting = in.readString();
        this.placePhotos = new ArrayList<Photo>();
        in.readList(this.placePhotos, Photo.class.getClassLoader());
        this.placeReviews = in.createTypedArrayList(PlaceReview.CREATOR);
        this.placeStatus = in.readString();
        this.isOpen = in.readByte() != 0;
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel source) {
            return new Place(source);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
}
