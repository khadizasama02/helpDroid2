package com.example.helpdroid.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String userName;
    String userEmail;
    String userPhoneNumber;
    String trustedPhoneNumber1;

    public User() {
    }

    public User(String userName, String userEmail, String userPhoneNumber, String trustedPhoneNumber1) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.trustedPhoneNumber1 = trustedPhoneNumber1;
    }

    protected User(Parcel in) {
        userName = in.readString();
        userEmail = in.readString();
        userPhoneNumber = in.readString();
        trustedPhoneNumber1 = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getTrustedPhoneNumber1() {
        return trustedPhoneNumber1;
    }

    public void setTrustedPhoneNumber1(String trustedPhoneNumber1) {
        this.trustedPhoneNumber1 = trustedPhoneNumber1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userPhoneNumber);
        dest.writeString(trustedPhoneNumber1);
    }
}
