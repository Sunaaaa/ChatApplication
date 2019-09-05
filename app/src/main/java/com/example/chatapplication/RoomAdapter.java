package com.example.chatapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends BaseAdapter {
    private List<Room> list = null;

    public RoomAdapter(Context context) {
        list = new ArrayList<Room>();
    }

    public RoomAdapter(Context context, List<Room> rlist) {
        this.list = rlist;
    }

    public void addItem(Room room){
        list.add(room);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.room_list, viewGroup, false);
        }
        TextView roomNo = (TextView)view.findViewById(R.id.roomNo);
        TextView roomName = (TextView)view.findViewById(R.id.roomName);
        TextView roomBoss = (TextView)view.findViewById(R.id.roomBoss);
        //Button roomBtn = (Button)view.findViewById(R.id.roomBtn);

        Room room = list.get(i);
        Log.i("Room_Adapter", room.toString());
        roomNo.setText("" + room.getRoomno());
        roomName.setText(room.getTitle());
        roomBoss.setText(room.getBoss());

        // Message msgs = list.get(i);
        // msg.setText(msgs.getMsg());
        return view;
    }
}