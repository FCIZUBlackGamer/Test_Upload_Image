package com.example.mcst.cleanyourhome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void sign(View view) {
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));

    }

    public void regist(View view) {
        startActivity(new Intent(HomeActivity.this,RegisterActivity.class));

    }
}
