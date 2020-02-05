package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListActivity extends AppCompatActivity
{

    private static final String UID_EXTRA = "com.bushra.myblogger.uuid";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentManager fm;
    FloatingActionButton createPost;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        initToolbar();
        initNavigationMenu();

        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        createPost = findViewById(R.id.create_post_fbtn);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uId = getIntent().getIntExtra(UID_EXTRA, 0);
                Intent intent = CreateNewPostActivity.newIntent(PostListActivity.this, uId);
                startActivity(intent);
            }
        });

    }

    private void setupViewPager(ViewPager viewPager)
    {
        fm = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                Fragment fragment = null;
                if (i == 0) {
                    fragment = new ProgrammingFragment();
                } else if (i == 1) {
                    fragment = new SecurityFragment();
                } else if (i == 2) {
                    fragment = new NewsFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = null;
                if (position == 0) {
                    title = "Programming";
                } else if (position == 1) {
                    title = "Security";
                } else if (position == 2) {
                    title = "News";
                }
                return title;
            }
        });
    }

    public static Intent newIntent(Context bContext) {

        Intent i = new Intent(bContext, PostListActivity.class);
        return i;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar()
    {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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
                Class mActivity = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_favorite:
                        mActivity = FavoritesActivity.class;
                        setTitle(menuItem.getTitle());
                        break;
                    default:
                        mActivity = PostListActivity.class;
                        break;
                }
                startActivity(new Intent(PostListActivity.this, mActivity));
                mDrawer.closeDrawers();
                return true;
            }

        });

         Menu menu_navigation= navView.getMenu();

         View navigation_header = navView.getHeaderView(0);

        CircleImageView userPhoto=navigation_header.findViewById(R.id.nav_header_imageView);
        TextView userName=navigation_header.findViewById(R.id.nav_header_uName);
        TextView userEmail=navigation_header.findViewById(R.id.nav_header_uEmail);

        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "Bushra";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        userName.setText(sharedpreferences.getString("name",null ));
        userEmail.setText(sharedpreferences.getString("email",null ));
        Picasso.get().load(BlogLab.uri+sharedpreferences.getString("photoUrl",null )).into(userPhoto);

    }


}

