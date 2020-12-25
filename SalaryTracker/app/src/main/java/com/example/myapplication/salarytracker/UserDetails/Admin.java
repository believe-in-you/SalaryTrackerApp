package com.example.myapplication.salarytracker.UserDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class Admin implements Parcelable {

    private String name, username, password, phoneno;

    // Empty constructor for firestore
    public Admin() {

    }

    public Admin(String name, String username, String password, String phoneno) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneno = phoneno;

    }

    protected Admin(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        phoneno = in.readString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(phoneno);
    }

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

}
