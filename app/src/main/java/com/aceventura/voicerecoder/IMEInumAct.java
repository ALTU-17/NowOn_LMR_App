package com.aceventura.voicerecoder;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aceventura.voicerecoder.Extra.MainActivity;
import com.aceventura.voicerecoder.Retrofit.Utils.SaveSharedPreference;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aceventura.voicerecoder.Imp.Demo_second_Record;
import com.aceventura.voicerecoder.Retrofit.ApiInterface;
import com.aceventura.voicerecoder.Retrofit.Modals.DeviceInfo;
import com.aceventura.voicerecoder.Retrofit.Modals.RegisterMod;
import com.aceventura.voicerecoder.Retrofit.Utils.SharedClass;
import com.aceventura.voicerecoder.Retrofit.WebApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IMEInumAct extends AppCompatActivity {
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private ImageView getImeiButton;
    FloatingActionButton floatingActionButton, floatingActionButton2;
    TextInputEditText imeii;
    TextView imei, modelname, build, androidver, set;
    String model = Build.MODEL;
    String buildNumber = Build.ID;
    String imeistr, secret, tokenSTR, androidId;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private static final int PERMISSION_CODE = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 111;
    SharedClass sharedClass;
    ApiInterface apiService;
    ApiInterface apiInterface;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imeinum);
        getImeiButton = findViewById(R.id.getImeiButton);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        imeii = findViewById(R.id.imeii);
        set = findViewById(R.id.set);

        imei = findViewById(R.id.imei);
        build = findViewById(R.id.build);
        androidver = findViewById(R.id.androidver);
        modelname = findViewById(R.id.modelname);

        getDeviceID(IMEInumAct.this);
//        getToken();
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);

        sharedClass = SharedClass.getInstance(this);
        checkPermissions();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

