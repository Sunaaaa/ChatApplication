package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    RoomManager roomManager = new RoomManager();
    TextView wr_myid;
    TextView wr_count;
    ChatUser user;
    Socket socket;
    PrintWriter out = null;
    BufferedReader br = null;
    BlockingQueue blockingQeque = new ArrayBlockingQueue(30);

    class ReceiveRunnable implements Runnable{
        private BufferedReader br;
        private Handler handler;

        public ReceiveRunnable(BufferedReader br, Handler handler) {
            this.br = br;
            this.handler = handler;
        }

        @Override
        public void run() {
            String msg ="";
            try {
                socket = new Socket("70.12.115.72", 7878);
                Log.i("ReceiveRunnable",socket.toString());

                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                Log.i("ReceiveRunnable", "서버 연결 성공");
                out.println("/@showRoomList" );
                out.flush();
            }catch (Exception e) {
                Log.i("ReceiveRunnable", "서버 연결 실패" +  e.toString());
            }
            try {
                while (((msg = br.readLine()) != null)){
                    Log.i("Waiting_Room_Receive", "받는다.");
                    Bundle bundle = new Bundle();
                    String[] msgArray = msg.split(",");
                    Log.i("Waiting_Room_Receive", msgArray[0]);
                    Log.i("Waiting_Room_Receive", msgArray[1]);

                    if (msgArray[0].equals("/@showRoomList")){
                        Log.i("Waiting_Room_Receive", "0이다.");
                        Log.i("Waiting_Room_Receive", msg);
                        bundle.putString("data", msgArray[1]);
                        bundle.putString("protocol", "/@showRoomList");
                    }
                    if (msgArray[0].equals("/@updateRoomList")){
                        Log.i("Waiting_Room_Receive", "방만들었");
                        Log.i("Waiting_Room_Receive", msg);
                        bundle.putStringArray("data", msgArray);
                        bundle.putString("protocol", "/@updateRoomList");
                    }
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Log.i("Waiting_Room_Receive", msg);
                    Log.i("Waiting_Room_Receive", "보낸다.");
                }
            }catch (Exception e){
                Log.i("Waiting_Room_Error", "서버랑 연결해서 존재하는 방만들기 실패.." +  e.toString());
            }
        }
    }
    class SendRunnable implements Runnable{
        BlockingQueue blockingQueue;
        public SendRunnable(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
        }
        @Override
        public void run() {
            try {
                while(true){
                    String msg = (String)blockingQueue.take();
                    Log.i("SendRunnable", "보내기 얍얍얍!"+msg);
                    out.println(msg);
                    out.flush();
                    Log.i("SendRunnable", "보내기 얍얍얍!___________"+msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        wr_myid = (TextView)findViewById(R.id.wr_myid);
        Button newRoomBtn = (Button)findViewById(R.id.newRoomBtn);
        final Button wr_exBtn = (Button)findViewById(R.id.wr_exBtn);

        wr_count = (TextView)findViewById(R.id.wr_count);

        Intent i = getIntent();
        user = i.getParcelableExtra("user");
        Log.i("Waiting_Room","내 이름은 " + user.getUsername());

        wr_myid.setText(user.getUsername());
        wr_count.setText("채팅 (" + roomManager.getRoomList().size() + ")");
        final ListView listView = (ListView)findViewById(R.id.roomList);
        final RoomAdapter adapter = new RoomAdapter(this);
        listView.setAdapter(adapter);


        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String protocol = bundle.getString("protocol");
                if (protocol.equals("/@showRoomList")){
                    String data = bundle.getString("data");
                    wr_count.setText(data);
                }
                if (protocol.equals("/@updateRoomList")){
                    String[] data = bundle.getStringArray("data");
                    for (int i = 0; i < data.length-1; i++){
                        Log.i("data이다.", data[i]);
                    }
                    wr_count.setText(data[1]);
                    List<Room> rlist = new ArrayList<Room>();
                    for (int i = 2; i < data.length-1; i+=3){
                        Room r = new Room();
                        int no = Integer.parseInt(data[i]);
                        r.setRoomno(no);
                        r.setTitle(data[i+1]);
                        r.setBoss(data[i+2]);
                        rlist.add(r);
                        adapter.addItem(r);
                        adapter.notifyDataSetChanged();
                    }
//                    RoomAdapter roomAdapter2 = new RoomAdapter(getApplicationContext(), rlist);
//                    listView.setAdapter(adapter);
                    Log.i("Handler", "리스트 출력");
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.i("Handler_Item_Click", "클릭되었다.");
//                        Intent intent = new Intent();
//                        intent.putExtra("roomNo", );
                        int roomNo = ((Room)adapter.getItem(i)).getRoomno();
                        Toast.makeText(getApplicationContext(), "" + roomNo, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("chatid", user.getUsername());
                        intent.putExtra("chatRoomNo", ""+roomNo);
                        ComponentName componentName = new ComponentName("com.example.chatapplication", "com.example.chatapplication.ClientActivity");
                        intent.setComponent(componentName);
                        startActivity(intent);
                    }
                });
            }
        };

        ReceiveRunnable receiveRunnable = new ReceiveRunnable(br, handler);
        SendRunnable sendRunnable = new SendRunnable(blockingQeque);
        Thread rt = new Thread(receiveRunnable);
        Thread st = new Thread(sendRunnable);
        rt.start();
        st.start();


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
                        room.setBoss(user.getUsername());
                        roomManager.addRoom(room);
                        String str = "/@newRoom" + ',' +room.getTitle() + ',' +room.getBoss();
                        blockingQeque.add(str);
//                        SendRunnable sendRunnable = new SendRunnable(blockingQeque);
//                        Thread t = new Thread(sendRunnable);
//                        t.start();

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
}
