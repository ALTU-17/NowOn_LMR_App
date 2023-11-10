package com.aceventura.voicerecoder.Imp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aceventura.voicerecoder.Retrofit.WebApi;
import com.aceventura.voicerecoder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminEnd extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_end);

        EditText editTextApiEndpoint = findViewById(R.id.editTextApiEndpoint);
        ImageView buttonUpdateEndpoint = findViewById(R.id.buttonUpdateEndpoint);

        editTextApiEndpoint.setText(WebApi.BASE_URL);

        buttonUpdateEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the new API endpoint
                String newApiEndpoint = editTextApiEndpoint.getText().toString();

                // Save the new API endpoint to SharedPreferences or your configuration class
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("apiEndpoint", newApiEndpoint);
                editor.apply();

                // Optionally, you can provide user feedback that the API endpoint has been updated.
                Toast.makeText(getApplicationContext(), "API Endpoint updated.", Toast.LENGTH_SHORT).show();
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String apiEndpoint = sharedPreferences.getString("apiEndpoint", "default_api_endpoint");
        Log.d("TAG", "apiEndpoint  : " + apiEndpoint);
// Use apiEndpoint to make API requests.



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.calendar1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        // Launch the HomeActivity
                        startActivity(new Intent(AdminEnd.this, Demo_second_Record.class));
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

        Intent in = new Intent(AdminEnd.this, Demo_second_Record.class);
        startActivity(in);

//        ApproveDailyTimeSheet.this.finish();
    }
}