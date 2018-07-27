package momen.shahen.com.gps_cloudbaseddonationsystemproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Server API Key:    AIzaSyCrZ62IpaVNlplujkymp4b3lsMeCe6Tjc4
    //Sender ID: 221676839457

    private TabLayout tabLayout;
    ViewPager viewPager=null;
    Database database;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new Database(this);

        viewPager =(ViewPager)findViewById(R.id.pager);
        FragmentManager fragmentManager =getSupportFragmentManager();
        viewPager.setAdapter(new pager(fragmentManager));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,Complain.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(Home.this,Settings_activity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        cursor = database.ShowData();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(Home.this,Profile.class));
        } else if (id == R.id.make_notification) {
            if (cursor.getCount()==0)
            {
                Toast.makeText(this,"No Privileges to Send",Toast.LENGTH_SHORT).show();

            }
            else {
                while (cursor.moveToNext()) {
                    if (cursor.getString(3).equals("HOSPITAL")) {
                        startActivity(new Intent(Home.this,Make_Notification1.class));
                    }else if (cursor.getString(3).equals("DONNER")){
                        Toast.makeText(this,"It's for Hospitals Only to send Reports to Donner",Toast.LENGTH_LONG).show();
                    }
                }
            }

        } else if (id == R.id.nav_slideshow) {

            if (cursor.getCount()==0)
            {
                Toast.makeText(this,"No Privileges to Send",Toast.LENGTH_SHORT).show();

            }
            else {
                while (cursor.moveToNext()) {
                    if (cursor.getString(3).equals("HOSPITAL")) {
                        Toast.makeText(this,"HOSPITAL",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this,Send_report.class));
                    }else //if (cursor.getString(3).equals("DONNER"))
                    {
                        Toast.makeText(this,"DONNER",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home.this,Reports.class));
                    }
                }
            }

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(Home.this,About.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(Home.this,MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

class pager extends FragmentPagerAdapter
{

    public pager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position==0)
        {
            fragment = new fragA();
        }
        else if(position==1)
        {
            fragment = new fragB();
        }
        else if(position==2)
        {
            fragment = new fragC();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
