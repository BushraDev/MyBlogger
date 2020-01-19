package com.bushra.myblogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpFragment extends DialogFragment
{

    CircleImageView uGalaryPhoto,uCameraPhoto;
    EditText uName,uEmail,uPassword;
    RadioGroup uGender;
    RadioButton maleBtn,femaleBtn;
    String genderType="null";
    Button uBirthdate,createAccount;
    Uri imgUri;
    Bitmap bm;
    int yearOB,monthOB,dayOB;
    LinearLayout.LayoutParams layoutParams;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {

        View v =LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sign_up,null);

        uGalaryPhoto=v.findViewById(R.id.galary_photo);
        uGalaryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,0);
            }
        });

        uCameraPhoto=v.findViewById(R.id.camera_photo);
        uCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(i,1);
            }
        });

        uName=v.findViewById(R.id.u_name);
        uEmail=v.findViewById(R.id.u_email);
        uPassword=v.findViewById(R.id.u_pass);
        uBirthdate=v.findViewById(R.id.u_birthdate);
        uBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        uBirthdate.setText(day+"/"+(month+1)+"/"+year);
                        yearOB=year;
                        monthOB=month+1;
                        dayOB=day;
                    }
                },year,month,dayOfMonth);
                datePickerDialog.show();
            }
        });


        uGender=v.findViewById(R.id.radio_gender);
        maleBtn=v.findViewById(R.id.radioMale);
        femaleBtn=v.findViewById(R.id.radioFemale);
        maleBtn.setText("male");
        femaleBtn.setText("female");
        uGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId==maleBtn.getId())
                    genderType="male";
                else if (checkedId==femaleBtn.getId())
                    genderType="female";
            }
        });

        createAccount=v.findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(bm==null)
                    Toast.makeText(getActivity(),"please choose your profile photo",Toast.LENGTH_SHORT).show();
                else
                {
                    if(uName.getText().toString().matches(""))
                        Toast.makeText(getActivity(),"please insert your name",Toast.LENGTH_SHORT).show();
                    else
                    {
                        if (uEmail.getText().toString().matches(""))
                            Toast.makeText(getActivity(),"please insert your email",Toast.LENGTH_SHORT).show();
                        else
                        {
                            if(!isEmailValid( uEmail.getText()))
                                Toast.makeText(getActivity(),"please insert a valid email",Toast.LENGTH_SHORT).show();
                            else
                            {
                                if(uPassword.getText().toString().matches(""))
                                    Toast.makeText(getActivity(),"please insert your password",Toast.LENGTH_SHORT).show();
                                else
                                {
                                    if (uBirthdate.getText().toString().equals(""))
                                        Toast.makeText(getActivity(),"please select your birth date",Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        if (genderType.matches("null"))
                                            Toast.makeText(getActivity(),"please select your gender",Toast.LENGTH_SHORT).show();
                                        else
                                        {
                                            final String photo=BitMapToString(bm);
                                            final String name = uName.getText().toString();
                                            final String email=uEmail.getText().toString();
                                            final String password = uPassword.getText().toString();
                                            final String birthdate = uBirthdate.getText().toString();
                                            mAuth = FirebaseAuth.getInstance();
                                            mAuth.createUserWithEmailAndPassword(email,password)
                                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>()
                                                    {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task)
                                                        {
                                                            if(task.isSuccessful())
                                                            {

                                                                UserModel userModel=new UserModel(name,email,password,birthdate,photo,genderType);
                                                                FirebaseFirestore.getInstance().collection("Users")
                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            if (getActivity() != null)

                                                                                Toast.makeText(getActivity(), "User registered Successfully", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                        else
                                                                        {
                                                                            if (getActivity() != null)

                                                                                Toast.makeText(getActivity(), "Registeration Failed ", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });

                                                                FirebaseUser user = mAuth.getCurrentUser();
                                                                updateUI(user);
                                                                getDialog().dismiss();
                                                            }
                                                            else
                                                                {
                                                                    if (getActivity() != null)
                                                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    updateUI(null);
                                                                }
                                                        }
                                                    });

                                        }

                                    }


                                }

                            }

                        }

                    }
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sign_up_title)
                .setView(v)
                .create();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Resources res = getResources();

        int titleId = res.getIdentifier("alertTitle", "id", "android");
        View title = getDialog().findViewById(titleId);
        if (title != null)
        {
             title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ((TextView)title).setTextColor(getResources().getColor(R.color.colorSignInBtn));
        }


    }

    public String BitMapToString(Bitmap bitmap)

       {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }

        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0 && resultCode==Activity.RESULT_OK && data != null)
        {

            imgUri=data.getData();
            try {
                InputStream inputStream=getActivity().getContentResolver().openInputStream(imgUri);
                bm= BitmapFactory.decodeStream(inputStream);
                uGalaryPhoto.setImageBitmap(bm);
            } catch (FileNotFoundException e)
            {
                Log.e("imguri","can't get bitmap of image uri");
            }
        }
        else if(requestCode==1 && resultCode== Activity.RESULT_OK && data != null)
        {
            bm= (Bitmap)data.getExtras().get("data");

            uGalaryPhoto.setImageBitmap(bm);
        }
    }
    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public  void updateUI(FirebaseUser currentUser)
    {
        if(currentUser==null)
        {
            if (getActivity() != null)
            {
                Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
                startActivity(loginIntent);
            }

        }
        else
        {
            if (getActivity() != null)
            {
                Intent userIntent=new Intent(getActivity(),BlogsActivity.class);
                startActivity(userIntent);
            }

        }
    }


}
