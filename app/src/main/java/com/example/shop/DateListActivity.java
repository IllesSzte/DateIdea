package com.example.shop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DateListActivity extends AppCompatActivity {
    private static final String PREF_KEY = DateListActivity.class.getPackage().toString();

    private static final String LOG_TAG = DateListActivity.class.getName();
    private FirebaseUser user;
    private int gridNumber = 1;
    private RecyclerView mRecyclerView;
    private ArrayList<DateIdea> dateIdeas;
    private DateIdeaAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private NotificationHelper mNotificationHelper;
    private AlarmManager mAlarmManager;
    private JobScheduler mJobScheduler;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));

        dateIdeas = new ArrayList<>();

        mAdapter = new DateIdeaAdapter(this, dateIdeas);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Dates");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

        mNotificationHelper = new NotificationHelper(this);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);


        setJobScheduler();
    }

    private void initializeData() {
        dateIdeas.clear();
        mItems.orderBy("name", Query.Direction.DESCENDING).addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error != null) {
                Log.w(LOG_TAG, "Listen failed.", error);
                return;
            }

            dateIdeas.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                DateIdea item = document.toObject(DateIdea.class);
                item.setId(document.getId());
                dateIdeas.add(item);
            }

            mAdapter.notifyDataSetChanged();
        });
    }



    private void queryData() {
        dateIdeas.clear();
        mItems.orderBy("name", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DateIdea item = document.toObject(DateIdea.class);
                        item.setId(document.getId());
                        dateIdeas.add(item);
                    }

                    if (dateIdeas.size() == 0) {
                        initializeData();
                        queryData();
                    }
                    mAdapter.notifyDataSetChanged();
                });
    }

    public void deleteItem(DateIdea item) {
        DocumentReference ref = mItems.document(item._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + item._getId());
                    Toast.makeText(this, "Item " + item.getName() + " is successfully deleted.", Toast.LENGTH_LONG).show();

                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(1000);

                    TextView successMessage = findViewById(R.id.delete);
                    successMessage.startAnimation(fadeOut);

                    // Itt hívjuk meg a queryData() metódust a törlés után
                    queryData();
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });
        mNotificationHelper.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setJobScheduler() {
        // SeekBar, Switch, RadioButton
        int networkType = JobInfo.NETWORK_TYPE_UNMETERED;
        Boolean isDeviceCharging = true;
        int hardDeadline = 5000; // 5 * 1000 ms = 5 sec.

        ComponentName serviceName = new ComponentName(getPackageName(), NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceName)
                .setRequiredNetworkType(networkType)
                .setRequiresCharging(isDeviceCharging)
                .setOverrideDeadline(hardDeadline);

        JobInfo jobInfo = builder.build();
        mJobScheduler.schedule(jobInfo);

    }



    public void createNewDate(MenuItem item) {
        Intent intent = new Intent(this, CreateDateActivity.class);
        startActivity(intent);
    }

    public void logOut(MenuItem item) {
        Intent intent = new Intent(this, WelcomePageActivity.class);
        startActivity(intent);
    }

    public void updateDate(DateIdea dateIdea) {
        Intent intent = new Intent(this, ModifyDateActivity.class);
        intent.putExtra("date_idea", dateIdea);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData(); // itt frissítjük az adatokat
    }
}
