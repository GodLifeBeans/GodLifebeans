package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.ActivityMealaddBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.*;

public class MealAddActivity extends AppCompatActivity {
    ActivityMealaddBinding binding;
    private int GET_GALLERY_IMAGE = 0;
    private String mediaPath = null;
    private String bytestream;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMealaddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");

        //갤러리에서 식단 이미지 추가
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        binding.sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //query 날리기
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String tts = timeToString(timestamp);
                if(mediaPath != null){
                    File file = new File(mediaPath);
                    Meals meal = new Meals(binding.name.getText().toString(), id, tts.substring(0, 8), tts.substring(8, 12), binding.comment.getText().toString());

                    //addrequest
                    RequestQueue addrequest = Volley.newRequestQueue(MealAddActivity.this);
                    String uri = String.format("http://"+HOST+"/add_meals?user_id="+id+"&name="+binding.name.getText().toString()+"&comment="+
                            binding.comment.getText().toString()+"&date"+tts.substring(0, 8)+"&time"+tts.substring(8, 12));
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener() {
                        @Override
                        public void onResponse(Object response){
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray result = jsonObject.getJSONArray("result");
                                Log.d("result",result.toString());
                                for (int i = 0 ; i<result.length() ; i++){
                                    JSONObject usage = result.getJSONObject(i);
                                    Log.d("usage", usage.toString());
                                    int finish = usage.getInt("complete");
                                    String content = usage.getString("content");
                                    Log.d("finish",String.valueOf(finish));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("add meal volley", "error");
                        }
                    });
                    addrequest.add(stringRequest);
                    //imageadd request

                    //imageurl add request

                }
            }
        });
    }
    @SuppressLint("Range")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_GALLERY_IMAGE){
            if(resultCode == RESULT_OK){
                Bitmap bitmap = null;

                String[] proj = {MediaStore.Images.Media.DATA};
                Uri photoUri = data.getData();
                Cursor cursor = getContentResolver().query(photoUri, proj, null, null, null);
                cursor.moveToFirst();
                mediaPath = cursor.getString(cursor.getColumnIndex(proj[0]));
                cursor.close();

                try{
                    InputStream in = getContentResolver().openInputStream(photoUri);
                    bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    binding.image.setImageBitmap(bitmap);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] image = bos.toByteArray();
                    bytestream = Base64.encodeToString(image, 0);
                    Log.d("bytestream", bytestream);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
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
