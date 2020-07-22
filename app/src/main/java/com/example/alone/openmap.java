package com.example.alone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class openmap extends AppCompatActivity {
    TextView temps,cityS,sunny,date;
    ImageView imgTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openmap);

        temps = findViewById(R.id.tvTemp);
        cityS = findViewById(R.id.tvCity);
        sunny = findViewById(R.id.tvSunny);
        date = findViewById(R.id.tvDate);

        find_weather();

    }

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
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
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
