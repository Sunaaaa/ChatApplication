package com.example.chatapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WaitingRoomActivity extends AppCompatActivity {

    RoomManager roomManager = new RoomManager();
    TextView wr_myid;
    TextView wr_count;
    ChatUser user;
    Stream<Room> stream;
    int roomCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        wr_myid = (TextView)findViewById(R.id.wr_myid);
        Button newRoomBtn = (Button)findViewById(R.id.newRoomBtn);
        Button wr_exBtn = (Button)findViewById(R.id.wr_exBtn);
        wr_count = (TextView)findViewById(R.id.wr_count);

        Intent i = getIntent();
        user = new ChatUser(i.getStringExtra("id"));
        Log.i("Waiting_Room",user.getUsername());

        wr_myid.setText(user.getUsername());
        wr_count.setText("채팅 (" + roomManager.getRoomCount() + ")");
        final ListView listView = (ListView)findViewById(R.id.roomList);
        final RoomAdapter adapter = new RoomAdapter(this);
        listView.setAdapter(adapter);

        wr_exBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.chatapplication","com.example.chatapplication.MainActivity");
                i.setComponent(cname);
                startActivity(i);
                Log.i("Waiting_Room","메인 엑티비티로 이동@");
            }
        });

        newRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Anonymouse : 클래스 선언하면서 인스턴스를 바로 올림
                final EditText et = new EditText(WaitingRoomActivity.this);
                final Room room = new Room();

                // 경고창 용도로 사용되는 AlertDialog 생성
                AlertDialog.Builder dialog = new AlertDialog.Builder(WaitingRoomActivity.this);

                // Dialog에 title과 message 설정
                dialog.setTitle("새 채팅방 만들기");
                dialog.setMessage("새로 만드는 채팅방의 이름을 적어 주세요.");

                // Dialog 에 입력상자 (EditText)
                dialog.setView(et);

                // Dialog의 취소/확인 버튼에 이벤트 적용
                // positive 버튼 : Dialog 인터페이스가 가지는 onClickListener의 인스턴스를 가짐 -> Dialog의 onClick() 메소드를 오버 라이드
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        room.setTitle(et.getText().toString());
                        roomCnt++;
                        room.setRoomno(roomCnt);
                        room.setBoss(user.getUsername());
                        roomManager.addRoom(room);
                        wr_count.setText("채팅 (" + roomCnt + ")");
                        adapter.addItem(room);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

                Log.i("Waiting_Room","방만들기 완료");

                Log.i("RRRRRRR__RRRRR","=================================");
                for (int i = 0; i < roomManager.getRoomList().size(); i++){
                    Log.i("RRRRRRR__RRRRR","--------------" + roomManager.getRoomList().get(i).getRoomno());
                    Log.i("RRRRRRR__RRRRR","--------------" + roomManager.getRoomList().get(i).getTitle());
                }
                Log.i("RRRRRRR__RRRRR","=================================");

            }
        });
    }
}
