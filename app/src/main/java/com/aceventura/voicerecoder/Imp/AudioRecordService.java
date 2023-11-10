package com.aceventura.voicerecoder.Imp;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.aceventura.voicerecoder.Retrofit.Utils.MultipartRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aceventura.voicerecoder.Retrofit.ApiInterface;

import com.aceventura.voicerecoder.Retrofit.Utils.SharedClass;
import com.aceventura.voicerecoder.Retrofit.WebApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AudioRecordService extends Service {
    private MediaRecorder mediaRecorder;
    DownloadManager dm;
    private boolean isRecording = false;
    ApiInterface apiService;
    private static String mFileName = null;
    String eveID,tokenSTR, DeviceSec,AndroidId,md5Hash;
    SharedClass sharedClass;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "start_recording":
                    String startTime = intent.getStringExtra("start_time");
                    Log.e("TAG", "11: ");
                    Log.e("TAG", "startTime serv : " + startTime);
                    scheduleRecordingStart(startTime);
                    break;
                case "stop_recording":
                    String stopTime = intent.getStringExtra("stop_time");
                    Log.e("TAG", "stopTime serv : " + stopTime);
                    scheduleRecordingStop(stopTime);
                    break;
            }
        }

        return START_STICKY;
    }

    private int startRecording() {
        if (!isRecording) {
            Log.e("TAG", "2: ");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());

            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

//            String filename = "Recording_" + dateFormat.format(new Date()) + ".WAV";
            mFileName += "/Download/Recording_" + dateFormat.format(new Date()) + ".wav";

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioEncodingBitRate(128000); // Set the bitrate to 128kbps
            mediaRecorder.setAudioSamplingRate(44100);
            String filePath = getOutputMediaFile(); // Define your file path
            mediaRecorder.setOutputFile(mFileName);

//            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//            Uri uri = Uri.parse(mFileName);
//            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                Log.d("AudioRecordService", "Recording started");

                if (isRecording=true){
                    handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                        }
                    });
                    return Service.START_STICKY;
                }



//                Toast.makeText(AudioRecordService.this, "Recording started", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        return 0;
    }

    private int stopRecording() {
        Log.e("TAG", "13: ");
        if (mediaRecorder != null) {
            try {
            Log.e("TAG", "stop: ");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception
            }
//            mediaRecorder = null;
//            isRecording = false;

            Log.d("AudioRecordService", "Recording stopped");
            Token();
            handler.postDelayed(apiCallRunnable, 10000);

            handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Recording Stopped and Saved on"+mFileName, Toast.LENGTH_LONG).show();
                }
            });
            return Service.START_STICKY;


