package com.bushra.myblogger;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User
{

    private int uId;
    private String uName;
    private String uEmail;
    private String uPassword;
    private String uBirthDate;
    private String uPhoto;
    private String uGender;

    public static ContentValues getContentValues(User u)
    {
        ContentValues values=new ContentValues();

        return values;

    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getuBirthDate() {
        return uBirthDate;
    }

    public void setuBirthDate(String uBirthDate) {
        this.uBirthDate = uBirthDate;
    }

    public String getuPhoto() {
        return uPhoto;
    }

    public void setuPhoto(String uPhoto) {
        this.uPhoto = uPhoto;
    }

    public String getuGender() {
        return uGender;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }

    public static User getUser(JSONObject jsonObject) throws JSONException {
        int uId=jsonObject.getInt("u_id");
        Log.e("user id ",uId+"");
        String uName=jsonObject.getString("name");
        String uEmail=jsonObject.getString("e_mail");
        String uPassword=jsonObject.getString("password");
        String uBirthDate=jsonObject.getString("birth_date");
        String uPhoto=jsonObject.getString("photo");
        String uGender=jsonObject.getString("gender");
        User user=new User();
        user.setuId(uId);
        user.setuName(uName);
        user.setuEmail(uEmail);
        user.setuPassword(uPassword);
        user.setuBirthDate(uBirthDate);
        user.setuPhoto(uPhoto);
        user.setuGender(uGender);
        return user;
    }
}
