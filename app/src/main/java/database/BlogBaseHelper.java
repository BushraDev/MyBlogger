package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlogBaseHelper extends SQLiteOpenHelper
{


    private static final String DATABASE_NAME="my_blog";
    private static final int VERSION=1;


    public BlogBaseHelper(Context context)
    {

        super(context, DATABASE_NAME, null, VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        sqLiteDatabase.execSQL("create table " + BlogDbSchema.UsersTable.NAME + "(" +
                BlogDbSchema.UsersTable.Cols.ID + " primary key autoincrement, " +
                BlogDbSchema.UsersTable.Cols.UUID+","+
                BlogDbSchema.UsersTable.Cols.NAME + ", " +
                BlogDbSchema.UsersTable.Cols.E_MAIL + ", " +
                BlogDbSchema.UsersTable.Cols.PASSWORD +"," +
                BlogDbSchema.UsersTable.Cols.BIRTH_DATE+","+
                BlogDbSchema.UsersTable.Cols.GENDER+
                ")"
        );

        sqLiteDatabase.execSQL("create table " + BlogDbSchema.PostsTable.NAME + "(" +
                BlogDbSchema.PostsTable.Cols.ID + " primary key autoincrement, " +
                BlogDbSchema.PostsTable.Cols.UUID + ", " +
                BlogDbSchema.PostsTable.Cols.TITLE + ", " +
                BlogDbSchema.PostsTable.Cols.CATEGORY + ", " +
                BlogDbSchema.PostsTable.Cols.CONTENT +"," +
                BlogDbSchema.PostsTable.Cols.DATE+","+
                BlogDbSchema.PostsTable.Cols.FAVORITE+"," +
                BlogDbSchema.PostsTable.Cols.U_ID +" constraint foreign key (u_id) references users (u_id) "+
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
