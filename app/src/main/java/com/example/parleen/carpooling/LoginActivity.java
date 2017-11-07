package com.example.parleen.carpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    // private TextView loginError;
    private EditText userNameInput;
    private EditText emailidInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    //private TextView signup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameInput = (EditText) findViewById(R.id.userName);
        emailidInput = (EditText) findViewById(R.id.EmailId);
        passwordInput = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);

        mAuth = FirebaseAuth.getInstance();

        //Check if the user already logged in
        if (mAuth.getCurrentUser() != null) {
            // User  logged in
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }


        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getEmail = emailidInput.getText().toString().trim();
                String getPassword = passwordInput.getText().toString().trim();
                callSignIn(getEmail,getPassword);
            }
        });



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getEmail = emailidInput.getText().toString().trim();
                String getPassword = passwordInput.getText().toString().trim();
                callSignup(getEmail,getPassword);
            }
        });
*/

    }

    public void onClickLogin(View view) {
        String getEmail = emailidInput.getText().toString().trim();
        String getPassword = passwordInput.getText().toString().trim();
        if (TextUtils.isEmpty(getEmail)) {
            Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(getPassword)) {
            Toast.makeText(LoginActivity.this, "Enter your  password", Toast.LENGTH_SHORT).show();
        } else {
            callSignIn(getEmail, getPassword);
        }
    }

    public void onClickSignup(View v) {
       /* String getEmail = emailidInput.getText().toString().trim();
        String getPassword = passwordInput.getText().toString().trim();
        callSignup(getEmail,getPassword);*/

        Intent signupIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(signupIntent);
        finish();
    }



/*    private void callSignup(String email, String password) {

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
                            Toast.makeText(LoginActivity.this, "Signup Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            setUserProfile();
                            Toast.makeText(LoginActivity.this, "your Account created sucessfully",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }*/

   /* //Set user profile
    public void setUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            *//*String name = user.getDisplayName();
            String email = user.getEmail();*//*
            // Uri photoUrl = user.getPhotoUrl();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userNameInput.getText().toString().trim()).build();

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
    }*/

    //Now start signin process
    public void callSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TESTING", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Signin Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent signInIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(signInIntent);
                            finish();
                        }

                        // ...
                    }
                });
    }

}
