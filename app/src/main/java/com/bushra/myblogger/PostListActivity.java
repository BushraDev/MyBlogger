package com.bushra.myblogger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListActivity extends AppCompatActivity
{

    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    NavigationView navView;
    String category,title,content;
    Uri imgUri;
    Bitmap bm;
    final String MyPREFERENCES = "Bushra";
    SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    RecyclerView mRecyclerview;
    PostAdapter mAdapter;
    private TabLayout tab_layout;
    private int item_per_display = 3;
    ArrayList<Post> posts;
    private static ProgressDialog progress;
    AlertDialog.Builder builder ;
    Class mActivity = null;
    private static final String[] POSTS = new String[]{
            "Programming Posts", "Security Posts", "News Posts"
    };
    public static String single_choice_selected=POSTS[0];


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        initToolbar();
        initNavigationMenu();

        mRecyclerview=findViewById(R.id.programming_posts_recyclerview);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(PostListActivity.this));
        mRecyclerview.setHasFixedSize(true);

        setUpAdapter(single_choice_selected );
        initComponent();


        progress = new ProgressDialog(PostListActivity.this);
        progress.setMessage("Posting...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUpAdapter(single_choice_selected );
    }


    public static Intent newIntent(Context bContext) {

        Intent i = new Intent(bContext, PostListActivity.class);
        return i;

    }


    public  void setUpAdapter(String cat)
    {

        BlogLab blogLab = BlogLab.get(PostListActivity.this);
        BlogLab.VolleyListiner listiner=new BlogLab.VolleyListiner()
        {
            @Override
            public void onsucss(ArrayList<Post> postModels) {
                posts=postModels;


                if (mAdapter == null)
                {
                    mAdapter = new PostAdapter(PostListActivity.this,item_per_display,posts.subList(0,item_per_display));
                    mRecyclerview.setAdapter(mAdapter);
                    mAdapter.setOnLoadMoreListener(new PostAdapter.OnLoadMoreListener() {
                        @Override
                        public void onLoadMore(int current_page) {
                            loadNextData();
                        }
                    });
                }
                else
                {
                    mAdapter.setPosts(postModels);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        if(cat.matches(POSTS[0]))
        {
            blogLab.getProgrammingPosts(listiner,PostListActivity.this);
        }
        else if(cat.matches(POSTS[1]))
        {
            blogLab.getSecurityPosts(listiner,PostListActivity.this);
        }
        else if(cat.matches(POSTS[2]))
        {
            blogLab.getNewsPosts(listiner,PostListActivity.this);

        }


    }

    private void loadNextData() {
        mAdapter.setLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.insertData(posts.subList(0,item_per_display));
            }
        }, 1500);
    }

    void initComponent()
    {
        tab_layout =  findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_home), 0);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_notifications), 1);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_add_box), 2);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_favorite_border), 3);
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ic_person), 4);

        // set icon color pre-selected
        tab_layout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(4).getIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        showCustomDialog();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        showCustomDialog();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == R.id.filter) {
            showSingleChoiceDialog();
        }
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSingleChoiceDialog() {
        single_choice_selected = POSTS[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Posts Category");
        builder.setSingleChoiceItems(POSTS, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                single_choice_selected = POSTS[i];
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setUpAdapter(single_choice_selected);

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void initToolbar()
    {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

    }

    private void initNavigationMenu()
    {
        navView = findViewById(R.id.nav_view);
        mDrawer = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.open,R.string.close)
        {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                builder =new AlertDialog.Builder(PostListActivity.this);
                switch (menuItem.getItemId()) {
                    case R.id.log_out:
                        mDrawer.closeDrawers();
                        builder.setTitle("Log out")
                                .setMessage("Are you sure you want to log out ?")
                                .setPositiveButton("LOG OUT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ProgressDialog progress;
                                        progress = new ProgressDialog(PostListActivity.this);
                                        progress.setMessage("Logging out...");
                                        progress.setCancelable(false);
                                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progress.show();
                                        editor.remove("id");
                                        editor.remove("name");
                                        editor.remove("email");
                                        editor.remove("photoUrl");
                                        editor.commit();
                                        mActivity=LoginActivity.class;
                                        progress.dismiss();
                                        startActivity(new Intent(PostListActivity.this, mActivity));
                                        finish();

                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mDrawer.closeDrawers();
                                    }
                                })
                                .setCancelable(true);
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();
                        break;
                    default:
                        break;
                }

                return true;
            }

        });

         Menu menu_navigation= navView.getMenu();

         View navigation_header = navView.getHeaderView(0);

        CircleImageView userPhoto=navigation_header.findViewById(R.id.nav_header_imageView);
        TextView userName=navigation_header.findViewById(R.id.nav_header_uName);
        TextView userEmail=navigation_header.findViewById(R.id.nav_header_uEmail);



        userName.setText(sharedpreferences.getString("name",null ));
        userEmail.setText(sharedpreferences.getString("email",null ));
        Picasso.get().load(BlogLab.uri+sharedpreferences.getString("photoUrl",null )).into(userPhoto);

    }

    private void showCustomDialog()
    {
        final Dialog dialog = new Dialog(PostListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_post);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ImageButton pPhoto=dialog.findViewById(R.id.post_photo);
        pPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,0);
            }
        });
        ImageButton take_photo=dialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(i,1);
            }
        });

        final Spinner dropDownList = dialog.findViewById(R.id.post_category);
        ArrayList<String> speciality = new ArrayList<>();
        speciality.add("Programming");
        speciality.add("Security");
        speciality.add("News");
        ArrayAdapter<String>arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,speciality);
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
        ImageView uPhoto=dialog.findViewById(R.id.user_photo);
        Picasso.get().load(BlogLab.uri+sharedpreferences.getString("photoUrl",null )).into(uPhoto);
        final int uId=sharedpreferences.getInt("id",0 );
        TextView uName=dialog.findViewById(R.id.user_name);
        uName.setText(sharedpreferences.getString("name",null ));

        final EditText pTitle=dialog.findViewById(R.id.post_title);
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

        final EditText pContent=dialog.findViewById(R.id.post_content);
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

        TextView createPost=dialog.findViewById(R.id.create_post_btn);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                progress.show();

                if(bm==null)
                    Toast.makeText(PostListActivity.this,"please select post photo",Toast.LENGTH_SHORT).show();
                else
                {
                    if(pTitle.getText().toString().matches(""))
                        Toast.makeText(PostListActivity.this,"please insert post title",Toast.LENGTH_SHORT).show();
                    else
                    {
                        if (pContent.getText().toString().matches(""))
                            Toast.makeText(PostListActivity.this,"please insert post content",Toast.LENGTH_SHORT).show();

                                        else
                                        {
                                            final String pho=BitMapToString(bm);
                                            BlogLab.addPostVolleyListiner listener=new BlogLab.addPostVolleyListiner()
                                            {

                                                @Override
                                                public void onsucss(Boolean res) {
                                                    if(res)
                                                    {
                                                        setUpAdapter(single_choice_selected );
                                                        Toast.makeText(PostListActivity.this,"Complete posting successfully",Toast.LENGTH_SHORT).show();
                                                        progress.dismiss();

                                                    }
                                                    else if(res==false)
                                                    {
                                                        progress.dismiss();
                                                        final AlertDialog.Builder dialog=new AlertDialog.Builder(PostListActivity.this);
                                                        dialog.setMessage(  "failed posting\nplease check your connection , then try again");
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
                                            };
                                            BlogLab.get(PostListActivity.this).addPost(listener,category,title,content,date,uId,pho,PostListActivity.this);
                                        }
                                    }
                                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==1 && resultCode==RESULT_OK && data != null)
        {
            bm= (Bitmap)data.getExtras().get("data");

        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (progress!=null && progress.isShowing()){
            progress.dismiss();
        }
    }



}

