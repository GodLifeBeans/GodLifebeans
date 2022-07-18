package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsActivity  extends AppCompatActivity {
    RecyclerView friend_rv;
    private ArrayList<Contact> arrayList;
    private LinearLayoutManager linearLayoutManager;
    ContactAdapter contactAdapter;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        friend_rv = (RecyclerView)findViewById(R.id.friend_rv);

        arrayList = new ArrayList<Contact>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        friend_rv.setLayoutManager(linearLayoutManager);
        contactAdapter = new ContactAdapter(arrayList);
        friend_rv.setAdapter(contactAdapter);


        //여기서 사용자 목록 받아와서 추가
        RequestQueue requestQueue = Volley.newRequestQueue(FriendsActivity.this);
        String uri = String.format("http://"+HOST+"/get_users");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray result = jsonObject.getJSONArray("result");
                    Log.d("result",result.toString());

                  for (int i = 0 ; i<result.length();i++){
                      JSONObject usage = result.getJSONObject(i);
                        Log.d("usage", usage.toString());
                        String userid = usage.getString("user_id");
                        String name = usage.getString("name");
                        String user_img = usage.getString("user_img");
                        Contact contact = new Contact(userid,name,user_img);
                        arrayList.add(contact);
                    }
                    Log.d("arrayList_size", String.valueOf(arrayList.size()));
                    contactAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("todo 볼리에러","에러");
            }
        });
        requestQueue.add(stringRequest);





    }
}
