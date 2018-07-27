package akhaled.shahen.com.exchangeme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Switch_Nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction transaction;
    Fragment newFragment;
    FragmentManager fragmentManager;
    Intent intent;
    String get_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_switch__nav );
        // for changing
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        intent = getIntent();
        get_email = intent.getStringExtra( "email" );

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        newFragment = new Home();
        transaction.replace(R.id.swap_all,newFragment).commit();
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Switch_Nav.this,Add_item.class ) ;
                intent.putExtra( "email",get_email );
                startActivity( intent );
            }
        } );


///////////////////////////////////////
        DrawerLayout drawer =  findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }


//////
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (id == R.id.home) {
            newFragment = new Home();
            transaction.replace(R.id.swap_all,newFragment);
            //
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.department) {
            newFragment = new Department();
            transaction.replace(R.id.swap_all,newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.profile) {
            newFragment = Profile.newInstance( get_email );
            transaction.replace(R.id.swap_all,newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
//////////
        }
        else if (id == R.id.go_evaluator) {
            newFragment = new Evaluator() ;
            transaction.replace(R.id.swap_all,newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
//////////
        }
        else if (id == R.id.about) {
            newFragment = new About();
            transaction.replace(R.id.swap_all,newFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.logout) {
            Intent intent = new Intent(Switch_Nav.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
          ///////////
        }else if(id == R.id.help){
            newFragment = new Help();
            transaction.replace(R.id.swap_all,newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
///
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
    ///
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
