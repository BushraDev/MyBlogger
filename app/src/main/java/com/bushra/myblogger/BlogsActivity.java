package com.bushra.myblogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class BlogsActivity extends AppCompatActivity
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

        final int uId = getIntent().getIntExtra(UID_EXTRA, 0);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);

        viewPager = findViewById(R.id.view_pager);

        tabLayout.setupWithViewPager(viewPager);

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

        createPost = findViewById(R.id.create_post_fbtn);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uId = getIntent().getIntExtra(UID_EXTRA, 0);
                Intent intent = PostActivity.newIntent(BlogsActivity.this, uId);
                startActivity(intent);
            }
        });

        mDrawer = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView = findViewById(R.id.nav_view);
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
                        mActivity = BlogsActivity.class;
                        setTitle(menuItem.getTitle());
                        break;
                }
                startActivity(new Intent(BlogsActivity.this, mActivity));
                mDrawer.closeDrawers();

                return true;
            }

        });




    }


    public static Intent newIntent(Context bContext, int uId) {

        Intent i = new Intent(bContext, BlogsActivity.class);
        i.putExtra(UID_EXTRA, uId);

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


}

