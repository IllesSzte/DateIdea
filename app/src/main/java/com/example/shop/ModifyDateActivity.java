package com.example.shop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class ModifyDateActivity extends Activity {
    private static final String LOG_TAG = ModifyDateActivity.class.getName();

    private static final String PREF_KEY = DateListActivity.class.getPackage().toString();

    private SharedPreferences preferences;

    EditText dateName;
    EditText datePrice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_date);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        dateName = findViewById(R.id.editTextDateName);
        datePrice = findViewById(R.id.editTextPassword);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");
        dateName.setText(userName);
        datePrice.setText(password);

    }

    public void modifyDateIdea(DateIdea item) {
    }

    public void modifyDateIdea(View view) {
    }
}
