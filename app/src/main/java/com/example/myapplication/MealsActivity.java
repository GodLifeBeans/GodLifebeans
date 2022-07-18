package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.ActivityMealsBinding;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealsActivity extends AppCompatActivity {
    private ArrayList<Meals> mealsArrayList = new ArrayList<>();
    private static final String HOST = "192.249.19.168";
    private AdapterMeals adapterMeals;
    ActivityMealsBinding binding;
    String date;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMealsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String tts = timeToString(timestamp);

        date = tts.substring(0, 8);
        Log.d("date?", date);
        getMealList(id, date);

        binding.datepickertimeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
            }
        });
        binding.datepickertimeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                Log.d("ondateselected", ""+year+month+day);
                date = dateString(year, month, day);
                getMealList(id, date);
            }
        });
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(this, 1));

        adapterMeals = new AdapterMeals(mealsArrayList);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(adapterMeals);

        binding.plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealAddActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getMealList(id, date);
    }
    public String dateString(int year, int month, int date){
        if(1<=month&&month<=9){
            return ""+year+"0"+month+date;
        }
        else return ""+year+month+date;
    }

    private void getMealList(String id, String date){
        ImageAddApi imageAddApi = RetrofitClientInstance.getRetrofitInstance().create(ImageAddApi.class);
        imageAddApi.getTodayMeals(id, date).enqueue(new Callback<ArrayList<Meals>>() {
            @Override
            public void onResponse(Call<ArrayList<Meals>> call, Response<ArrayList<Meals>> response) {
                Log.d("response", response.toString());
                mealsArrayList.clear();
                mealsArrayList.addAll(response.body());
                adapterMeals.notifyDataSetChanged();
                adapterMeals = new AdapterMeals(mealsArrayList);
                binding.recyclerview.setAdapter(adapterMeals);
                Toast.makeText(getApplicationContext(), "이미지 불러옴", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ArrayList<Meals>> call, Throwable t) {
                Log.d("retrofit failure", "meal get failure");
            }
        });
    }
    public String timeToString(Timestamp timestamp){
        String ts = timestamp.toString();
        String year = ts.substring(0, 4);
        String month = ts.substring(5, 7);
        String day = ts.substring(8, 10);
        String hour = ts.substring(11, 13);
        String minute = ts.substring(14, 16);
        String sec = ts.substring(17, 19);
        return year+month+day+hour+minute+sec;
    }
}