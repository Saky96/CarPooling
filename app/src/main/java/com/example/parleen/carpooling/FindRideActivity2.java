package com.example.parleen.carpooling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FindRideActivity2 extends AppCompatActivity {
    TextView seats,carNo,carname,username,email,phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride2);
        seats = (TextView) findViewById(R.id.txt_main3_name);
        carNo= (TextView) findViewById(R.id.txt_main3_sal);
        carname= (TextView) findViewById(R.id.txt_main3_id);
        username= (TextView) findViewById(R.id.txt_main4_sal);
        email= (TextView) findViewById(R.id.txt_main5_sal);
        phoneNo= (TextView) findViewById(R.id.txt_main6_sal);

        seats.setText(getIntent().getStringExtra("Seats Availbl"));
        carNo.setText(getIntent().getStringExtra("Car No"));
        carname.setText(getIntent().getStringExtra("Car Name"));
        username.setText(getIntent().getStringExtra("User Name"));
        email.setText(getIntent().getStringExtra("E-mail"));
        phoneNo.setText(getIntent().getStringExtra("Phone No"));


    }
}
