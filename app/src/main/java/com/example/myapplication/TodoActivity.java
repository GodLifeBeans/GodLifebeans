package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.badoualy.datepicker.DatePickerTimeline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoActivity  extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private RecyclerView todoRV;
    private TodoAdapter todoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Todo> arrayList;
    private Todo todo;
    private EditText input_todo;
    private Button submit;
    private String Date = " ";
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    private String date;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        DatePickerTimeline timeline = (DatePickerTimeline)findViewById(R.id.calendar);
        Intent intent =getIntent();

        String user_id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        //사실 이건 필요없음
        String profileImage = intent.getStringExtra("profileImage");

        Log.d("user_id, name,", user_id+name+profileImage);
        arrayList = new ArrayList<Todo>();
        //리사이클러뷰 생성
        todoRV = findViewById(R.id.todoRV);
        linearLayoutManager = new LinearLayoutManager(this);
        todoRV.setLayoutManager(linearLayoutManager);
        todoAdapter = new TodoAdapter(arrayList);
        todoRV.setAdapter(todoAdapter);
        //제출 버튼
        submit = (Button)findViewById(R.id.submit);

        //저장버튼
        save = (Button)findViewById(R.id.save);

        //editText
        input_todo = (EditText)findViewById(R.id.input_todo);

        Date = getTime();
        Log.d("처음 날짜 ",Date);
        //처음에 날짜에 해당하는 todo들 있는지,
        //있으면 arraylist에 add
        RequestQueue requestQueue = Volley.newRequestQueue(TodoActivity.this);
        String uri = String.format("http://"+HOST+"/get_todo?user_id="+user_id+"&date="+Date);
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
                        int finish = usage.getInt("complete");
                        String content = usage.getString("content");
                        Log.d("finish",String.valueOf(finish));
                        boolean bool;
                        if (finish == 1){
                            bool = true;
                        }else{
                            bool=false;
                        }
                        Todo addingtodo = new Todo(content,bool);
                        arrayList.add(addingtodo);
                    }
                    Log.d("arrayList_size", String.valueOf(arrayList.size()));
                    todoAdapter.notifyDataSetChanged();
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

        //날짜 선택
        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                arrayList.clear();
                todoAdapter.notifyDataSetChanged();
                month = month+1;
                Log.d("ondateselected", ""+year+month+day);
                Date = ""+year+month+day;
                Log.d("Date",Date);
                RequestQueue requestQueue2 = Volley.newRequestQueue(TodoActivity.this);
                String uri = String.format("http://"+HOST+"/get_todo?user_id="+user_id+"&date="+Date);
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, uri, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray result = jsonObject.getJSONArray("result");
                            Log.d("result",result.toString());
                            for (int i = 0 ; i<result.length();i++){
                                JSONObject usage = result.getJSONObject(i);
                                Log.d("usage", usage.toString());
                                int finish = usage.getInt("complete");
                                String content = usage.getString("content");
                                Log.d("finish",String.valueOf(finish));
                                boolean bool;
                                if (finish == 1){
                                    bool = true;
                                }else{
                                    bool=false;
                                }
                                Todo addingtodo = new Todo(content,bool);
                                arrayList.add(addingtodo);
                                Log.d("arrayList",content);
                            }
                           Log.d("arrayList_size", String.valueOf(arrayList.size()));
                            todoAdapter.notifyDataSetChanged();
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
                requestQueue2.add(stringRequest1);
            }
        });





        //추가 버튼
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo addtodo = new Todo(input_todo.getText().toString(), false);
                arrayList.add(addtodo);
                input_todo.setText("");
                todoAdapter.notifyDataSetChanged();
            }


        });

        //저장 버튼
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size =arrayList.size();
                for (int i = 0 ; i<size; i++){
                    Todo add = arrayList.get(i);
                    Log.d("todo",user_id+add.getContent()+add.isCompleted()+Date);
                    RequestQueue requestQueue = Volley.newRequestQueue(TodoActivity.this);
                    String uri = String.format("http://"+HOST+"/insert_todo?user_id="+user_id+"&content="+add.getContent()+"&complete="+add.isCompleted()+"&date="+Date);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                Log.d("response",jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("볼리에러","에러");
                        }
                    });
                    requestQueue.add(stringRequest);
                }
            }
        });

    }
    private String getTime() {
        long now = System.currentTimeMillis();
        java.util.Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMdd");
        String getTime = dateFormat.format(date);
        return getTime;
    }

}
