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

import com.example.ecommunity.MunicipalServices_Categories.Cleanliness_Categories;
import com.example.ecommunity.MunicipalServices_Categories.Encroachment_Categories;
import com.example.ecommunity.MunicipalServices_Categories.IllegalBillboards_Categories;
import com.example.ecommunity.MunicipalServices_Categories.WaterConnection_Categories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MunicipalServices_Activity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String>arrayAdapter;
    String[] listitems = {"Cleanliness","Water Connection","Encroachment","Illegal Billboards",};
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private String savecuurentdate,savecuurenttime,randomkey;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipal_services_);
        // Database Connection
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData").child(userID).child("Complaint Details");
        listView = (ListView) findViewById(R.id.lv_municipalservices);
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
                            newPost.child("ComplaintType").setValue("Cleanliness");

                            Intent intent = new Intent(MunicipalServices_Activity.this, Cleanliness_Categories.class);
                            startActivity(intent);
                        }if (position == 1){
                            newPost.child("ComplaintType").setValue("Water Connection");

                            Intent intent = new Intent(MunicipalServices_Activity.this, WaterConnection_Categories.class);
                            startActivity(intent);
                        }if (position == 2){
                            newPost.child("ComplaintType").setValue("Encroachment");

                            Intent intent = new Intent(MunicipalServices_Activity.this, Encroachment_Categories.class);
                            startActivity(intent);
                        }if (position == 3){
                            newPost.child("ComplaintType").setValue("Illegal Billboards");

                            Intent intent = new Intent(MunicipalServices_Activity.this, IllegalBillboards_Categories.class);
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
