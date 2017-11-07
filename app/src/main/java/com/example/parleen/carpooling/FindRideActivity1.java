package com.example.parleen.carpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindRideActivity1 extends AppCompatActivity {
    List<PoolingDetails> employeeeList = new ArrayList<>();
    List<Details> employeeeList1 = new ArrayList<>();


    // ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride1);
        final ListView listView = (ListView) findViewById(R.id.listViewUser);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("Pooling details1");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PoolingDetails poolingDetails = dataSnapshot1.getValue(PoolingDetails.class);
                    employeeeList.add(poolingDetails);
                    Log.e("size of list", employeeeList.size() + "");
                    //Log.e("name",poolingDetails.getSeatsAvailb());

                }
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(FindRideActivity1.this, employeeeList);
                listView.setAdapter(myBaseAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(FindRideActivity1.this, FindRideActivity2.class);
                        intent.putExtra("Seats Availbl", employeeeList.get(position).getSeatsAvailb() + "");
                        intent.putExtra("Car No", employeeeList.get(position).getCarNo() + "");
                        intent.putExtra("Car Name", employeeeList.get(position).getCarName());
                        intent.putExtra("E-mail",employeeeList.get(position).getEmail());
                        intent.putExtra("Phone No",employeeeList.get(position).getPhoneNumber());
                        startActivity(intent);
                        finish();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("database error", databaseError.getMessage());
            }
        });

    }
}

