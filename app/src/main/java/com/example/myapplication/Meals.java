package com.example.myapplication;
import android.net.Uri;
public class Meals {
    private int meal_id;
    private String imageurl;
    private String name;
    private String userid;
    private String time;
    private String date;
    private String comment;
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