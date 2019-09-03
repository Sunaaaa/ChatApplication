package com.example.chatapplication;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private List<Room> roomList = new ArrayList<Room>();
    private int roomCount;
    private int cunRoomNum;

    public RoomManager() {
        this.roomCount = 0;
        this.cunRoomNum = 1;
    }

    public void addRoom(Room room){
        this.roomList.add(room);
    }

    public void removeRoom(Room room){
        this.roomList.remove(room);
        roomCount--;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public int getCunRoomNum() {
        return cunRoomNum;
    }

    public void setCunRoomNum(int cunRoomNum) {
        this.cunRoomNum = cunRoomNum;
    }
}
