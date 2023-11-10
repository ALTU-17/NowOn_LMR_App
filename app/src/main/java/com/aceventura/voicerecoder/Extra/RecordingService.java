package com.aceventura.voicerecoder.Extra;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.widget.Toast;

public class  RecordingService extends Service {
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize and configure the MediaRecorder
        mediaRecorder = new MediaRecorder();
        // Set audio source, output format, encoder, etc.
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("start_recording")) {
            startScheduledRecording(intent);
        } else if (intent.getAction().equals("stop_recording")) {
            stopScheduledRecording(intent);
        }

        return START_STICKY;
    }

    private void startScheduledRecording(Intent intent) {
        // Extract the scheduled start time from the intent
        String startTime = intent.getStringExtra("start_time");
        Toast.makeText(this, "startttttttt", Toast.LENGTH_LONG).show();
        // Schedule the startRecording action using AlarmManager
        scheduleRecordingStart(startTime);
    }

    private void stopScheduledRecording(Intent intent) {
        // Extract the scheduled stop time from the intent
        String stopTime = intent.getStringExtra("stop_time");
        Toast.makeText(this, "stopppppp", Toast.LENGTH_LONG).show();
        // Schedule the stopRecording action using AlarmManager
        scheduleRecordingStop(stopTime);
    }
    private void scheduleRecordingStart(String startTime) {
        // Parse the startTime and schedule a startRecording action using AlarmManager
        // You'll need to convert the startTime string to a timestamp and calculate the time difference
        // Then, use AlarmManager to trigger the startRecording action at the scheduled time.
    }

    private void scheduleRecordingStop(String stopTime) {
        // Parse the stopTime and schedule a stopRecording action using AlarmManager
        // You'll need to convert the stopTime string to a timestamp and calculate the time difference
        // Then, use AlarmManager to trigger the stopRecording action at the scheduled time.
    }

    private void startRecording() {
        // Initialize and start audio recording using MediaRecorder.
        // Ensure you handle permissions properly.
    }

    private void stopRecording() {
        // Stop audio recording and save the recording if needed.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

