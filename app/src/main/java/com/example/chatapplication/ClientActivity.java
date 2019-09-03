package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class ClientActivity extends AppCompatActivity {
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    ListView listView;
    EditText msgEt;
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
            }catch (Exception e) {
                Log.i("ReceiveRunnable", "서버 연결 실패" +  e.toString());
            }
            try {
                while (((msg = br.readLine()) != null)) {
//                    if (!((msg = br.readLine()) != null))
//                        break;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", msg);
                    Message message = new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Log.i("ReceiveRunnable", "메시지 전달!");
                    Log.i("ReceiveRunnable", msg);
                }
            }catch (IOException e) {
                Log.i("ReceiveRunnable", e.toString());
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
                String msg = (String)blockingQueue.take();

                out.println(msg);
                out.flush();

                Log.i("SendRunnable", "채팅 보내기!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        TextView myid = (TextView)findViewById(R.id.myid);
        msgEt = (EditText)findViewById(R.id.msgEt);

        Intent intent = getIntent();
        myid.setText(intent.getStringExtra("id"));

        ListView listView = (ListView)findViewById(R.id.chat);
        final MessageAdapter adapter = new MessageAdapter(this);
        listView.setAdapter(adapter);

        Button exBtn = (Button)findViewById(R.id.exBtn);
        Button sendBtn = (Button)findViewById(R.id.sendBtn);
        exBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockingQeque.add("EXIT");
                SendRunnable sendRunnable = new SendRunnable(blockingQeque);
                Thread t = new Thread(sendRunnable);
                t.start();
            }
        });

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String mymsg = bundle.getString("msg");
                adapter.addItem(mymsg);
                adapter.notifyDataSetChanged();
                Log.i("Handler", mymsg);
            }
        };

        ReceiveRunnable receiveRunnable = new ReceiveRunnable(br, handler);
        Log.i("ReceiveRunnable","ReceiveRunnable 실행할 예정임");

        Thread t = new Thread(receiveRunnable);
        t.start();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMsg = msgEt.getText().toString();
                blockingQeque.add(sendMsg);
                SendRunnable sendRunnable = new SendRunnable(blockingQeque);
                Thread t = new Thread(sendRunnable);
                t.start();
            }
        });
    }
}
