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
                ComponentName cname = new ComponentName("com.example.chatapplication","com.example.chatapplication.WaitingRoomActivity");
                i.setComponent(cname);
                i.putExtra("user",user);
                startActivity(i);
                Log.i("Main","메인 인텐트 실행");
            }
        });

    }
}
