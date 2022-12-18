package com.example.tugas5_recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.example.tugas5_recycleview.databinding.ActivityDestinationBinding;
import com.google.android.material.navigation.NavigationView;

public class DestinationActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private ActivityDestinationBinding binding;
    private SessionManager session;

    RecyclerView recylerView;
    String s1[], s2[],s3[];
    int images[] = {R.drawable.jokowi,R.drawable.bjhabibie,R.drawable.megawati,R.drawable.soeharto};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDestinationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        session = new SessionManager(getApplicationContext());
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        //action Bar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav_view = (NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_presiden) {
                    Intent a = new Intent(DestinationActivity.this, DestinationActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_provinsi) {
                    Intent a = new Intent(DestinationActivity.this, ProvinsiActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_alarm) {
                    Intent a = new Intent(DestinationActivity.this, MainActivity.class);
                    startActivity(a);
                }
                else if (id == R.id.nav_restapi) {
                    Intent a = new Intent(DestinationActivity.this, RestApi.class);
                    startActivity(a);
                }
                else if (id == R.id.nav_logout) {
                    Intent a = new Intent(DestinationActivity.this, HalamanMasuk.class);
                    session.setLogin(false);
                    startActivity(a);
                    finish();
                }
                return true;
            }
        });
        //recycle View
        recylerView = findViewById(R.id.recyclerView);
        s1 = getResources().getStringArray(R.array.presiden);
        s2 = getResources().getStringArray(R.array.deskripsi);
        s3 = getResources().getStringArray(R.array.julukan);
        PresidenAdapter appAdapter = new PresidenAdapter(this,s1,s2,s3,images);
        recylerView.setAdapter(appAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DestinationActivity.
                this,LinearLayoutManager.HORIZONTAL, false
        );
        recylerView.setLayoutManager(layoutManager);
        recylerView.setItemAnimator(new DefaultItemAnimator());
        //work manager
        final OneTimeWorkRequest request = new
                OneTimeWorkRequest.Builder(MyWorker.class).build();
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().enqueueUniqueWork("Notifikasi",
                        ExistingWorkPolicy.REPLACE, request);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        return abdt.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }
}