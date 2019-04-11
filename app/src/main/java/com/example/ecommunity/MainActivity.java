package com.example.ecommunity;

import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton b_TotalComplaint,b_InProgress,b_Resolved;
    private FloatingActionButton fab,fab1;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    private TextView textView;

    ImageView imageView;
    FirebaseUser firebaseUser;
    DatabaseReference myref;
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myAuth = FirebaseAuth.getInstance();
        firebaseUser = myAuth.getCurrentUser();

//        myref = FirebaseDatabase.getInstance().getReference("App").child("UserData").child(firebaseUser.getUid());
//        imageView = findViewById(R.id.nav_imageView);
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               String image = dataSnapshot.child("Profile Information").child("ProfileImage").getValue().toString();
//                Picasso.get().load(image).into(imageView);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Connection Error! Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
//
//            }
//        });


        b_TotalComplaint = (ImageButton) findViewById(R.id.btn_totalcomplaint);
        b_InProgress = (ImageButton) findViewById(R.id.btn_inprogress);
        b_Resolved  = (ImageButton)findViewById(R.id.btn_resolved);

        b_TotalComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TotalComplaints_Activity.class);
                startActivity(i);
            }
        });
        b_InProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,InProgrssComplaint_Activity.class);
                startActivity(i);
            }
        });
        b_Resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ResolvedComplaint_Activity.class);
                startActivity(i);
            }
        });
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        textView = (TextView) findViewById(R.id.textView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewComplaint_Activity.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = view.getId();
                switch (id){
                    case R.id.fab:
                        animateFAB();
                        break;
                    case R.id.textView:
                        //animateFAB();
                        break;
                    case R.id.fab1:
                        Intent intent = new Intent(MainActivity.this,NewComplaint_Activity.class);
                        startActivity(intent);
                        break;

                }

//                Intent intent = new Intent(MainActivity.this, NewComplaint_Activity.class);
//                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(this,FeedBackActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this,HistoryActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id){
//            case R.id.fab:
//
//                animateFAB();
//                break;
//            case R.id.fab1:
//                Intent op = new Intent(MainActivity.this,NewComplaint_Activity.class);
//
//                startActivity(op);
//                break;
//
//        }
//    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab1.setClickable(false);

            isFabOpen = false;

            //Log.d("ok", "close");


        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab1.setClickable(true);

            isFabOpen = true;

            //Log.d("ok", "open");

        }
    }
}
