package com.example.ecommunity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText edit_phone,edit_code;
    Button get_code,put_code;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String codeSent;
    CountryCodePicker countryCodePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(this, MainActivity.class));

            finish();
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("App").child("UserData");
        firebaseAuth = FirebaseAuth.getInstance();

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        edit_phone = (EditText) findViewById(R.id.phonenumber_ed);
        edit_code = (EditText) findViewById(R.id.verificationode_ed);
        put_code = (Button)findViewById(R.id.login_button);
        countryCodePicker.registerCarrierNumberEditText(edit_phone);

        put_code.setEnabled(false);
        edit_code.setEnabled(false);

        get_code = (Button)findViewById(R.id.verificationcode_button);
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
                PhoneNumberValidation();
                Toast.makeText(LoginActivity.this,"Code Sent",Toast.LENGTH_LONG).show();
            }
        });

        put_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
                VerifyCodeValidation();
            }
        });

    }



    private void verifySignInCode(){
        String code_phone = edit_code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code_phone);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {



            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    put_code.setEnabled(false);
                    edit_code.setEnabled(false);
                    final String phone = edit_phone.getText().toString().trim();

                    String userID = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    }
                    assert userID != null;
                    DatabaseReference databaseReference1 = databaseReference.child(userID);
                    databaseReference1.child("Phone").setValue(phone);

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_LONG).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(getApplicationContext(),
                                "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void sendVerificationCode(){

        String number = countryCodePicker.getFullNumberWithPlus();




        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeoutn
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
            put_code.setEnabled(true);
            edit_code.setEnabled(true);
        }
    };

    private boolean PhoneNumberValidation() {


        if (edit_phone.getText().toString().isEmpty()) {
            edit_phone.setError("Enter Your Phone Number First ");
            edit_phone.requestFocus();

            return false;
        }
        else if (edit_phone.length() > 11 ) {
            edit_phone.setError("Please enter a Valid Number");
            return false;
        }
        else if (edit_phone.length() < 11 ) {
            edit_phone.setError("Please enter a Valid 11 digit Number");
            return false;
        }

        return true;
    }



    private boolean VerifyCodeValidation(){
        if ((edit_code.getText().toString().isEmpty()  ) ) {
            edit_code.setError("Enter Verification Code");
            edit_code.requestFocus();
            return false;
        }
        else if (edit_code.length() > 6 ) {
            edit_code.setError("Please Enter a 6 Digit Verification Code");
            return false;
        }
        else if (edit_code.length() < 6 ) {
            edit_code.setError("Please Enter a 6 Digit Verification Code");
            return false;
        }
        return true;
    }


}
