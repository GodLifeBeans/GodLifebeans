package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button goto_todo_btn;

    //intent로 받아온 값



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String profileImage = intent.getStringExtra("profile_image");
        String name=intent.getStringExtra("nickname");
        String id = intent.getStringExtra("kakao_id");
        Log.d("profile, name, id", ""+profileImage+name+id);


        Button goto_todo_btn = (Button)findViewById(R.id.goto_todo_btn);
        goto_todo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("id",id);
                intent.putExtra("profileImage",profileImage);
                startActivity(intent);
            }
        });


    }
}