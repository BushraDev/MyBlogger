package com.bushra.myblogger;

import android.content.ContentValues;

import java.util.Date;
import java.util.UUID;

public class UserModel
{

    private int uId;
    private String uName;
    private String uEmail;
    private String uPassword;
    private String uBirthDate;
    private String uPhoto;
    private String uGender;

    public UserModel(String uName, String uEmail, String uPassword, String uBirthDate, String uPhoto, String uGender) {
        this.uName = uName;
        this.uEmail = uEmail;
        this.uPassword = uPassword;
        this.uBirthDate = uBirthDate;
        this.uPhoto = uPhoto;
        this.uGender = uGender;
    }

    public static ContentValues getContentValues(UserModel u)
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
}
