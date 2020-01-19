package com.bushra.myblogger;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment
{

    private static final String DIALOG_SIGN_UP = "DialogSignUp";


    EditText userEmail,userPass;
    Button loginBtn,signUpBtn;

    private FirebaseAuth mAuth;
    LinearLayout loginLayout;
    ProgressBar progressBar;


    public static Fragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        loginLayout=v.findViewById(R.id.loginlayout);
        userEmail=v.findViewById(R.id.user_email);
        userPass=v.findViewById(R.id.user_pass);
        loginBtn=v.findViewById(R.id.login_btn);
        progressBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);
        loginLayout.addView(progressBar);
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final String email = userEmail.getText().toString();
                final String pass = userPass.getText().toString();

                if(email.matches(""))
                    Toast.makeText(getActivity(),"please insert your email",Toast.LENGTH_SHORT).show();
                else
                    {
                        if (pass.equals(""))
                            Toast.makeText(getActivity(), "please insert your pass", Toast.LENGTH_SHORT).show();
                        else
                        {
                            progressBar.setVisibility(View.VISIBLE);

                            mAuth = FirebaseAuth.getInstance();
                            mAuth.signInWithEmailAndPassword(email,pass)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task)
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            if(task.isSuccessful())
                                            {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                updateUI(user);
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                updateUI(null);

                                            }
                                        }
                                    });
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
                FragmentManager manager = getFragmentManager();
                SignUpFragment dialog = new SignUpFragment();
                dialog.show(manager, DIALOG_SIGN_UP);
            }

        });

        return v;
    }

    public  void updateUI(FirebaseUser currentUser)
    {
        if(currentUser==null)
        {
            Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent userIntent=new Intent(getActivity(),BlogsActivity.class);
            startActivity(userIntent);
        }
    }


}
