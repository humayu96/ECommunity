package com.example.ecommunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class SubmitComplaints_Activity extends AppCompatActivity {

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String downloadurl;
    EditText ed_ComplaintDetails,ed_ComplaintLocation,ed_PostalAddress;
    Button button_attachements,button_Submit;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    ImageView imageView;
    private SharedPreferences preferences;

    private String savecuurentdate,savecuurenttime,randomkey;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_complaints_);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        // Database Connection
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData").child(userID);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ed_ComplaintDetails = findViewById(R.id.ed_ComplaintDetails);
        ed_ComplaintLocation = findViewById(R.id.ed_ComplaintLocation);
        ed_PostalAddress = findViewById(R.id.ed_postaladdress);
        button_attachements = findViewById(R.id.btn_attachments);
        button_Submit = findViewById(R.id.btn_submit);

        imageView = findViewById(R.id.image_attachment);



        button_attachements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();
            }
        });

        button_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationDetails())
                {
                    final ProgressDialog progressDialog = new ProgressDialog(SubmitComplaints_Activity.this);
                    progressDialog.setTitle("Uploading your Complaint...");
                    progressDialog.show();

                    final String ComplaintDetails = ed_ComplaintDetails.getText().toString().trim();
                    final String ComplaintLocation = ed_ComplaintLocation.getText().toString().trim();
                    final String PostalAddress = ed_PostalAddress.getText().toString().trim();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentdate =  new SimpleDateFormat("MMM dd, yyy");
                    savecuurentdate = currentdate.format(calendar.getTime());
                    SimpleDateFormat currenttime =  new SimpleDateFormat("HH:mm:ss a");
                    savecuurenttime = currenttime .format(calendar.getTime());
                    randomkey = savecuurentdate +savecuurenttime;

                    if (!TextUtils.isEmpty(ComplaintDetails) && !TextUtils.isEmpty(ComplaintLocation) && !TextUtils.isEmpty(PostalAddress) )
                    {

                        final StorageReference reference = storageReference .child(UUID.randomUUID().toString());
                        final UploadTask uploadTask = reference.putFile(filePath);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final DatabaseReference newPost = databaseReference.child(randomkey).child("Complaint Details");


                                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful())
                                        {
                                            throw task.getException();
                                        }

                                        downloadurl =reference.getDownloadUrl().toString();
                                        return reference .getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        if (task.isSuccessful()){
                                            downloadurl = task.getResult().toString();
                                            databaseReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    newPost.child("ComplaintDetails").setValue(ComplaintDetails );
                                                    newPost.child("ComplaintLocation").setValue(ComplaintLocation);
                                                    newPost.child("PostalAddress").setValue(PostalAddress);
                                                    newPost.child("Image").setValue(downloadurl)

                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        // retrieve the value of counter from Shared Preference
                                                                        preferences = PreferenceManager.getDefaultSharedPreferences(SubmitComplaints_Activity.this);
                                                                        int counter = preferences.getInt("counter_data", 0);
                                                                        counter++;

                                                                        // store the value of counter after incrementing it by 1
                                                                        SharedPreferences.Editor edit = preferences.edit();
                                                                        edit.putInt("image_data", counter);
                                                                        edit.commit();

                                                                        Intent intent = new Intent(SubmitComplaints_Activity.this, MainActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(intent);
                                                                    }}});}
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SubmitComplaints_Activity.this, "Check Your Network Connection! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                .getTotalByteCount());
                                        progressDialog.setMessage("Complaint is Submitted "+(int)progress+"%");
                                    }
                                });
                    }

                }
            }

        });

    }

    private boolean validationDetails() {

        if (ed_ComplaintDetails.getText().toString().isEmpty() ) {
            ed_ComplaintDetails.setError("Enter Complaint Details ");
            ed_ComplaintDetails.requestFocus();
            return false;
        }
        else if (ed_ComplaintDetails.length() > 1000) {

            ed_ComplaintDetails.setError("Please Enter Detail Less than 1000 Words ");
            return false;
        }


        if (ed_ComplaintLocation.getText().toString().isEmpty()) {
            ed_ComplaintLocation.setError("Enter Your Complaint Location First");
            ed_ComplaintLocation.requestFocus();

            return false;
        }
        else if (ed_ComplaintLocation.length() > 100 ) {
            ed_ComplaintLocation.setError("Please Enter Detail Less than 100 Words");
            return false;
        }

        if (ed_PostalAddress.getText().toString().isEmpty()) {
            ed_PostalAddress.setError("Enter Your Complaint Location First");
            ed_PostalAddress.requestFocus();

            return false;
        }
        else if (ed_PostalAddress.length() > 100 ) {
            ed_PostalAddress.setError("Please Postal Address Less than 100 Words");
            return false;
        }



        return true;
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Picture"), PICK_IMAGE_REQUEST);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
