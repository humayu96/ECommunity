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

import com.example.ecommunity.CitizanRights_Categories.ConsumerRights_Categories;
import com.example.ecommunity.CitizanRights_Categories.DenialofRights_Categories;
import com.example.ecommunity.CitizanRights_Categories.RighttoInformation_Categories;
import com.example.ecommunity.CitizanRights_Categories.RighttoServices_Categories;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class CitizanRights_Activity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String>arrayAdapter;
    String[] listitems = {"Consumer Rights","Right to Services","Right to Information","Denial of Rights"};
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String savecuurentdate,savecuurenttime,randomkey;
    Intent intent ;
    String url;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizan_rights_);
        // Database Connection
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData").child(userID).child("Complaint Details");

        listView = (ListView) findViewById(R.id.lv_citizanrights);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listitems);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                if (position == 0){
//
//                    HashMap<String,String> post = new HashMap<String, String>();
//                    post.put("ComplaintType","Consumer Rights");
//                    //databaseReference.child("ComplaintType").setValue("Consumer Rights");
//                    databaseReference.setValue(post);
//
//                    Intent intent = new Intent(CitizanRights_Activity.this,ConsumerRights_Categories.class);
//                    startActivity(intent);
//                }
//                if (position == 1){
//
//                    HashMap<String,String> post = new HashMap<String, String>();
//                    post.put("ComplaintType","Right to Services");
//                    //databaseReference.child("ComplaintType").setValue("Right to Services");
//                    databaseReference.setValue(post);
//
//                    Intent intent = new Intent(CitizanRights_Activity.this,RighttoServices_Categories.class);
//                    startActivity(intent);
//                }

//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat currentdate =  new SimpleDateFormat("MMM dd, yyy");
//                savecuurentdate = currentdate.format(calendar.getTime());
//                SimpleDateFormat currenttime =  new SimpleDateFormat("HH:mm:ss a");
//                savecuurenttime = currenttime .format(calendar.getTime());
//                randomkey = savecuurentdate +savecuurenttime;

                final DatabaseReference newPost = databaseReference.child(url);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (position == 0){
                            Intent intent = new Intent(CitizanRights_Activity.this, ConsumerRights_Categories.class);
                            newPost.child("ComplaintType").setValue("Consumer Rights");

                            startActivity(intent);
                        }if (position == 1){
                            newPost.child("ComplaintType").setValue("Right to Services");

                            Intent intent = new Intent(CitizanRights_Activity.this, RighttoServices_Categories.class);
                            startActivity(intent);
                        }if (position == 2){
                            newPost.child("ComplaintType").setValue("Right to Information");
                            Intent intent = new Intent(CitizanRights_Activity.this, RighttoInformation_Categories.class);
                            startActivity(intent);
                        }if (position == 3){
                            newPost.child("ComplaintType").setValue("Right to Information");
                            Intent intent = new Intent(CitizanRights_Activity.this, DenialofRights_Categories.class);
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
