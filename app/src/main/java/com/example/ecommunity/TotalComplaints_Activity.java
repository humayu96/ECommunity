package com.example.ecommunity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TotalComplaints_Activity extends AppCompatActivity {
    TextView Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_complaints_);
        Count = (TextView)findViewById(R.id.textcounter);

        // in the second activity page, get the stored counter value
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int counter = preferences.getInt("counter_data", 0);
        Count.setText(String.valueOf(counter));
    }
}
