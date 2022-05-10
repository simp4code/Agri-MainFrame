package com.example.agrijuan;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity {
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid ="2e07c755ee0dc74565e9a8f0cfd42937";
    DecimalFormat df = new DecimalFormat("#.##");
    TextView res;
    ImageView usericon;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView userName;
    FirebaseAuth firebaseAuth;
    CardView sell,invent;
    FloatingActionButton fab;
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        res = findViewById(R.id.temp_result);
        usericon = findViewById(R.id.user_icon);
        getWeatherDetails();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        userName = findViewById(R.id.google_name);
        sell = findViewById(R.id.sell_crops);
        invent = findViewById(R.id.app_inventory);
        fab = findViewById(R.id.addbtn);
        setupHyperlink();
        


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Inventory.class));
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser account = firebaseAuth.getCurrentUser();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || account != null){
            String pName = acct.getDisplayName();
            Uri photo = acct.getPhotoUrl();
            Picasso.with(this).load(photo).into(usericon);
            userName.setText(pName);
        }
        usericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,Profile.class));
                finish();
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,ToDo.class));
                finish();
            }
        });
        invent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,StockList.class));
                finish();
            }
        });
    }


    public void getWeatherDetails(){
        String country = "Philippines";
        String tempUrl = url +"?lat=12.8797&lon=121.7740&appid="+appid;

       StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
             Log.d("response",response);
               String output="";
               try {
                   JSONObject jsonResponse = new JSONObject(response);
                   JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                   JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                   String description = jsonObjectWeather.getString("description");
                   JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                   double temp = jsonObjectMain.getDouble("temp") -  273.15;
                   double feelslike= jsonObjectMain.getDouble("feels_like") - 273.15;
                   float pressure = jsonObjectMain.getInt("pressure");
                   int humidity = jsonObjectMain.getInt("humidity");
                   JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                   String wind = jsonObjectWind.getString("speed");
                   JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                   String clouds = jsonObjectClouds.getString("all");
                   JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                   String countryName = jsonObjectSys.getString("country");
                   String cityName = jsonResponse.getString("name");

                   output = "\n"
                           +"\n Temperature: "+df.format(temp)+ "C"
                           +"\n Humidity: "+humidity+ "%"
                           +"\n Wind Speed: "+wind+ "m/s"
                           +"\n Cloudiness: "+clouds+ "%"
                           +"\n Pressure: "+pressure+ "hPa";

                   res.setText(output);


               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
           }
       });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.activity_main_link);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}