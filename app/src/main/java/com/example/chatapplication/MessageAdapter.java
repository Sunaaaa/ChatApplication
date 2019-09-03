package com.example.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    private ArrayList<Message> list = null;

    public MessageAdapter(Context context) {
        list = new ArrayList<Message>();
    }

    public void addItem(String msg){
        Message m = new Message(msg);
        list.add(m);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chatting_message, viewGroup, false);
        }
        TextView msg = (TextView)view.findViewById(R.id.msg);
        Message msgs = list.get(position);
        msg.setText(msgs.getMsg());
        return view;
    }
}
