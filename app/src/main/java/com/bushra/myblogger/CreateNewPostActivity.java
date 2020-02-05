package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateNewPostActivity extends AppCompatActivity
{
    private static final String EXTRA_UID="com.bushra.myblogger.uid";
    ImageView pPhoto;
    CircleImageView take_photo;
    Uri imgUri;
    Bitmap bm;

    Spinner dropDownList;
    ArrayList<String> speciality;
    ArrayAdapter<String>arrayAdapter;

    EditText pTitle,pContent;
    Button createPost;

    String category,title,content;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        final int uId=getIntent().getIntExtra(EXTRA_UID,0);

        pPhoto=findViewById(R.id.post_photo);
        pPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,0);
            }
        });

        take_photo=findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(i,1);
            }
        });

        dropDownList = findViewById(R.id.post_category);
        speciality = new ArrayList<>();
        speciality.add("Programming");
        speciality.add("Security");
        speciality.add("News");
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,speciality);
        dropDownList.setAdapter(arrayAdapter);

        dropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=dropDownList.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pTitle=findViewById(R.id.post_title);
        pTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title =charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pContent=findViewById(R.id.post_content);
        pContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                content=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        final String date =dayOfMonth+"/"+(month+1)+"/"+year;



        createPost =findViewById(R.id.create_post_btn);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String pho=BitMapToString(bm);

                BlogLab.get(CreateNewPostActivity.this).addPost(category,title,content,date,uId,pho);
                    startActivity(PostListActivity.newIntent(CreateNewPostActivity.this));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0 && resultCode==RESULT_OK && data != null)
        {

            imgUri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(imgUri);
                bm= BitmapFactory.decodeStream(inputStream);
                pPhoto.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==1 && resultCode==RESULT_OK && data != null)
        {
            bm= (Bitmap)data.getExtras().get("data");

            pPhoto.setImageBitmap(bm);
        }
    }

    public static Intent newIntent(Context c, int uId)
    {
        Intent i = new Intent(c, CreateNewPostActivity.class);
        i.putExtra(EXTRA_UID,uId);
        return i;
    }

    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
