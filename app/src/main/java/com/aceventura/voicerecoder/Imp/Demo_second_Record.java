package com.aceventura.voicerecoder.Imp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aceventura.voicerecoder.Retrofit.WebApi;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aceventura.voicerecoder.R;
import com.aceventura.voicerecoder.Retrofit.Utils.SharedClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Demo_second_Record extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;
    // Instantiate the RequestQueue.
    SharedClass sharedClass;
    TextView evename, prom, artist, venuetxt, dateop, datecl, progtxt;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_CODE = 1;
    ProgressBar progressBar;
    String tokenSTR, DeviceSec,AndroidId;
    ImageView imageView8;
    String artists,venue,promoter,endTime,startTime,ename;
    int id;
    // Define your Bearer Token
//    String bearerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4Njg2MjIwMzQ3ODQ4ODYiLCJpc3MiOiJub3dvbiIsImV4cCI6MTY5NzcwMzg0NH0.k6cpn54spNG8-oaKOEMdMnfHaHW3dfNFJZpjkZkc61Aaf4yFza_fD8ubAkHsiHtG6GHTw8RBMb_ZlcO39FE6Jw";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_second_record);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        prom = findViewById(R.id.prom);
        evename = findViewById(R.id.evename);
        venuetxt = findViewById(R.id.venue);
        artist = findViewById(R.id.artist);
        dateop = findViewById(R.id.dateop);
        datecl = findViewById(R.id.Doorclosing);
        imageView8 = findViewById(R.id.imageView8);
        progtxt = findViewById(R.id.progtxt);
        progressBar = findViewById(R.id.progressBar);

        sharedClass = SharedClass.getInstance(this);

        checkPermissions();

        // Check if the permission is granted, and request it if not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Demo_second_Record.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "2468",        // Your unique channel ID
//                    "nowon time",      // Human-readable channel name
//                    NotificationManager.IMPORTANCE_LOW // Importance level
//            );
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }

//        Intent intent = getIntent();
//        DeviceSec = intent.getStringExtra("DeviceSec");

        DeviceSec = sharedClass.getString("DeviceSec");
        AndroidId = sharedClass.getString("imei");

        if (!DeviceSec.isEmpty()) {
            progtxt.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("imei", AndroidId);
                jsonParams.put("deviceSecret", DeviceSec);
            } catch (JSONException e) {
                e.printStackTrace();
            }

// Create a StringRequest to send the raw parameters
            StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.BASE_URL + "token", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("TAG", "tokkkkkk  : " + response);
                    tokenSTR = response;
                    Log.d("TAG", "tokenSTR  : " + tokenSTR);

                    // Handle the API response here
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle errors here
                    if (error.equals("Authorization failed")) {
                        Toast.makeText(Demo_second_Record.this, "Authorization failed", Toast.LENGTH_LONG).show();
                    }
                }
            }) {
                @Override
                public byte[] getBody() {
                    // Convert the JSON parameters to a byte array
                    return jsonParams.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

// Add the request to the request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {



        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        // Launch the HomeActivity
//                        startActivity(new Intent(Demo_second_Record.this, Demo_second_Record.class));
                        return true;

                    case R.id.calendar1:
                        // Launch the DashboardActivity
                        startActivity(new Intent(Demo_second_Record.this, AdminAccess.class));
                        return true;


                    default:
                        return false;
                }
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date currentTime = new Date();
// Format the date and time
        String formattedTime = sdf.format(currentTime);
        Log.e("TAG", "onCreate: " + formattedTime);
//        if (formattedTime.equals("2023-10-19T15:53:00")){

        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        token();


//        if (formattedTime.equals("2023-10-20T14:20:00")) {

//// Inside your activity or another component
//        Intent startRecordingIntent = new Intent(Demo_second_Record.this, AudioRecordService.class);
//        startRecordingIntent.setAction("start_recording");
//        startRecordingIntent.putExtra("start_time", "2023-11-01T12:23:00");
//        startService(startRecordingIntent);
//
//// Corrected: Use stopRecordingIntent for stopping recording
//        Intent stopRecordingIntent = new Intent(Demo_second_Record.this, AudioRecordService.class);
//        stopRecordingIntent.setAction("stop_recording");
//        stopRecordingIntent.putExtra("stop_time", "2023-11-01T12:23:30");
//        startService(stopRecordingIntent);

//        }

        handler.postDelayed(apiCallRunnable, 1000);


    }

    private void token() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("imei", "868622034784886");
            jsonParams.put("deviceSecret", "pD4c9zFDcVu1Hh6zNA5xEl419LmjTLZ7RlQVqar-Iuw");
        } catch (JSONException e) {
            e.printStackTrace();
        }

