package com.aceventura.voicerecoder.Extra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.aghajari.waveanimation.AXWaveView;
import com.aceventura.voicerecoder.R;
//import com.example.voicerecoder.databinding.FragmentRecordBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_CODE = 1;
    BottomNavigationView navfrag;
//    FragmentRecordBinding binding;
    private NavController navController;
    private boolean isRecording = false;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private static String mFileName = null;
    private Handler handler;
    private long startTimeMillis;
    Button out;
    ImageButton alarmbtn;
    AXWaveView waveView;

    public RecordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        binding = FragmentRecordBinding.inflate(inflater, container, false);
//        return binding.getRoot();


        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        handler = new Handler();
//        binding.recordButton.setOnClickListener(this);
//        binding.recordListBtn.setOnClickListener(this);
        waveView = view.findViewById(R.id.wave);
        out = view.findViewById(R.id.out);
        navfrag = view.findViewById(R.id.bottom_navigation);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        alarmbtn = view.findViewById(R.id.alarmbtn);
        waveView.setVisibility(View.GONE);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);




        out.setVisibility(View.GONE);
        alarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording = false;
                            Intent intent = new Intent(getContext(), SetAlarmforRecording.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio Still recording");
                    alertDialog.setMessage("Are you sure, you want to stop the recording?");
                    alertDialog.create().show();
                } else {
                    Intent intent = new Intent(getContext(), SetAlarmforRecording.class);
                    startActivity(intent);
                }
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    //Stop Recording
                    stopRecording();

                    // Change button image and set Recording state to false
//                    binding.recordButton.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        /*  Check, which button is pressed and do the task accordingly
         */
        switch (v.getId()) {
            case R.id.record_list_btn:
                /*
                Navigation Controller
                Part of Android Jetpack, used for navigation between both fragments
                 */
                if (isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording = false;
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio Still recording");
                    alertDialog.setMessage("Are you sure, you want to stop the recording?");
                    alertDialog.create().show();
                } else {
                    navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                }
                break;

            case R.id.record_button:

                //Check permission to record audio
                if (checkPermissions()) {
                    //Start Recording
                    startRecording();

                    // Change button image and set Recording state to false
//                    binding.recordButton.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));

                }

                break;
        }
    }

    private void stopRecording() {
        //Stop Timer, very obvious

        if (mediaRecorder != null) {
//            binding.recordTimer.stop();
//            waveView.setVisibility(View.GONE);
//            out.setVisibility(View.GONE);
//            binding.recordButton.setVisibility(View.VISIBLE);
//
//
//            //Change text on page to file saved
//            binding.recordFilename.setText("Recording Stopped, File Saved : " + recordFile);

            //Stop media recorder and set it to null for further use to record new audio
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            handler.removeCallbacks(updateRecordingTime);

            String filePath = mFileName;
            Log.e("TAG", "onCreate: " + filePath);
            Log.e("TAG", "3: ");
            String md5Hash = calculateMD5(filePath);
            if (md5Hash != null) {
                System.out.println("MD5 Hash: " + md5Hash);
                Log.e("TAG", "onCreate111: " + md5Hash);
            } else {
                System.out.println("Error calculating MD5 hash");
                Log.e("TAG", "onCre: " + md5Hash);
            }
            Log.e("TAG", "4: ");
        }
    }

    private void startRecording() {
        //Start timer from 0
        waveView.setVisibility(View.VISIBLE);
        out.setVisibility(View.VISIBLE);
//        binding.recordTimer.setBase(SystemClock.elapsedRealtime());
//        binding.recordTimer.start();
//        binding.recordButton.setVisibility(View.GONE);

        //Get app external directory path
        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        //Get current date and time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();

        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
        recordFile = "Recording_" + formatter.format(now) + ".wav";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

//            String filename = "Recording_" + dateFormat.format(new Date()) + ".WAV";
        mFileName += "/Download/Recording_" + dateFormat.format(new Date()) + ".wav";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioEncodingBitRate(128000); // Set the bitrate to 128kbps
        mediaRecorder.setAudioSamplingRate(44100); // Set the sample rate to 44.1kHz
        mediaRecorder.setOutputFile(mFileName);

//        binding.recordFilename.setText("Recording, File Name : " + recordFile);

        //Setup Media Recorder for recording
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            isRecording = true;
            handler.post(updateRecordingTime);
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Start Recording
        mediaRecorder.start();
    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        if (isRecording) {
//            stopRecording();
//        }
//    }

    private Runnable updateRecordingTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {
            long currentTimeMillis = SystemClock.uptimeMillis();
            long elapsedMillis = currentTimeMillis - startTimeMillis;
            int seconds = (int) (elapsedMillis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
//            recordingTime.setText(String.format("%02d:%02d", minutes, seconds));
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };


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

    public static void createWavFileWithMD5(String md5Hash) {
        try {
            File outputFile = new File(mFileName); // Specify the path for the output .wav file
            byte[] md5Bytes = md5Hash.getBytes();
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(md5Bytes);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
