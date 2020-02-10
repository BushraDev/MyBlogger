package com.bushra.myblogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class LoginFragment extends Fragment
{

    private static final String DIALOG_SIGN_UP = "DialogSignUp";

    private EditText userEmail,userPass;
    private Button loginBtn,signUpBtn;
    private static ProgressDialog progress;

    public static Fragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        userEmail=v.findViewById(R.id.user_email);
        userPass=v.findViewById(R.id.user_pass);
        loginBtn=v.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String email = userEmail.getText().toString();
                final String pass = userPass.getText().toString();

                if (userEmail.getText().toString().matches(""))
                    Toast.makeText(getActivity(),"please insert your email",Toast.LENGTH_SHORT).show();
                else {
                    if (!isEmailValid(userEmail.getText()))
                        Toast.makeText(getActivity(), "please insert a valid email", Toast.LENGTH_SHORT).show();
                    else {
                        if (userPass.getText().toString().matches(""))
                            Toast.makeText(getActivity(), "please insert your password", Toast.LENGTH_SHORT).show();
                        else {
                            BlogLab blogLab = BlogLab.get(getActivity());
                            if (blogLab.haveNetwork()) {
                                progress = new ProgressDialog(getActivity());
                                progress.setMessage("Logging in...");
                                progress.setCancelable(false);
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.show();
                                BlogLab.UserVolleyListiner listiner = new BlogLab.UserVolleyListiner() {
                                    @Override
                                    public void onsucss(User user) {
                                        progress.dismiss();
                                        checkUser(user, getActivity());
                                    }
                                };

                                blogLab.getUser(email, pass, getActivity(), listiner);
                            } else if (!blogLab.haveNetwork()) {
                                Intent i = new Intent(getActivity(), NoItemInternetImage.class);
                                startActivity(i);
                            }
                        }
                    }
                }


                }

        });

        signUpBtn=v.findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getActivity(),SignUpActivity.class);
                startActivity(i);
                getActivity().finish();

            }

        });

        return v;
    }



    public  static void checkUser(User user,Context context)
    {
        if(user != null)
        {
            try
            {
                String MyPREFERENCES = "Bushra";
                SharedPreferences sharedpreferences= context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE );
                SharedPreferences.Editor editor= sharedpreferences.edit();
                editor.putInt("id", user.getuId());
                editor.putString("name", user.getuName());
                editor.putString("email", user.getuEmail());
                editor.putString("photoUrl", user.getuPhoto());
                editor.commit();

                sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String name = sharedpreferences.getString("name", "gg");
                int id = sharedpreferences.getInt("id", 5);
                Log.e("here is name ",name+"");
                Log.e("here is id ",id+"");

                Intent i = PostListActivity.newIntent(context);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                ((Activity)context).finish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            final AlertDialog.Builder dialog=new AlertDialog.Builder(context);
            dialog.setMessage(  "User not exist\nplease check your email and password , then try again");
            dialog.setCancelable(true);

            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                }
            });

            dialog.create();
            dialog.show();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
