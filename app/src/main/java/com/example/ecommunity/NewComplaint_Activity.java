package com.example.ecommunity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

public class NewComplaint_Activity extends AppCompatActivity {

    GridView gridView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String savecuurentdate,savecuurenttime,randomkey;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint_);
       // Database Connection
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData").child(userID).child("Complaint Details");
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentdate =  new SimpleDateFormat("MMM dd, yyy");
                savecuurentdate = currentdate.format(calendar.getTime());
                SimpleDateFormat currenttime =  new SimpleDateFormat("HH:mm:ss a");
                savecuurenttime = currenttime .format(calendar.getTime());
                randomkey = savecuurentdate +savecuurenttime;

//


                final DatabaseReference newPost = databaseReference.child(randomkey);
                databaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (position == 0){
                            newPost.child("ComplaintDept").setValue("Citizan Rights");
                            Intent intent = new Intent(NewComplaint_Activity.this,CitizanRights_Activity.class);
                            startActivity(intent);
                        }if (position == 1){
                            newPost.child("ComplaintDept").setValue("Education");

                            Intent intent = new Intent(NewComplaint_Activity.this,Education_Activity.class);
                            startActivity(intent);
                        }if (position == 2){
                            newPost.child("ComplaintDept").setValue("Energy Power");

                            Intent intent = new Intent(NewComplaint_Activity.this,EnergyPower_Activity.class);
                            startActivity(intent);
                        }if (position == 3){
                            newPost.child("ComplaintDept").setValue("Law And Order");

                            Intent intent = new Intent(NewComplaint_Activity.this,LawAndOrder_Activity.class);
                            startActivity(intent);
                        }if (position == 4){
                            newPost.child("ComplaintDept").setValue("Municipal Services");
                            Intent intent = new Intent(NewComplaint_Activity.this,MunicipalServices_Activity.class);
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
    public class GridViewAdapter extends BaseAdapter {

        private Context mContext;

        public GridViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {

                R.drawable.citizanrights,
                R.drawable.education,
                R.drawable.energyandpower,
                R.drawable.lawandorder,
                R.drawable.municipalservices




        };
    }
}
