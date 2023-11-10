package com.aceventura.voicerecoder.Imp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.aceventura.voicerecoder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminAccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_access);

        EditText editTextApiEndpoint = findViewById(R.id.editTextApiEndpoint);
        ImageView buttonUpdateEndpoint = findViewById(R.id.buttonUpdateSignin);

        buttonUpdateEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextApiEndpoint.getText().toString().equals("2468")){

                    startActivity(new Intent(AdminAccess.this, AdminEnd.class));
                }else {
                    Toast.makeText(AdminAccess.this, "Wrong Access Code!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.calendar1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        // Launch the HomeActivity
                        startActivity(new Intent(AdminAccess.this, Demo_second_Record.class));
                        return true;

                    case R.id.calendar1:
                        // Launch the DashboardActivity
//                        startActivity(new Intent(AdminEnd.this, AdminEnd.class));
                        return true;


                    default:
                        return false;
                }
            }
        });

    }
    @Override
    public void onBackPressed() {

        Intent in = new Intent(AdminAccess.this, Demo_second_Record.class);
        startActivity(in);

//        ApproveDailyTimeSheet.this.finish();
    }
}