package com.example.myapplication;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class Meals {
    @SerializedName("meals_id")
    private int meal_id;
    @SerializedName("picture")
    private String imageurl;
    @SerializedName("name")
    private String name;
    @SerializedName("user_id")
    private String userid;
    @SerializedName("time")
    private String time;
    @SerializedName("date")
    private String date;
    @SerializedName("comment")
    private String comment;
    @SerializedName("complete")
    private boolean checked = false;

    public Meals(String name, String userid, String date, String time, String comment){
        this.name = name;
        this.userid = userid;
        this.time = time;
        this.date = date;
        this.comment = comment;
    }
    public void setMeal_id(int meal_id){this.meal_id = meal_id;}
    public void setName(String name){this.name = name;}
    public void setImageurl(String imageurl){this.imageurl = imageurl;}
    public void setUserid(String userid){this.userid = userid;}
    public void setTime(String time){this.time = time;}
    public void setComment(String comment){this.comment = comment;}
    public void setChecked(){checked = true;}
    public void setDate(String date){this.date = date;}
    public int getMeal_id(){return meal_id;}
    public String getImageurl(){return imageurl;}
    public String getName(){return name;}
    public String getTime(){return time;}
    public String getUserid(){return userid;}
    public String getComment(){return comment;}
}
interface ImageAddApi {
    @Multipart
    @POST("/meals/img")  //add picture to my meal sent
    Call<JsonObject> uploadImage(@Part MultipartBody.Part file, @Query("meals_id") int meals_id);
    @POST("/add_meals")  //add my meal without image
    Call<JsonObject> addMeal(@Body Meals meal);
    @POST("/get_meals")  //get my today meal -> recyclerview
    Call<ArrayList<Meals>> getTodayMeals(@Body Meals_GetToday today);
    @POST("/verify_meals")  //verify my friend's meal -> add stamp to friend
    Call<MealsResponse> verifyFriendMeals(@Body Meals_verifyFriend verify);
}
class MealsResponse{//실패 or 성공
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess(){
        return success;
    }
}
class MealsAddResponse{ //이미지 빼고 나머지 저장 후 그 id 리턴
    @SerializedName("result")
    private int resultid;

    public int getResultid(){return resultid;}
}
class Meals_GetToday{
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("date")
    private String date;

    public Meals_GetToday(String user_id, String date){
        this.user_id = user_id;
        this.date = date;
    }
}
class Meals_verifyFriend{
    @SerializedName("meal_id")
    private int meal_id;

    public Meals_verifyFriend(int meal_id){
        this.meal_id = meal_id;
    }
}
class Meals_UriAdd{
    @SerializedName("meal_id")
    private int meal_id;

    public Meals_UriAdd(int meal_id){
        this.meal_id = meal_id;
    }
}