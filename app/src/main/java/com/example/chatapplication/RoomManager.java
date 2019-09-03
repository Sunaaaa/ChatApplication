package com.example.chatapplication;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private List<Room> roomList = new ArrayList<Room>();

    public RoomManager() {
    }

    public void addRoom(Room room){
        this.roomList.add(room);
    }

    public void removeRoom(Room room){
        this.roomList.remove(room);
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

}
