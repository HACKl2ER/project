package com.example.alone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    Button btnLogout;
    //private FirebaseDatabase firebaseDatabase;
    //private FirebaseAuth firebaseAuth;
    private TextView temps,cityS,sunny,date,percentage;
    public Button charge,Light;
    ImageView imgTemp,battery;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //private BatteryReceiver mBatteryReceiver = new BatteryReceiver();
    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference getData = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        temps = findViewById(R.id.tvTemp);
        cityS = findViewById(R.id.tvCity);
        sunny = findViewById(R.id.tvSunny);
        date = findViewById(R.id.tvDate);
        imgTemp = findViewById(R.id.imTemp);
        Light = findViewById(R.id.button3);
        percentage = findViewById(R.id.percentageLabel);
        battery = findViewById(R.id.batteryImage);
        charge = findViewById(R.id.Charging);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        find_weather();

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intToMain = new Intent(Home.this, Login.class);
                startActivity(intToMain);
            }

        });

        getData.child("/sensorValue_Mid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                double percen = (dataSnapshot.getValue(Integer.class) / 12.73 ) * 100;
                int batter = (100 - (int)percen);
                //int charge = (Math.round((batter / 100.0) * 5);
                //charg1 = ((int)(batter / 100) * 5);
                double timeCharge  = (Math.round((batter / 100.0) * 5) / 0.5);

                if (timeCharge >= 1) {
                    charge.setText((int)timeCharge +" ชม");
                } else {
                    double timess = (Math.round((((batter / 100.0) * 5) / 0.5) * 3600) / 60);
                    charge.setText((int)timess +" นาที");
                }

                if (percen >= 90) {
                    battery.setImageResource((R.drawable.b100));

                } else if (90 > percen && percen >= 65) {
                    battery.setImageResource(R.drawable.b75);

                } else if (65 > percen && percen >= 40) {
                    battery.setImageResource(R.drawable.b50);

                } else if (40 > percen && percen >= 15) {
                    battery.setImageResource(R.drawable.b25);

                } else {
                    battery.setImageResource(R.drawable.b0);

                }

                percentage.setText((int)percen + "%");




            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Home.this, "Error !!" + databaseError, Toast.LENGTH_LONG).show();
            }
        });

        getData.child("/sensorValue_Mid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Light.setText(dataSnapshot.getValue(Integer.class)+"LUX");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Home.this, "Error !!" + databaseError, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.editProfile:
                startActivity(new Intent(Home.this,Profile.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //protected void onResume() {
    //    super.onResume();
    //    registerReceiver(mBatteryReceiver, mIntentFilter);
    //}

    //@Override
    //protected void onPause() {
    //    unregisterReceiver(mBatteryReceiver);
    //    super.onPause();
    //}

    public void find_weather(){
        String url ="http://api.openweathermap.org/data/2.5/weather?q=SiSaKet,th&appid=1fbcb0a7e1d27f3649d9571a266c5ae3&units=Imperial";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String city = response.getString("name");

                    temps.setText(temp);
                    cityS.setText(city);
                    sunny.setText(description);

                    Calendar calendar = Calendar.getInstance();
                    //SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-dd-MMM");
                    String formatted_date = sdf.format(calendar.getTime());
                    date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32) /1.8000;
                    centi = Math.round(centi);
                    int i = (int)centi;
                    temps.setText(String.valueOf(i));

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

}

