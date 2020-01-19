package com.bushra.myblogger;

import android.content.Intent;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity
{

    private final int SPLASH_DISPLAY_LENGTH = 2500;

    private FirebaseAuth mAuth;
    ConstraintLayout splashScreen;
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashScreen=findViewById(R.id.splash_screen);
        splashImage =findViewById(R.id.splash_image);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        splashImage.startAnimation(animation);



       new Handler().postDelayed(new Runnable(){
            @Override
            public void run()
            {
               // Check if user is signed in (non-null) and update UI accordingly.
                 mAuth = FirebaseAuth.getInstance();

               FirebaseUser currentUser = mAuth.getCurrentUser();
               updateUI(currentUser);

           }
        }, SPLASH_DISPLAY_LENGTH);


    }



    public  void updateUI(FirebaseUser currentUser)
    {
        if(currentUser==null)
        {
            Intent loginIntent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else
        {
            Intent userIntent=new Intent(SplashActivity.this,BlogsActivity.class);
            startActivity(userIntent);
            finish();
        }
    }


}

