package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FriendsMap extends AppCompatActivity {
    TextView friend_name;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_map);
        friend_name = (TextView)findViewById(R.id.friend_name);
        Intent intent = getIntent();
       String name =  intent.getStringExtra("name");
       name = name + "네 콩가";
       String user_id=  intent.getStringExtra("user_id");
       Log.d("name, user_id", name +" name "+ user_id);
       friend_name.setText(name);



    }
}