package com.example.myapplication;


public class Contact {
    private String userid;
    private String phonenum;
    private String url;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Contact(String userid,String url, String phonenum) {
        this.userid = userid;
        this.url = url;
        this.phonenum = phonenum;

    }

    public Contact() {

    }
    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }


}
