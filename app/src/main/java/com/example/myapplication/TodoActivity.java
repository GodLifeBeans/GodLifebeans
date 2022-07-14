package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoActivity  extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    RecyclerView todoRV;
    TodoAdapter todoAdapter;
    EditText todo;
    LinearLayoutManager layoutManager;
    private ArrayList<Todo> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        DatePickerTimeline timeline = (DatePickerTimeline)findViewById(R.id.calendar);
        TextView selectDate = (TextView)findViewById(R.id.selectDate);
        todo = (EditText)findViewById(R.id.todo);
        todoRV = (RecyclerView)findViewById(R.id.todoRV);
        layoutManager = new LinearLayoutManager(this);
        todoRV.setLayoutManager(layoutManager);
        arrayList  =new ArrayList<>();
        todoAdapter = new TodoAdapter(arrayList);
        todoRV.setAdapter(todoAdapter);
        Todo todo= new Todo("2022","할일",true);
        arrayList.add(todo);
        todoAdapter.notifyDataSetChanged();

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int pYear = calendar.get(Calendar.YEAR);
                int pMonth = calendar.get(Calendar.MONTH);
                int pDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(TodoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "/" + month + "/" + day;
                        selectDate.setText(date);
                    }
                }, pYear, pMonth, pDay);
                datePickerDialog.show();
              //timeline.setSelectedDate(pYear, pMonth, pDay);
            }
        });
        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                Log.d("여기","dateselect");
                Log.d("ondateselected", ""+year+month+day+index);
              //  datePickerDialog.onDateChanged(datePickerDialog,year,month,day);
            }
        });


    }

}
