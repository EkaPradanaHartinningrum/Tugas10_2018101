package com.example.tugas5_recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tugas5_recycleview.databinding.ActivityRestApiBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class RestApi extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SessionManager session;

    //declaration variable
    private ActivityRestApiBinding binding;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityRestApiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
        //Action Bar
        dl = (DrawerLayout)findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();
        session = new SessionManager(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_presiden) {
                    Intent a = new Intent(RestApi.this, DestinationActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_provinsi) {
                    Intent a = new Intent(RestApi.this, ProvinsiActivity.class);
                    startActivity(a);
                } else if (id == R.id.nav_alarm) {
                    Intent a = new Intent(RestApi.this, MainActivity.class);
                    startActivity(a);
                }
                else if (id == R.id.nav_restapi) {
                    Intent a = new Intent(RestApi.this, RestApi.class);
                    startActivity(a);
                }
                else if (id == R.id.nav_logout) {
                    Intent a = new Intent(RestApi.this, HalamanMasuk.class);
                    session.setLogin(false);
                    startActivity(a);
                    finish();
                }
                return true;
            }
        });
    }
    //action Bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/en.wikipedia/all-access/all-agents/Tiger_King/daily/20210901/20210930")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String> {
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //get data json
        public void parseJson(String data) throws JSONException {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray cityArray =
                    jsonObject.getJSONArray("items");
            for (int i = 0; i < cityArray.length(); i++) {
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("views").toString();
                if (Sobj.equals(index)) {
                    String project = obj.get("project").toString();
                    binding.project.setText(project);
                    String article = obj.get("article").toString();
                    binding.article.setText(article);
                    String granularity = obj.get("granularity").toString();
                    binding.granularity.setText(granularity);
                    String timestamp = obj.get("timestamp").toString();
                    binding.timestamp.setText(timestamp);
                    String access = obj.get("access").toString();
                    binding.access.setText(access);
                    String agent = obj.get("agent").toString();
                    binding.agent.setText(agent);
                    String views = obj.get("views").toString();
                    binding.views.setText(views);
                    break;
                } else {
                    binding.project.setText("Not Found");
                }
            }
        }
    }
}