//        getIMEINumber();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IMEInumAct.this, MainActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IMEInumAct.this, Demo_second_Record.class);
                startActivity(intent);
            }
        });
        String androidVersion = getAndroidVersion();
        Log.e("TAG", "onCreate: " + androidVersion);
        Log.e("TAG", "onCreate: " + buildNumber);
        Log.e("TAG", "onCreate: " + model);

        androidver.setText("Android Version : : " + androidVersion);
        build.setText("Build Number : : " + buildNumber);
        modelname.setText("Model : : " + model);
        imei.setText("Device ID : : " + androidId);

        // Check if the app has permission to access the phone state
        if (ContextCompat.checkSelfPermission(IMEInumAct.this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted; request it from the user
            ActivityCompat.requestPermissions(IMEInumAct.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            // Permission is already granted; get the IMEI number
//            getIMEINumber();
            Log.e("TAG", "btn: " + androidVersion);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebApi.BASE_URL) // Replace with your API base URL
                .addConverterFactory(GsonConverterFactory.create()) // Use a converter factory that matches your data format
                .build();

        // Get the Android ID


        DeviceInfo deviceInfo = new DeviceInfo();



        deviceInfo.setImei(androidId);
        deviceInfo.setModel(model);
        deviceInfo.setBuildNumber(buildNumber);
        deviceInfo.setAndroidVersion(androidVersion);

        if (SaveSharedPreference.getLoggedStatus(this)) {
            Intent intent = new Intent(getApplicationContext(), Demo_second_Record.class);
            startActivity(intent);
        }

        // Set a click listener for the button
        getImeiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  final RegisterMod registerMod = new RegisterMod("868622034784886","Redmi Note 5 Pro","PKQ1.180904.001","Android 9 (SDK 28, REL)");
                Log.e("TAG", "11111@: " + androidVersion + model + buildNumber + imeistr);

                ApiInterface apiInterface1 = retrofit.create(ApiInterface.class);

                Call<RegisterMod> call = apiInterface1.sendDeviceData(deviceInfo);
                call.enqueue(new Callback<RegisterMod>() {
                    @Override
                    public void onResponse(Call<RegisterMod> call, Response<RegisterMod> response) {

                        Log.e("TAG", "btn: " + androidVersion + model + buildNumber + imeistr);
                        Log.d("mydata001", "REg" + new Gson().toJson(response.body()));
                        Log.d("TAG", "onResponse: " + imeii.getText().toString());

//                        sharedClass.setString("androidVersion", androidVersion);
//                        sharedClass.setString("model", model);
//                        sharedClass.setString("buildNumber", buildNumber);
//                        sharedClass.setString("androidId", androidId);


                        RegisterMod registerMod = response.body();
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getMessage().equals("Device already registered!")) {
//                                Intent intent = new Intent(IMEInumAct.this, Demo_second_Record.class);
//                                        .putExtra("DeviceSec", "pD4c9zFDcVu1Hh6zNA5xEl419LmjTLZ7RlQVqar-Iuw"));
                                sharedClass.setString("DeviceSec", "pD4c9zFDcVu1Hh6zNA5xEl419LmjTLZ7RlQVqar-Iuw");
                                sharedClass.setString("imei", "868622034784886");
//                                startActivity(intent);

                                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                                Intent intent1 = new Intent(getApplicationContext(), Demo_second_Record.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent1);
                            } else {


                                sharedClass.setString("secret", registerMod.getSecret());
                                secret = registerMod.getSecret();
                                Log.d("TAG", "secret: " + secret);
                                Toast.makeText(IMEInumAct.this, "" + registerMod.getMessage(), Toast.LENGTH_SHORT).show();
                                set.setText("Wait for Admin Approval");
//                                Intent intent = new Intent(IMEInumAct.this, Demo_second_Record.class);
                                sharedClass.setString("DeviceSec", secret);
                                sharedClass.setString("imei", androidId);
//                                startActivity(intent);
                                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                                Intent intent = new Intent(getApplicationContext(), Demo_second_Record.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterMod> call, Throwable t) {

                        Toast.makeText(IMEInumAct.this, "Throwable " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }

    private void getToken() {


        JSONObject jsonParams = new JSONObject();
        try {
//            String getSec = sharedClass.getString("secret");
            jsonParams.put("imei", androidId);
            jsonParams.put("deviceSecret", "NTq-UjFHEz24pAOwGO5kl0NBaZhFFCmqB86N309PuWo");
            Log.d("TAG", "para  : " + androidId);
            Log.d("TAG", "para  : " + secret);
        } catch (JSONException e) {
            e.printStackTrace();
        }

// Create a StringRequest to send the raw parameters ###%% DicSecrate  NTq-UjFHEz24pAOwGO5kl0NBaZhFFCmqB86N309PuWo
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebApi.BASE_URL + "token", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Log.d("TAG", "tokkkkkk  : " + response);
                    tokenSTR = response;
                    Log.d("TAG", "tokenSTR  : " + tokenSTR);
                    Toast.makeText(IMEInumAct.this, "Device Approved", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(IMEInumAct.this, Demo_second_Record.class);
                    startActivity(intent);

                    // Handle the API response here
                } else {

                    Toast.makeText(IMEInumAct.this, "Device not Approved yet", Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(IMEInumAct.this, error+ " Device not Approved yet", Toast.LENGTH_LONG).show();
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

//        Api.getInfo().token("868622034784886","pD4c9zFDcVu1Hh6zNA5xEl419LmjTLZ7RlQVqar-Iuw")
//                .enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d("mydata001", "Token" + new Gson().toJson(response.body()));
//                if (response.isSuccessful()) {
////                    StringMod stringResponse = response.body();
////                    String stringValue = stringResponse.getStringValue();
//                    Log.d("TAG", "res tok   : " + response);
//
//                    Intent intent = new Intent(IMEInumAct.this,MainActivity.class);
//                    startActivity(intent);
//
//                    // Handle the string value
//                } else {
//                    // Handle the API error
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("TAG", "err tok   : " + t);
//            }
//        });
//


    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_READ_PHONE_STATE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, get the IMEI number
//                getIMEINumber();
//            } else {
//                // Permission denied; handle this case as needed
//            }
//        }
//    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String codename = Build.VERSION.CODENAME;

        return "Android " + release + " (SDK " + sdkVersion + ", " + codename + ")";
    }


    private void getIMEINumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Use getImei() for Android 10 and higher
                imeistr = telephonyManager.getImei();
                Toast.makeText(this, "" + imeistr, Toast.LENGTH_LONG).show();
                imeii.setText(imeistr);
                Log.d("TAG", "1: " + imeistr);
                // Use imei as needed
            } else {
                // Use getDeviceId() for Android 9 (Pie) and lower
                imeistr = telephonyManager.getDeviceId();
                // Use imei as needed
                Toast.makeText(this, "" + imeistr, Toast.LENGTH_LONG).show();
                imeii.setText(imeistr);
                Log.d("TAG", "2: " + imeistr);
            }
        } else {
            // Handle the case where TelephonyManager is not available on the device
        }
    }

    public String getDeviceID(Context context) {
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("TAG", "getDeviceID: " + androidId);
        return androidId;
    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(IMEInumAct.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(IMEInumAct.this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
}