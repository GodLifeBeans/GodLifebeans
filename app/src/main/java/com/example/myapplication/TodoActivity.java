package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private DatePickerDialog datePickerDialog;
    private RecyclerView todoRV;
    private TodoAdapter todoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Todo> arrayList;
    private Todo todo;
    private EditText input_todo;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        DatePickerTimeline timeline = (DatePickerTimeline)findViewById(R.id.calendar);
        TextView selectDate = (TextView)findViewById(R.id.selectDate);

        //리사이클러뷰 생성
        todoRV = findViewById(R.id.todoRV);
        linearLayoutManager = new LinearLayoutManager(this);
        todo = new Todo("2020",false);
        todoRV.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        arrayList.add(todo);
        todoAdapter = new TodoAdapter(arrayList);
        todoRV.setAdapter(todoAdapter);
        //제출 버튼
        submit = (Button)findViewById(R.id.submit);

        //editText
        input_todo = (EditText)findViewById(R.id.input_todo);

        //날짜 선택
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
                month = month+1;
                Log.d("ondateselected", ""+year+month+day+index);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo addtodo = new Todo(input_todo.getText().toString(),false);
                arrayList.add(addtodo);
                input_todo.setText("");
                todoAdapter.notifyDataSetChanged();;
            }
        });

    }

}
