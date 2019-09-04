package com.example.chatapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatUser implements Parcelable {
    private String username;

    public ChatUser() {
    }

    public ChatUser(String username) {
        this.username = username;
    }

    protected ChatUser(Parcel in) {
        username = in.readString();
    }

    public static final Creator<ChatUser> CREATOR = new Creator<ChatUser>() {
        @Override
        public ChatUser createFromParcel(Parcel in) {
            return new ChatUser(in);
        }

        @Override
        public ChatUser[] newArray(int size) {
            return new ChatUser[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
    }
}