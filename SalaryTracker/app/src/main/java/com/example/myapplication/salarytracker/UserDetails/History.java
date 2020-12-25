package com.example.myapplication.salarytracker.UserDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable {

    private String name, date, month, year, leaves, amount;

    public History() {}

    public History(String name, String date, String month, String year, String leaves, String amount) {
        this.name = name;
        this.date = date;
        this.month = month;
        this.year = year;
        this.leaves = leaves;
        this.amount = amount;
    }

    protected History(Parcel in) {
        name = in.readString();
        date = in.readString();
        month = in.readString();
        year = in.readString();
        leaves = in.readString();
        amount = in.readString();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getLeaves() {
        return leaves;
    }

    public String getAmount() {
        return amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(year);
        dest.writeString(leaves);
        dest.writeString(amount);
    }
}
