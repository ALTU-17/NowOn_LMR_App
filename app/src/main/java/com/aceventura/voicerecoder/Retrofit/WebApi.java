package com.aceventura.voicerecoder.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class WebApi {

//////  TEST URL
    public static final String BASE_URL = "http://31.220.74.138:8888/";

//    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//    String apiEndpoint = sharedPreferences.getString("apiEndpoint", "default_api_endpoint");


    public static final String register = "register";
    public static final String token = "token";
    public static final String upload = "/nowon/api/v1/recording/upload";
    public static final String events = "events";
}
