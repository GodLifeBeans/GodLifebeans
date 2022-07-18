package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ActivityMealaddBinding;
import com.google.gson.JsonObject;

import java.io.File;
import java.sql.*;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealAddActivity extends AppCompatActivity {
    ActivityMealaddBinding binding;
    private int GET_GALLERY_IMAGE = 0;
    private String mediaPath = null;
    private Uri photoUri;
    private static final String HOST = "192.249.19.168";
    private static final String PORT = "80";
    private int add_id;
    ImageAddApi imageAddApi = RetrofitClientInstance.getRetrofitInstance().create(ImageAddApi.class);
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
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
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
                    final int[] mid = new int[1];
                    //add meal object request
                    imageAddApi.addMeal(meal).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if(response == null){
                                Log.d("null", "null");
                            }
                            else{
                                Log.d("type", String.valueOf(response.body().toString()));
                                add_id = response.body().get("result").getAsInt();
                            }
                            Log.d("responsebody", response.toString());
                            //mid[0] = res.getResultid();
                            //System.out.println(mid[0]);
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("retrofit failure", "meal add failure");
                        }
                    });
                    Log.d("add_id",String.valueOf(add_id));
                    //imageadd request
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);

                    imageAddApi.uploadImage(fileToUpload, add_id).enqueue(new Callback<JsonObject>() {
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
                            Toast.makeText(getApplicationContext(), "등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "please add image", Toast.LENGTH_LONG).show();
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
                photoUri = data.getData();
                Log.d("photoUri", String.valueOf(photoUri));
                //이미지 띄우기
                Glide.with(this.getApplicationContext()).load(photoUri).into(binding.image);

                Cursor cursor = getApplicationContext().getContentResolver().query(Uri.parse(photoUri.toString()), null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                Log.d("mediaPath", mediaPath);

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