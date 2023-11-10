package com.aceventura.voicerecoder.Extra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aceventura.voicerecoder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SetAlarmforRecording extends AppCompatActivity {

    Button button, scheduleButton, startTimeButton, stopTimeButton;
    private int startHour, startMinute, stopHour, stopMinute;
    private TextView digitalClock, recordingTime2;
    private Handler clockHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarmfor_recording);

        digitalClock = findViewById(R.id.digitalClock);
//        recordingTime2 = findViewById(R.id.recordingTime2);
        scheduleButton = findViewById(R.id.scheduleButton);
        startTimeButton = findViewById(R.id.startTimeButton);
        stopTimeButton = findViewById(R.id.stopTimeButton);
        clockHandler = new Handler();

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });
        stopTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleRecording();
            }
        });

        Runnable updateClockRunnable = new Runnable() {
            @Override
            public void run() {
                // Get the current time in HH:mm:ss format
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String currentTime = sdf.format(new Date());

                // Update the digital clock display
                digitalClock.setText(currentTime);

                // Schedule the next update in 1 second
                clockHandler.postDelayed(this, 1000);
            }
        };

// Start updating the digital clock
        clockHandler.post(updateClockRunnable);

//// Start the recording service
//        Intent startRecordingIntent = new Intent(this, RecordingService.class);
//        startRecordingIntent.setAction("START_RECORDING");
//        startService(startRecordingIntent);
//
//// Stop the recording service
//        Intent stopRecordingIntent = new Intent(this, RecordingService.class);
//        stopRecordingIntent.setAction("STOP_RECORDING");
//        startService(stopRecordingIntent);

    }


    private void scheduleRecording() {
        // Get the selected start and stop times (startHour, startMinute, stopHour, stopMinute)

        // Create an AlarmManager instance
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Toast.makeText(SetAlarmforRecording.this, "wwww", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "onReceive: 000");

        // Create an Intent to trigger the recording (you should customize this)
        Intent startRecordingIntent = new Intent("START_RECORDING_ACTION");
        PendingIntent startRecordingPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                startRecordingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Set up the start time for the recording
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMinute);
        long startTimeMillis = startTime.getTimeInMillis();

        // Set up the stop time for the recording
        Calendar stopTime = Calendar.getInstance();
        stopTime.set(Calendar.HOUR_OF_DAY, stopHour);
        stopTime.set(Calendar.MINUTE, stopMinute);
        long stopTimeMillis = stopTime.getTimeInMillis();
        Toast.makeText(SetAlarmforRecording.this, "qqq", Toast.LENGTH_SHORT).show();
        // Schedule the recording to start
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTimeMillis, startRecordingPendingIntent);
        Log.e("TAG", "onReceive: 111  " + startTimeMillis);

        // Create an Intent for stopping the recording
        Intent stopRecordingIntent = new Intent("STOP_RECORDING_ACTION");
        PendingIntent stopRecordingPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                stopRecordingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Schedule the recording to stop
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, stopTimeMillis, stopRecordingPendingIntent);

        // Optionally, display a message or update UI to indicate that recording is scheduled
    }

    private void showTimePickerDialog(final boolean isStartTime) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                SetAlarmforRecording.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update the start or stop time based on user selection
                        if (isStartTime) {
                            startHour = hourOfDay;
                            startMinute = minute;
                            Toast.makeText(SetAlarmforRecording.this, "" + startHour + ":" + startMinute, Toast.LENGTH_SHORT).show();
                            startTimeButton.setText(startHour + ":" + startMinute);
                        } else {
                            stopHour = hourOfDay;
                            stopMinute = minute;
                            stopTimeButton.setText(stopHour + ":" + stopMinute);
                            Toast.makeText(SetAlarmforRecording.this, "" + stopHour, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                isStartTime ? startHour : stopHour,
                isStartTime ? startMinute : stopMinute,
                false
        );
        timePickerDialog.show();
    }
}