package com.example.chatapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatService extends Service {

    private final IBinder iBinder = new ChatBinder();
    private Socket socket;
    BufferedReader br;
    PrintWriter out;
    BlockingQueue blockingQeque = new ArrayBlockingQueue(30);
    ChatUser meuser;

    // 서버에서 보내는 데이터를 받는 Thread
    class ReceiveServiceRunnable implements Runnable{
        Intent receiveIntern = new Intent();

        public ReceiveServiceRunnable() {
        }

        @Override
        public void run() {
            String msg ="";
            try {
                // 서버와 연결
                socket = new Socket("70.12.115.72", 7878);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                Log.i("ReceiveServiceRunnable", "서버 연결 성공");
                out.println("/@loginUser," + meuser.getUsername());
                out.flush();
            }catch (Exception e) {
                Log.i("ReceiveServiceRunnable", "안되~~ 서버 연결 실패" +  e.toString());
            }
            try {
                while (((msg = br.readLine()) != null)){
                    Log.i("Waiting_Room_Receive", "서버의 데이터를 받는다.");
                    String[] msgArray = msg.split(",");
                    Log.i("Waiting_Room_Receive", msgArray.toString());
//                    if (msgArray[0].equals("/@loginUser")){
//                        Log.i("Waiting_Room_Receive", "나 로그인했다.");
//                        ComponentName componentName = new ComponentName("com.example.chatapplication","com.example.chatapplication.WaitingRoomActivity");
//                        receiveIntern.putExtra("", "");
//                    }
                    if (msgArray[0].equals("/@showRoomList")){
                        Log.i("Waiting_Room_Receive", "방 목록 보여줄래?");
                        Log.i("Waiting_Room_Receive", msg);
                    }
                    if (msgArray[0].equals("/@newRoom")){
                        Log.i("Waiting_Room_Receive", "방만들었는데 어떠니");
                        Log.i("Waiting_Room_Receive", msg);
                    }
                    if (msgArray[0].equals("/@msg")){
                        Log.i("Waiting_Room_Receive", "메시지가 왔는데?");
                        Log.i("Waiting_Room_Receive", msg);
                    }
                    receiveIntern.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    receiveIntern.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    receiveIntern.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    Log.i("Waiting_Room_Receive", "받았다. 꾸미자.");
                }
            }catch (Exception e){
                Log.i("Waiting_Room_Error", "서버랑 연결해서 존재하는 방만들기 실패.." +  e.toString());
            }
        }
    }

    // 클라이언트에서 서버로 보내는 Thread
    class SendServiceRunnable implements Runnable{
        BlockingQueue blockingQueue;
        public SendServiceRunnable(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
        }
        @Override
        public void run() {
            try {
                while(true){
                    String msg = (String)blockingQueue.take();
                    Log.i("SendServiceRunnable", "보내기 얍얍얍!"+msg);
                    out.println(msg);
                    out.flush();
                    Log.i("SendServiceRunnable", "보내기 얍얍얍!___________"+msg);
                }
            } catch (InterruptedException e) {
                Log.i("SendServiceRunnable", "보내기 얍얍얍!___________ 문제 발생");
                e.printStackTrace();
            }
        }
    }

    public class ChatBinder extends Binder{
        ChatService getService(){
            return ChatService.this;
        }
    }

    public ChatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    // 서비스 호출될 때마다 수행
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        meuser = intent.getParcelableExtra("user");

        SendServiceRunnable sendServiceRunnable = new SendServiceRunnable(blockingQeque);
        ReceiveServiceRunnable receiveServiceRunnable = new ReceiveServiceRunnable();
        Thread st = new Thread(sendServiceRunnable);
        Thread rt = new Thread(receiveServiceRunnable);
        st.start();
        rt.start();

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