// Create a StringRequest to send the raw parameters
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.BASE_URL + "token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "tokkkkkk  : " + response);
                tokenSTR = response;
                Log.d("TAG", "tokenSTR  : " + tokenSTR);

                // Handle the API response here
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors here
            }
        }) {
            @Override
            public byte[] getBody() {
                // Convert the JSON parameters to a byte array
                return jsonParams.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

// Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


//        RequestQueue queue = Volley.newRequestQueue(Demo_second_Record.this);
////        String url = MConstant.API_END+"/userLogin";
//        Map<String, String> params = new HashMap<String, String>();
//        Log.d("TAG", "111tokkkkkk  : ");
//        params.put("imei", "868622034784886");
//        params.put("deviceSecret", "pD4c9zFDcVu1Hh6zNA5xEl419LmjTLZ7RlQVqar-Iuw");
////        params.put("api",MConstant.API);
//        JSONObject objRegData = new JSONObject(params);
//        Log.d("TAG", "2222tokkkkkk  : " + WebApi.BASE_URL+"token");
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.POST, WebApi.BASE_URL+"token", objRegData, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("TAG", "tokkkkkk  : " + response);
////                        try {
////
////                            // handle response data
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                            //pDialog.show();
////                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        pDialog.hide();
//                    }
//                })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                return params;
//            }
//        };
//        queue.add(jsObjRequest);


    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(Demo_second_Record.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(Demo_second_Record.this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    // Create a Handler and a Runnable to make the API call
    Handler handler = new Handler();
    Runnable apiCallRunnable = new Runnable() {
        @Override
        public void run() {
            // Place your API call code here
            // This code will be executed after a 2-second delay
            RequestQueue queue = Volley.newRequestQueue(Demo_second_Record.this);
// Create a GET request with Bearer Token
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, WebApi.BASE_URL + "events", null,
                    response -> {
                        try {

                            if (response != null) {
                                // Create variables to hold the nearest event information
                                JSONObject nearestEvent = null;
                                String nearestEventName = "";
                                String nearestEventStartTime = "";
                                String nearestEventEndTime = "";
                                String promStr = "";
                                String venStr = "";
                                String artStr = "";
                                String idStr = "" ;

// Get the current date and time
                                Date currentDate = new Date();

// Initialize a SimpleDateFormat to parse the date-time format
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

// Initialize a variable to hold the smallest time difference
                                long minTimeDifference = Long.MAX_VALUE;

                                Log.d("TAG", "events: " + response);

                                // Handle the JSON array response
                                for (int i = 0; i < response.length(); i++) {
                                    // Access each object in the array
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    // Extract and use data from each object
                                     id = jsonObject.getInt("id");
                                     ename = jsonObject.getString("name");

                                     startTime = jsonObject.getString("startTime");


                                     endTime = jsonObject.getString("endTime");
                                     promoter = jsonObject.optString("promoter", ""); // Handle null values


                                     venue = jsonObject.getString("venue");


                                     artists = jsonObject.getString("artists");

                                    Log.d("TAG", "timeeee : " + ename +" "+ startTime +" " +
                                            promoter +venue);
                                    // Parse the event start time
                                    Date startTime1 = sdf.parse(endTime);

//                                if (startTime1.after(currentDate)) {
                                    // Calculate the time difference in milliseconds
                                    long timeDifference = startTime1.getTime() - currentDate.getTime();

                                    // Check if this event's start time is closer than the previous closest one
                                    if (timeDifference > 0 && timeDifference < minTimeDifference) {
                                        nearestEvent = jsonObject;
                                        minTimeDifference = timeDifference;


                                    }
                                }

                                if (nearestEvent != null) {
                                    nearestEventName = nearestEvent.getString("name");
                                    nearestEventStartTime = nearestEvent.getString("startTime");;
                                    promStr = nearestEvent.optString("promoter", "");
                                    artStr = nearestEvent.getString("artists");;
                                    idStr = String.valueOf(nearestEvent.getInt("id"));

                                    if (promStr.equals("null")) {
                                        prom.setText("Promoter: " + "   --");
                                    } else {

                                        prom.setText("Promoter: " + promStr);
                                    }

                                    if (artStr.equals("null")) {
                                        artist.setText("Artist: " + "   --");
                                    } else {

                                        artist.setText("Artist: " + artStr);
                                    }


                                    venStr = nearestEvent.getString("venue");;
                                    venuetxt.setText("Venue: " + venStr);

                                    nearestEventEndTime = nearestEvent.getString("endTime");;

                                    Log.d("TAG", "timeeee : " + nearestEventName + " " + nearestEventStartTime + " " +
                                            nearestEventEndTime + idStr);

                                    evename.setText("Event Name : " + nearestEventName);
                                    dateop.setText("Door Opening : " + nearestEventStartTime);
                                    sharedClass.setString("startTime", nearestEventStartTime);
                                    sharedClass.setString("iddd", idStr);
                                    datecl.setText("Door Closing : " + nearestEventEndTime);
//                                    artist.setText("Artist : " + artStr);

                                }
                                    Intent startRecordingIntent = new Intent(Demo_second_Record.this, AudioRecordService.class);
                                    startRecordingIntent.setAction("start_recording");
                                    startRecordingIntent.putExtra("start_time", nearestEventStartTime);
                                    startService(startRecordingIntent);

                                    // Corrected: Use stopRecordingIntent for stopping recording
                                    Intent stopRecordingIntent = new Intent(Demo_second_Record.this, AudioRecordService.class);
                                    stopRecordingIntent.setAction("stop_recording");
                                    stopRecordingIntent.putExtra("stop_time", nearestEventEndTime);
                                    startService(stopRecordingIntent);
//                                     Inside your activity or another component

                                    // Now, you can use the extracted data as needed.

                            } else {

                                Toast.makeText(Demo_second_Record.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(Demo_second_Record.this, "Restart please => " + error, Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Demo_second_Record.this,Demo_second_Record.class);
//                        startActivity(intent);
                        // Handle error here
                        error.printStackTrace();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", tokenSTR);
//                    headers.put("Sort-Order", "desc");
                    return headers;
                }
            };

// Add the request to the RequestQueue.  "Authorization", "Bearer " +
            queue.add(jsonArrayRequest);

        }
    };

    @Override
    public void onBackPressed() {
        finishAffinity();


//        ApproveDailyTimeSheet.this.finish();
    }

// Delay the API call by 2 seconds

}