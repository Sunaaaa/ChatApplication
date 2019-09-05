package com.example.chatapplication;

import android.app.Service;
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
                    android.os.Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Log.i("Waiting_Room_Receive", msg);
                    Log.i("Waiting_Room_Receive", "받았다. 꾸미자.");
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
