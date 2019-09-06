package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ChatUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView idTv = findViewById(R.id.idTv);
        Log.i("Main","메인 액티비티 실행");

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new ChatUser(idTv.getText().toString());
                Intent i = new Intent();
                Intent ia = new Intent();
                ComponentName cname = new ComponentName("com.example.chatapplication","com.example.chatapplication.ChatService");
                i.setComponent(cname);
                i.putExtra("suser",user);
                Log.i("Main",user.getUsername());
                startService(i);
                Log.i("Main","ChatService 실행");

                ia.setAction("WAITING_ROOM_ACTIVITY");
                ia.putExtra("user",user);
                Log.i("Main",user.getUsername());
                startActivity(ia);
                Log.i("Main","Waiting Room Activity실행");

            }
        });

    }
}
