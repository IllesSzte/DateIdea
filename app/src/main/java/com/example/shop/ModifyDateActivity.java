package com.example.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ModifyDateActivity extends Activity {
    private static final String LOG_TAG = ModifyDateActivity.class.getName();
    private static final String PREF_KEY = DateListActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    EditText dateName;
    EditText datePrice;
    private DateIdea dateIdea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_date);

        dateIdea = getIntent().getParcelableExtra("date_idea");

        dateName = findViewById(R.id.editTextDateName);
        datePrice = findViewById(R.id.editTextPassword);

        dateName.setText(dateIdea.getName());
        datePrice.setText(dateIdea.getPrice());
    }

    public void modify(View view) {
        String name = dateName.getText().toString();
        String price = datePrice.getText().toString();

        // Get a reference to the Firebase Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update the data in the Firebase Firestore database
        db.collection("Dates").document(dateIdea._getId())
                .update("name", name, "price", price)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update the local copy of the data
                        dateIdea.setName(name);
                        dateIdea.setPrice(price);

                        // Finish the activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Error updating document", e);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

