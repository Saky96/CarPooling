package com.example.parleen.carpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    TextView userName;
    Button signout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button button1=(Button)findViewById(R.id.buttonPool);
        Button buttonFindMyRide=(Button)findViewById(R.id.buttonFindRide);


        mAuth = FirebaseAuth.getInstance();
        userName = (TextView) findViewById(R.id.welcomeUsername);
        signout = (Button) findViewById(R.id.buttonSignout);

        //Again check if the user is already logged in or not
        if (mAuth.getCurrentUser()==null)
        {
            // user not logged in
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        //Fetch the display name of the current user
        FirebaseUser user = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser()!=null)
        {
            userName.setText("Welcome "+ user.getDisplayName());
        }

        //signout
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MainActivityPoolMyCar.class);
                startActivity(intent);
                finish();
            }
        });
        buttonFindMyRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,FindRideActivity1.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
