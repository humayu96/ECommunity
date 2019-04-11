package com.example.ecommunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FeedBackActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText ed_username,ed_userfeedback;
    Button btn_feedback;
    private String savecuurentdate,savecuurenttime,randomkey;

    private RecyclerView recyclerView;
    private DatabaseReference myref;
    LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserFeedbacks");


        ed_username = (EditText) findViewById(R.id.editText_name);
        ed_userfeedback = (EditText) findViewById(R.id.editText_feedback);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate =  new SimpleDateFormat("MMM dd, yyy");
        savecuurentdate = currentdate.format(calendar.getTime());
        SimpleDateFormat currenttime =  new SimpleDateFormat("HH:mm:ss a");
        savecuurenttime = currenttime .format(calendar.getTime());
        randomkey = savecuurentdate +savecuurenttime;
        btn_feedback = (Button)findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(FeedBackActivity.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                final String name = ed_username.getText().toString().trim();
                final String feedback = ed_userfeedback.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(feedback) ){
                    final DatabaseReference newPost = databaseReference.child(randomkey);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            newPost.child("UserName").setValue(name);
                            newPost.child("Feedback").setValue(feedback).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(FeedBackActivity.this, "Feedback is Uploaded ", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();


                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FeedBackActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading");
//        progressDialog.show();

        myref= FirebaseDatabase.getInstance().getReference("App").child("UserFeedbacks");
        myref.keepSynced(true);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerAdapter<FeedBack_DataModel,BlogViewHolder> recyclerAdapter=
                new FirebaseRecyclerAdapter<FeedBack_DataModel,BlogViewHolder>(
                        FeedBack_DataModel.class,
                        R.layout.feedback_cardlayout,
                        BlogViewHolder.class,
                        myref
                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, FeedBack_DataModel model, int position) {
                        viewHolder.setUserName(model.getUserName());
                        viewHolder.setFeedback(model.getFeedback());
                        //progressDialog.dismiss();
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView textView_name;
        TextView textView_feedback;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            textView_name = (TextView)itemView.findViewById(R.id.username);
            textView_feedback = (TextView) itemView.findViewById(R.id.userfeedback);
        }
        public void setUserName(String userName)
        {
            textView_name.setText(userName);
        }
        public void setFeedback(String feedback)
        {
            textView_feedback.setText(feedback);
        }

    }
}
