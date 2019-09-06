package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class WaitingRoomActivity extends AppCompatActivity {

    List<Room> roomList;
    TextView wr_myid;
    TextView wr_count;
    ChatUser user;
    ChatService chatService;
    boolean isService = false;
    WaitingRoomActivity waitingRoomActivity;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("BIND"," 바인딩을 시작한다.");
            ChatService.ChatBinder chatBinder = (ChatService.ChatBinder) iBinder;
            Log.i("BIND","바인딩을 성공했는가");
            chatService = chatBinder.getService();
            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to ChatService
        Intent intent = new Intent(WaitingRoomActivity.this, ChatService.class);
        bindService(intent, connection, Context.BIND_ABOVE_CLIENT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        isService = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        // 레이아웃 객체 생성
        wr_myid = (TextView)findViewById(R.id.wr_myid);
        Button newRoomBtn = (Button)findViewById(R.id.newRoomBtn);
        final Button wr_exBtn = (Button)findViewById(R.id.wr_exBtn);
        wr_count = (TextView)findViewById(R.id.wr_count);
        final ListView listView = (ListView)findViewById(R.id.roomList);

        // Intent로 값 가져와 설정
        Intent fromServiceIntent = getIntent();
        // 사용자 이름 가져와
        user = fromServiceIntent.getParcelableExtra("user");
        if (user != null){
            Log.i("fromServiceIntent","꺄꺄꺄꺄꺄꺄꺄꺄꺄꺄꺄꺆꺄ㅑ꺆꺄꺄꺄");
            wr_myid.setText(user.getUsername());
        }else {
            Log.i("fromServiceIntent","왜.......왜......왜......안넘어와요...");
        }

        // 채팅방 목록
        final RoomAdapter adapter = new RoomAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("WR_ListVIew","ListVIew 클릭");
                if (!isService){
                    Log.i("isService", "isService가 없어요");
                    return;
                }
                else {
                    Log.i("isService", "isService도 있고 바인딩도 되었다. ");
                }

            }
        });

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

        // 방만들기
        newRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Anonymouse : 클래스 선언하면서 인스턴스를 바로 올림
                final EditText et = new EditText(WaitingRoomActivity.this);
                final Room room = new Room();

                // 경고창 용도로 사용되는 AlertDialog 생성
                AlertDialog.Builder dialog = new AlertDialog.Builder(WaitingRoomActivity.this);
                dialog.setTitle("새 채팅방 만들기");
                dialog.setMessage("새로 만드는 채팅방의 이름을 적어 주세요.");
                dialog.setView(et);

                // Dialog의 취소/확인 버튼에 이벤트 적용
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        room.setTitle(et.getText().toString());
                        room.setBoss(user.getUsername());
                        String str = "/@newRoom" + ',' +room.getTitle() + ',' +room.getBoss();
                        chatService.toServer(str);
                        Log.i("STR__________", str);
                    }
                });
                dialog.setNegativeButton("NO", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("receiveDate_onNewIntent", "왜 여기 안들어 오시나요....");
        String receiveDate = intent.getStringExtra("receiveIntent");
        Log.i("receiveDate_onNewIntent", receiveDate);
        String[] msgArray = receiveDate.split(",");
         switch (msgArray[0]){
             case "/@showRoomList" :
                 Log.i("receiveDate_onNewIntent", "/@showRoomList");
                 if (msgArray[1].equals("0")) {
                     wr_count.setText("0");
                 } else {
                     wr_count.setText(msgArray[1]);
                 }
                 break;
             case "/@msg" :
                 Log.i("receiveDate_onNewIntent", "/@msg");
                 Toast.makeText(getApplicationContext(), "메시지 왔어요~", Toast.LENGTH_SHORT);
                 break;
             case "/@newRoom" :
                 Log.i("receiveDate_onNewIntent", "/@newRoom");
                 Toast.makeText(getApplicationContext(), "새로운 방 만들었다는디요 ~", Toast.LENGTH_SHORT);
                 break;
         }
    }
}
