package com.example.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreateDateActivity extends AppCompatActivity {
    private static final String LOG_TAG = CreateDateActivity.class.getName();
    private ArrayList<DateIdea> mItemsData;
    private DateIdeaAdapter mAdapter;
    private CollectionReference mItems;
    private FirebaseFirestore mFirestore;

    EditText dateEditText;
    EditText priceEditText;

    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_date);

        dateEditText = findViewById(R.id.editTextDateName);
        priceEditText = findViewById(R.id.editTextPassword);

        mItemsData = new ArrayList<>();
        mAdapter = new DateIdeaAdapter(this, mItemsData);
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");

    }

    public void createNewDateIdea(View view) {
        String dateName = dateEditText.getText().toString();
        String price = priceEditText.getText().toString();

        DateIdea date = new DateIdea(dateName, price, 0);
        mItems.add(date)
                .addOnSuccessListener(documentReference -> {
                    Log.d(LOG_TAG, "Item added successfully!");
                    mItemsData.add(date);
                    mAdapter.notifyItemInserted(mItemsData.size() - 1);

                    String toastMessage = "New date idea added with name: " + dateName + " and price: " + price;
                    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

                    dateEditText.setText("");
                    priceEditText.setText("");
                })
                .addOnFailureListener(e -> {
                    Log.w(LOG_TAG, "Error adding item", e);
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_LONG).show();
                });
    }
}
