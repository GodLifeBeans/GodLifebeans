package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WakeupActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 201;
    ImageView wakeup_img;
    Button gallery_btn;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    TextView choiceWakeup;
    String Img_path ;
    Button save_btn;
    Uri uriimage;
    String Date;
    String Hour;
    String user_id;

    String mediaPath;
    RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);
        wakeup_img =findViewById(R.id.wakeup_img);
        gallery_btn = findViewById(R.id.gallery_btn);
        save_btn = findViewById(R.id.save_btn);
        Date = getTime();
        DatePickerTimeline timeline = (DatePickerTimeline)findViewById(R.id.calendar);

        //intent에서 값 받아오기
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");

        //저장 버튼
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //갤러리 버튼
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                month = month+1;
                Log.d("ondateselected", ""+year+month+day);
                Date = ""+year+month+day;

                RequestQueue requestQueue = Volley.newRequestQueue(WakeupActivity.this);
                String uri = String.format("http://" + HOST + "/show_wakeup?user_id=" + user_id + "&date=" + Date);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new com.android.volley.Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.d("response", jsonObject.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String url = jsonObject1.getString("wakeup_img");
                            String time = jsonObject1.getString("wakeup_time");
                            Log.d("date", Date);
                            Log.d("url", url);
                            Log.d("time", time);
                            if (time!=null){
                                choiceWakeup.setText(time);
                            }
                            else{
                                choiceWakeup.setText("시간을 선택하세요");
                            }
                            if (url!=null){
                                Glide.with(getApplicationContext()).load(url).into(wakeup_img);
                            }
                            else{
                             wakeup_img.setImageResource(R.drawable.background);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            choiceWakeup.setText("시간을 선택하세요");
                            wakeup_img.setImageResource(R.drawable.background);
                            Log.d("오류", "여긴가?");
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        choiceWakeup.setText("시간을 선택하세요");
                        wakeup_img.setImageResource(R.drawable.background);
                        Log.d("볼리에러", error.toString());
                    }
                });
                requestQueue.add(stringRequest);

            }
        });

        choiceWakeup = findViewById(R.id.choiceWakeup);
        //시간 고르는 text
        choiceWakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(WakeupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        // EditText에 출력할 형식 지정
                        choiceWakeup.setText( " " + selectedHour + ":" + selectedMinute +" "+state);
                        Hour = selectedHour + ": " + selectedMinute +" "+state;
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        handlePostRequest();
            }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQUEST_CODE){
            if (resultCode == RESULT_OK){
                uriimage = data.getData();
                    //이미지 띄우기
                Glide.with(this.getApplicationContext()).load(uriimage).into(wakeup_img);

                Cursor cursor = getApplicationContext().getContentResolver().query(Uri.parse(uriimage.toString()), null, null, null, null);
                assert cursor != null;

                cursor.moveToFirst();
                mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            }else if(requestCode == RESULT_CANCELED){

            }
        }
    }

    private void handlePostRequest(){
        Button save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<JsonObject> call = service.addReview(user_id,Hour,Date);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.d("response",response.body().toString());
                        handleImagePost(response.body().get("result").getAsInt());
                       // handleImagePost(1);
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void handleImagePost(int wakeupId){
        if(mediaPath != null) {
            File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);

            Call<JsonObject> call = service.addReviewImage(fileToUpload, wakeupId);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "소중한 후기 감사합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "사진 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.e("THROW", t.getMessage());
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "소중한 후기 감사합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        java.util.Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMdd");
        String getTime = dateFormat.format(date);
        return getTime;
    }
}
