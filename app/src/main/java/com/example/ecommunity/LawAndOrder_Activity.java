package com.example.ecommunity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ecommunity.LawAndOrder_Categories.MotorwayPolice_Categories;
import com.example.ecommunity.LawAndOrder_Categories.Police_Categories;
import com.example.ecommunity.LawAndOrder_Categories.Prisons_Categories;
import com.example.ecommunity.LawAndOrder_Categories.TrafficPolice_Categories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class LawAndOrder_Activity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    String[] listitems = {"Police","Prisons","MotorWay Police","Traffic Police"};
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private String savecuurentdate,savecuurenttime,randomkey;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_and_order_);
        // Database Connection
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData").child(userID).child("Complaint Details");
        listView = (ListView) findViewById(R.id.lv_LawAndOrder);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listitems);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate =  new SimpleDateFormat("MMM dd, yyy");
                savecuurentdate = currentdate.format(calendar.getTime());
                SimpleDateFormat currenttime =  new SimpleDateFormat("HH:mm:ss a");
                savecuurenttime = currenttime .format(calendar.getTime());
                randomkey = savecuurentdate +savecuurenttime;

                final DatabaseReference newPost = databaseReference.child(randomkey);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (position == 0){
                            newPost.child("ComplaintType").setValue("Police");

                            Intent intent = new Intent(LawAndOrder_Activity.this, Police_Categories.class);
                            startActivity(intent);
                        }if (position == 1){
                            newPost.child("ComplaintType").setValue("Prisons");

                            Intent intent = new Intent(LawAndOrder_Activity.this, Prisons_Categories.class);
                            startActivity(intent);
                        }if (position == 2){
                            newPost.child("ComplaintType").setValue("Motorway Police");

                            Intent intent = new Intent(LawAndOrder_Activity.this, MotorwayPolice_Categories.class);
                            startActivity(intent);
                        }if (position == 3){
                            newPost.child("ComplaintType").setValue("Traffic Police");

                            Intent intent = new Intent(LawAndOrder_Activity.this, TrafficPolice_Categories.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
