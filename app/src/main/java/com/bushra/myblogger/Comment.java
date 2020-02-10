package com.bushra.myblogger;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment implements Parcelable {
    int pId;
    int uId;
    String comment;

    public  Comment()
    {

    }

    protected Comment(Parcel in) {
        pId = in.readInt();
        uId = in.readInt();
        comment = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public static Comment getComment(JSONObject jsonObject) throws JSONException {
        int pId=jsonObject.getInt("p_id");
        int uId=jsonObject.getInt("u_id");
        String comment=jsonObject.getString("comment");

        Comment c=new Comment();

        c.setpId(pId);
        c.setuId(uId);
        c.setComment(comment);

        return c;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pId);
        parcel.writeInt(uId);
        parcel.writeString(comment);
    }
}
