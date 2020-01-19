package com.bushra.myblogger;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.BlogBaseHelper;
import database.BlogDbSchema;
public class BlogLab
{

    private static BlogLab sBlogLab;
    private Context bContext;
    private SQLiteDatabase bDatabase;

    private BlogLab(Context context)
    {
        bContext = context.getApplicationContext();
        bDatabase = new BlogBaseHelper(bContext)
                .getWritableDatabase();
    }


    public void addUser(UserModel u)
    {

        ContentValues values = UserModel.getContentValues(u);
        bDatabase.insert(BlogDbSchema.UsersTable.NAME, null, values);
    }


    public static BlogLab get(Context context)
    {
        if (sBlogLab == null)
        {
            sBlogLab = new BlogLab(context);
        }
        return sBlogLab;
    }

}
