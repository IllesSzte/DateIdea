package com.example.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNewDate(View view) {
        Intent intent = new Intent(this, CreateDateActivity.class);
        startActivity(intent);
    }

    public void listAllDate(View view) {
        Intent intent = new Intent(this, DateListActivity.class);
        startActivity(intent);
    }
}
