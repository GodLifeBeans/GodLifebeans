package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivityMealsBinding;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealsActivity extends AppCompatActivity {
    private ArrayList<Meals> mealsArrayList;
    private static final String HOST = "192.249.19.168";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //binding = ActivityMealsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_meals);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String tts = timestamp.toString();
        DatePickerTimeline timeline = (DatePickerTimeline)findViewById(R.id.datepickertimeline);
        timeline.setFirstVisibleDate(Integer.parseInt(tts.substring(0,4)),
                Integer.parseInt(tts.substring(5,7)), Integer.parseInt(tts.substring(8, 10)));
        timeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
            }
        });
        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                Log.d("ondateselected", ""+year+month+day);
                String date = dateString(year, month, day);
                RequestQueue requestQueue2 = Volley.newRequestQueue(MealsActivity.this);
                String uri = String.format("http://"+HOST+"/get_todo?user_id="+id+"&date="+date);
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
                            }

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

        Button plusbutton = (Button)findViewById(R.id.plusbutton);
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
    public String dateString(int year, int month, int date){
        if(1<=month&&month<=9){
            return ""+year+"0"+month+date;
        }
        else return ""+year+month+date;
    }
}
