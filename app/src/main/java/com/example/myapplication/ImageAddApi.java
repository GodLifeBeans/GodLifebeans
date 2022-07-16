package com.example.myapplication;

import java.util.ArrayList;
import java.util.Map;
import com.google.gson.annotations.SerializedName;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
public interface ImageAddApi {
    @Multipart
    @POST("/meal_picture_add")
    Call<AddResponse> uploadImage(@Part MultipartBody.Part file);
}
class AddResponse{
    @SerializedName("resultcode")
    private int code;

    public int getCode(){
        return code;
    }
}
