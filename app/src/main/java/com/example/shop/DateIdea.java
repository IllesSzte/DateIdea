package com.example.shop;

import android.os.Parcel;
import android.os.Parcelable;

public class DateIdea implements Parcelable{
    private String id;
    private String name;
    private String price;

    public DateIdea(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public DateIdea() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }

    public String _getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    protected DateIdea(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
    }

    public static final Parcelable.Creator<DateIdea> CREATOR = new Creator<DateIdea>() {
        @Override
        public DateIdea createFromParcel(Parcel in) {
            return new DateIdea(in);
        }

        @Override
        public DateIdea[] newArray(int size) {
            return new DateIdea[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
    }
}

