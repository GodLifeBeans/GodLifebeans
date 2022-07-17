package com.example.myapplication;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService {

    @POST("wakeup")
    Call<JsonObject> addReview(@Query("user_id") String user_id,@Query("wakeup_time") String wakeup_time,@Query("date") String date);


    @Multipart
    @POST("wakeup/img")
    Call<JsonObject> addReviewImage(@Part MultipartBody.Part file, @Query("wakeupId") int wakeupId);


}
