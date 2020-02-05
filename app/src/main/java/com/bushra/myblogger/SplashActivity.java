package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity
{

    private final int SPLASH_DISPLAY_LENGTH = 2500;
    private ConstraintLayout splashScreen;
    private ImageView splashImage;
    private SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "Bushra";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashScreen=findViewById(R.id.splash_screen);
        splashImage =findViewById(R.id.splash_image);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        splashImage.startAnimation(animation);

       new Handler().postDelayed(new Runnable()
       {
            @Override
            public void run()
            {
               // Check if user is signed in (non-null) and update UI accordingly.
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String name = sharedpreferences.getString("name",null );
                updateUI(name);

           }
        }, SPLASH_DISPLAY_LENGTH);


    }



    public  void updateUI( String name)
    {

        if(name!=null)
        {
            Intent userIntent= PostListActivity.newIntent(SplashActivity.this);
            startActivity(userIntent);
            finish();
        }
        else
        {
            Intent loginIntent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }


}

