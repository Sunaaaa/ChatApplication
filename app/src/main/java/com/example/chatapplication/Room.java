package com.example.chatapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Room implements Parcelable {

    private String boss;
    private String title;
    private static int roomno;

    public Room() {
    }

    public Room(int roomno, String title, String userid) {
        this.boss = userid;
        this.roomno = roomno;
        this.title = title;
    }



    protected Room(Parcel in) {
        boss = in.readString();
        title = in.readString();
        roomno = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boss);
        dest.writeString(title);
        dest.writeInt(roomno);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    // 방장 변경
//    public void setBoss(String userid, String roomno) {
//        this.boss = userid;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public int getRoomno() {
        return roomno;
    }

    public void setRoomno(int roomno) {
        this.roomno = roomno;
    }
}
