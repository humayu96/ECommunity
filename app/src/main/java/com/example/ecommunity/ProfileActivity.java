package com.example.ecommunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 70;
    private String downloadurl;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;
    DatabaseReference myref;
    FirebaseAuth myAuth;


    ImageView img_ProfileImage,img_Addbutton;
    EditText ed_fullname,ed_gender,ed_dob,ed_cnic,ed_address;
    Button btn_update;

    String username,gender,dateofbirth,cnicnumber,address,profileimage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        // Database Connection
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData").child(userID);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        img_ProfileImage = findViewById(R.id.imageview_accountprofile);
        img_Addbutton = findViewById(R.id.imageview_addimage);

        ed_fullname = findViewById(R.id.editText_fullname);
        ed_gender = findViewById(R.id.editText_gender);
        ed_dob = findViewById(R.id.editText_DOB);
        ed_cnic = findViewById(R.id.editText_cnic);
        ed_address = findViewById(R.id.editText_address);
        btn_update = findViewById(R.id.btn_updateprofile);

        img_ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        img_Addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validationDetails())
                {
                    final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                    progressDialog.setTitle("Uploading your Profile Information...");
                    progressDialog.show();

                    final String fullname = ed_fullname.getText().toString().trim();
                    final String gender = ed_gender.getText().toString().trim();
                    final String dateofbirth = ed_dob.getText().toString().trim();
                    final String cnic = ed_cnic.getText().toString().trim();
                    final String address = ed_address.getText().toString().trim();

                    if (!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(gender) && !TextUtils.isEmpty(dateofbirth) &&
                            !TextUtils.isEmpty(cnic) && !TextUtils.isEmpty(address) )
                    {
                        final StorageReference reference = storageReference .child(UUID.randomUUID().toString());
                        final UploadTask uploadTask = reference.putFile(filePath);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final DatabaseReference newPost = databaseReference.child("Profile Information");


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

                                                    newPost.child("FullName").setValue(fullname );
                                                    newPost.child("Gender").setValue(gender );
                                                    newPost.child("DateofBirth").setValue(dateofbirth );
                                                    newPost.child("CNICNumber").setValue(cnic);
                                                    newPost.child("Address").setValue(address );
                                                    newPost.child("ProfileImage").setValue(downloadurl)

                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){

                                                                        Toast.makeText(ProfileActivity.this, "PROFILE UPDATED!", Toast.LENGTH_SHORT).show();
                                                                        progressDialog.dismiss();

                                                                    }}});}
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Toast.makeText(ProfileActivity.this, "Check Your Network Connection!", Toast.LENGTH_SHORT).show();

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
                                        Toast.makeText(ProfileActivity.this, "Check Your Network Connection! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        //progressDialog.dismiss();

                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                .getTotalByteCount());
                                        progressDialog.setMessage("Updating Your Profile Information  "+(int)progress+"%");
                                        progressDialog.dismiss();

                                    }
                                });
                    }

                }
            }

        });

        // Retriving Data
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading");
//        progressDialog.show();

        myAuth = FirebaseAuth.getInstance();
        firebaseUser = myAuth.getCurrentUser();

        myref = FirebaseDatabase.getInstance().getReference("App").child("UserData").child(firebaseUser.getUid());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = dataSnapshot.child("Profile Information").child("FullName").getValue().toString();
                    gender = dataSnapshot.child("Profile Information").child("Gender").getValue().toString();
                    dateofbirth = dataSnapshot.child("Profile Information").child("DateofBirth").getValue().toString();
                    cnicnumber = dataSnapshot.child("Profile Information").child("CNICNumber").getValue().toString();
                    address = dataSnapshot.child("Profile Information").child("Address").getValue().toString();

                    profileimage = dataSnapshot.child("Profile Information").child("ProfileImage").getValue().toString();
                    Picasso.get().load(profileimage).into(img_ProfileImage);

                    ed_fullname.setText(username);
                    ed_gender.setText(gender);
                    ed_dob.setText(dateofbirth);
                    ed_cnic.setText(cnicnumber);
                    ed_address.setText(address);

                }
                else
                {
                    Toast.makeText(ProfileActivity.this,"Upload your Profile Information",Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this, "Connection Error! Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }


//    Validate Input Data
    private boolean validationDetails() {

        if (ed_fullname.getText().toString().isEmpty() ) {
            ed_fullname.setError("Enter Full Name ");
            ed_fullname.requestFocus();
            return false;
        }
        else if (ed_fullname.length() > 25) {

            ed_fullname.setError("Enter Name Less than with Minimum 25 Words ");
            return false;
        }


        if (ed_gender.getText().toString().isEmpty()) {
            ed_gender.setError("Enter Your Gender");
            ed_gender.requestFocus();

            return false;
        }
        else if (ed_gender.length() > 6 ) {
            ed_gender.setError("Please Enter Gender with Minimum 6 Words");
            return false;
        }

        if (ed_dob.getText().toString().isEmpty()) {
            ed_dob.setError("Enter Your Date of Birth");
            ed_dob.requestFocus();

            return false;
        }
        else if (ed_dob.length() > 10 ) {
            ed_dob.setError("Enter Date of Birth with Minimum 10 Digits");
            return false;
        }

        if (ed_cnic.getText().toString().isEmpty()) {
            ed_cnic.setError("Enter Your CNIC Number");
            ed_cnic.requestFocus();

            return false;
        }
        else if (ed_cnic.length() > 15 ) {
            ed_cnic.setError("Enter Valid 15 Digit CNIC Number");
            return false;
        }

        if (ed_address.getText().toString().isEmpty()) {
            ed_address.setError("Enter Your Residential Address");
            ed_address.requestFocus();

            return false;
        }
        else if (ed_address.length() > 100 ) {
            ed_address.setError("Enter Address with Minimum 100 Words");
            return false;
        }




        return true;
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose a Profile Picture"), PICK_IMAGE_REQUEST);


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
                img_ProfileImage.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