//            Toast.makeText(AudioRecordService.this, "Recording stopped", Toast.LENGTH_LONG).show();
        }
        return 0;
    }

    private void scheduleRecordingStart(String startTime) {
        // Calculate the delay until the recording should start based on the provided start time.
        long delayMillis = calculateDelayMillis(startTime);
        Log.e("TAG", "1: ");
        if (delayMillis >= 0) {
            new Thread(() -> {
                Log.e("TAG", "1.1: ");
                SystemClock.sleep(delayMillis);
                startRecording();
            }).start();
        }
    }

    private void scheduleRecordingStop(String stopTime) {
        // Calculate the delay until the recording should stop based on the provided stop time.
        long delayMillis = calculateDelayMillis(stopTime);
        Log.e("TAG", "12: ");
        if (delayMillis >= 0) {
            new Thread(() -> {
                SystemClock.sleep(delayMillis);
                stopRecording();
            }).start();
        }
    }

    private long calculateDelayMillis(String targetTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date targetDate = dateFormat.parse(targetTime);
            long targetMillis = targetDate.getTime();
            long currentMillis = System.currentTimeMillis();
            long delayMillis = targetMillis - currentMillis;

            // If the target time has already passed, set a negative delay to start immediately.
            if (delayMillis < 0) {
                delayMillis = 0;
            }

            return delayMillis;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Indicates an error
        }
    }

    private String getOutputMediaFile() {
        // Implement your logic to generate a unique file path for audio recording.
        // Ensure you handle file naming and storage location appropriately.
        return null; // Replace with your generated file path
    }

    private byte[] readFileAsBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fileInputStream.read(data);
        fileInputStream.close();
        return data;
    }

    public static String calculateMD5(String filePath) {
        try {
            File audioFile = new File(filePath);
            FileInputStream fis = new FileInputStream(audioFile);
            Log.e("TAG", "1: ");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] md5sum = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : md5sum) {
                hexString.append(String.format("%02x", b));
            }

            fis.close();
            Log.e("TAG", "2: ");
            return hexString.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    Handler handler = new Handler();
    Runnable apiCallRunnable = new Runnable() {
        @Override
        public void run() {
            String filePath = mFileName;
            Log.e("TAG", "onCreate: " + filePath);
            Log.e("TAG", "3: ");
             md5Hash = calculateMD5(filePath);
            if (md5Hash != null) {
                System.out.println("MD5 Hash: " + md5Hash);
                Log.e("TAG", "onCreate111: " + md5Hash);
            } else {
                System.out.println("Error calculating MD5 hash");
                Log.e("TAG", "onCre: " + md5Hash);
            }




//            eveID = sharedClass.getString("iddd");

//            handler.postDelayed(apiCallRunnable1, 10000);
//
//
//        }
//    };
//
//
//    Runnable apiCallRunnable1 = new Runnable() {
//        @Override
//        public void run() {

//            Gson gson = new GsonBuilder().setLenient().create();
//            JsonReader jsonReader = new JsonReader(new StringReader(yourJsonString));
//            jsonReader.setLenient(true);
//            YourObject yourObject = gson.fromJson(jsonReader, YourObject.class);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebApi.BASE_URL) // Replace with your API base URL
                    .addConverterFactory(GsonConverterFactory.create()) // Use a converter factory that matches your data format
                    .build();

            // Define a Map to allow users to provide values for eventId and checksum
            Map<String, Object> eventinfoMap = new HashMap<>();
            String dd = "1234";

//            dd.replaceAll("\"", "");
            eventinfoMap.put("eventId", 1234); // Replace userProvidedEventId with the user's input
            eventinfoMap.put("checksum", md5Hash); // Replace userProvidedChecksum with the user's input

            // Convert the Map to a JSON string
            String userEventInfoJsonString = new Gson().toJson(eventinfoMap);

            userEventInfoJsonString = userEventInfoJsonString.replaceAll("\"eventId\":\"(\\d+)\"", "\"eventId\":$1");
            File file = new File(mFileName);
            // Get the name of the file
            String fileName = file.getName();
            System.out.println("  eventInfoJson: " + userEventInfoJsonString);
            System.out.println("File Name: " + fileName);

            // Create a MultipartBody.Part for the audio file
            RequestBody audioRequestBody = RequestBody.create(MediaType.parse("audio/*"), file);
            MultipartBody.Part audioFilePart = MultipartBody.Part.createFormData("audio", file.getName(), audioRequestBody);

            System.out.println("File filePart: " + mFileName);

            ApiInterface apiInterface1 = retrofit.create(ApiInterface.class);
            Log.d("TAG", "Upload time  : " + tokenSTR);
//            StringMod eventInfo = new StringMod(1234, "b98fec0d31deff5c08dc4d7742a57273");
//// Convert the JSON data to a RequestBody
//            RequestBody eventInfoRequestBody = RequestBody.create(
//                    MediaType.parse("application/json"),
//                    new Gson().toJson(eventInfo)
//            );

//            Call<StringMod>call1 =apiInterface1.Upload( RequestBody.create(MediaType.parse("text/plain"), "eventInfo"),
//                    eventInfoRequestBody,
//                    audioFilePart);
//
//            call1.enqueue(new Callback<StringMod>() {
//                @Override
//                public void onResponse(Call<StringMod> call, retrofit2.Response<StringMod> response) {
//
//                }
//
//                @Override
//                public void onFailure(Call<StringMod> call, Throwable t) {
//
//                }
//            });

//            Call<UploadMod> call = apiInterface1.Upload(tokenSTR,userEventInfoJsonString, audioFilePart);
//            call.enqueue(new Callback<UploadMod>() {
//                @Override
//                public void onResponse(Call<UploadMod> call, retrofit2.Response<UploadMod> response) {
//                    if (response.isSuccessful()) {
//                        Log.e("TAG", "onCre: " + response.body());
//                        Toast.makeText(AudioRecordService.this, "Event data successfully uploaded!", Toast.LENGTH_LONG).show();
////                        YourResponseModel result = response.body();
//                        // Handle the response data here
//                    } else {
//                        // Handle the error
//                        Toast.makeText(AudioRecordService.this, "unsuccessfull ", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<UploadMod> call, Throwable t) {
//                    Toast.makeText(AudioRecordService.this, "Uploading Fail: "+t, Toast.LENGTH_LONG).show();
//                    System.out.println("     ===>>>> : " + t);
//                }
//            });
            System.out.println("  tokenSTR   ===>>>> : " + tokenSTR);


            RequestQueue requestQueue = Volley.newRequestQueue(AudioRecordService.this);
            JSONObject eventInfoJson = new JSONObject();
            try {
                eventInfoJson.put("eventId", 123455);
                eventInfoJson.put("checksum",md5Hash );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = "http://31.220.74.138:8888/nowon/api/v1/recording/upload";
                    MultipartRequest multipartRequest = new MultipartRequest(
                    Request.Method.POST,
                            WebApi.BASE_URL+"upload",
                    response -> {
                        // Handle the API response
                        if (response.statusCode==200) {
                            String responseData = new String(response.data);
                        Log.e("TAG", "onCre: " + responseData);
                        Log.e("TAG", "eventInfoJson: " + eventInfoJson);
                        Toast.makeText(AudioRecordService.this, "Event data successfully uploaded!", Toast.LENGTH_LONG).show();
                            handler.postDelayed(refresh, 1000);
//                        YourResponseModel result = response.body();
                        // Handle the response data here
                    } else {
                        // Handle the error
                        Toast.makeText(AudioRecordService.this, "unsuccessfull ", Toast.LENGTH_SHORT).show();
                    }

                    },
                    error -> {
                        // Handle API request error
                        Toast.makeText(AudioRecordService.this, "Uploading Fail: "+error, Toast.LENGTH_LONG).show();
                    System.out.println("     ===>>>> : " + error);
                    System.out.println("  tokenSTR   ===>>>> : " + tokenSTR);
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", tokenSTR);
//                    headers.put("Sort-Order", "desc");
                    return headers;
                }
            };
// Add the eventInfo JSON as a part
            multipartRequest.addJsonPart("eventInfo", eventInfoJson);
            multipartRequest.addFilePart("file", file);
            Log.e("TAG", "eventInfoJson:11 " + file);
//            multipartRequest.addHeader("Authorization", tokenSTR);
//            Map<String, String> headers = null;
//            try {
//                headers = multipartRequest.getHeaders();
//            } catch (AuthFailureError e) {
//                throw new RuntimeException(e);
//            }
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                Log.d("RequestHeaders", entry.getKey() + ": " + entry.getValue());
//            }

            // Add the request to the request queue
            requestQueue.add(multipartRequest);



        }
        };

    Runnable refresh = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(AudioRecordService.this,Demo_second_Record.class);
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
        };

    private void Token() {
        sharedClass = SharedClass.getInstance(this);

        DeviceSec = sharedClass.getString("DeviceSec");
        AndroidId = sharedClass.getString("imei");


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


                // Handle the API response here
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors here
                if (error.equals("Authorization failed")) {
                    Toast.makeText(AudioRecordService.this, "Token Authorization failed", Toast.LENGTH_LONG).show();
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

    }

}

