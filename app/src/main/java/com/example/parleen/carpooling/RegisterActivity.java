package com.example.parleen.carpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parleen.carpooling.Utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    final FirebaseDatabase database=FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button register;
    private EditText usernameInput;
    private EditText emailInput;
    private EditText addressInput;
    private EditText phoneInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register =(Button)findViewById(R.id.buttonRegister);

        passwordInput =(EditText)findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameInput =(EditText)findViewById(R.id.userName);
                emailInput =(EditText)findViewById(R.id.email);
                addressInput =(EditText)findViewById(R.id.address);
                phoneInput =(EditText)findViewById(R.id.phone);


               // String ab=emailInput.getText().toString();



                String getEmail = emailInput.getText().toString().trim();
                String getPassword = passwordInput.getText().toString().trim();
                callSignup(getEmail,getPassword);

            }
        });


    }

    private void callSignup(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.e("message",task.getResult().toString());
                            Toast.makeText(RegisterActivity.this, "Signup Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Details details=new Details();
                            details.setAddres(addressInput.getText().toString());
                            details.setPhonNo(Integer.parseInt(phoneInput.getText().toString().trim()));
                            details.setUserName(usernameInput.getText().toString());
                            details.setEmail(emailInput.getText().toString());
                            DatabaseReference myRef=database.getReference("details");
                            myRef.child(myRef.push().getKey()).setValue(details);
                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                            SharedPref.getInstance().setString("Email",emailInput.getText().toString().trim());
                            SharedPref.getInstance().setString("Phone",phoneInput.getText().toString().trim());
                            Toast.makeText(RegisterActivity.this, "your Account created sucessfully",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    //Set user profile
    public void setUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            /*String name = user.getDisplayName();
            String email = user.getEmail();*/
            // Uri photoUrl = user.getPhotoUrl();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameInput.getText().toString().trim()).build();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //String uid = user.getUid();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "USER PROFILE UPDATED");
                            }
                        }
                    });

        }
    }

}
