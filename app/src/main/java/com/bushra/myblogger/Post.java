package com.bushra.myblogger;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Post implements Serializable
{

    private int pId;
    private String pTitle;
    private String pCategory;
    private String pContent;
    private String pDate;
    private String pPhoto;
    private int pLikes;
    private int pComments;
    private int uId;
    public boolean progress = false;

    public Post(boolean progress) {
        this.progress = progress;
    }
    public Post() {

    }



    public static Post getPost(JSONObject post) throws JSONException
    {
        Post postModel=new Post();
        postModel.setpId(post.getInt("p_id"));
        postModel.setpTitle(post.getString("title"));
        postModel.setpCategory(post.getString("category"));
        postModel.setpContent(post.getString("content"));
        postModel.setpDate(post.getString("date"));
        postModel.setpPhoto(post.getString("photo"));
        postModel.setuId(post.getInt("u_id"));
        postModel.setpLikes(post.getInt("likes"));
        postModel.setpComments(post.getInt("comments"));

        return postModel;
    }

    public int getpLikes() {
        return pLikes;
    }

    public void setpLikes(int pLikes) {
        this.pLikes = pLikes;
    }

    public int getpComments() {
        return pComments;
    }

    public void setpComments(int pComments) {
        this.pComments = pComments;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public String getpContent() {
        return pContent;
    }

    public void setpContent(String pContent) {
        this.pContent = pContent;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getpDate()
    {
        return pDate;
    }

    public String getpPhoto() {
        return pPhoto;
    }

    public void setpPhoto(String photo) {
        this.pPhoto = photo;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }
}
