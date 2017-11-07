package com.example.parleen.carpooling;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.parleen.carpooling.Utils.SharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;



public class MainActivityPoolMyCar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, DirectionFinderListener {
    final FirebaseDatabase database=FirebaseDatabase.getInstance();
    Calendar dateTime= Calendar.getInstance();
    DateFormat dateFormat=DateFormat.getDateTimeInstance();

    private TextView textViewTime,textViewDate;
    private Button buttonTime,buttonDate;
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText carNo1=(EditText)findViewById(R.id.carNo1);
                EditText carName1=(EditText)findViewById(R.id.carName1);
                EditText seatsAvailable1=(EditText)findViewById(R.id.seats);
                TextView  textViewTime=(TextView) findViewById(R.id.textTime);
                etOrigin = (EditText) findViewById(R.id.etOrigin);
                etDestination = (EditText) findViewById(R.id.etDestination);




                PoolingDetails poolingDetails=new PoolingDetails();
                poolingDetails.setCarNo(Integer.parseInt(carNo1.getText().toString()));
                poolingDetails.setSeatsAvailb(Integer.parseInt(seatsAvailable1.getText().toString()));
                poolingDetails.setCarName(carName1.getText().toString());
                poolingDetails.setTimes(textViewTime.getText().toString());
                poolingDetails.setStartngPoint(etOrigin.getText().toString());
                poolingDetails.setDestintnPoint(etDestination.getText().toString());
                poolingDetails.setEmail(SharedPref.getInstance().getString("Email"));
                poolingDetails.setPhoneNumber(SharedPref.getInstance().getString("Phone"));
                DatabaseReference myRef=database.getReference("Pooling details1");
                myRef.child(myRef.push().getKey()).setValue(poolingDetails);
                Intent intent=new Intent(MainActivityPoolMyCar.this,HomeActivity.class);
                startActivity(intent);





            }
        });



        //region DATE AND TIME
        textViewTime=(TextView) findViewById(R.id.textTime);
        textViewDate=(TextView) findViewById(R.id.textDate);
        buttonTime=(Button) findViewById(R.id.buttonTime);
        buttonDate=(Button) findViewById(R.id.buttonDate);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTime();
            }
        });
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });
        updateTextLable();
        updateTextLable1();
        //endregion


        //region MAPS
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        //endregion




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }
    //region DATE AND TIME
    private void updateDate(){
        new DatePickerDialog(this, d,dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateTime(){
        new TimePickerDialog(this , t,dateTime.get(Calendar.HOUR_OF_DAY),dateTime.get(Calendar.MINUTE),true).show();
    }
    DatePickerDialog.OnDateSetListener d= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR ,year);
            dateTime.set(Calendar.MONTH,monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateTextLable1();
        }
    };
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateTextLable();
        }
    };

    public void updateTextLable(){
        textViewDate.setText(dateFormat.format(dateTime.getTime()));

    }
    public void updateTextLable1(){
        textViewTime.setText(dateFormat.format(dateTime.getTime()));
    }
    //endregion

    //region MAP*******************************************************************************************************
    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ambala = new LatLng(30.3781788, 76.7766974);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ambala, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("welcome to ambala")
                .position(ambala)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
    //endregion*******************************************************************************************
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;

    }*/
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_signOut) {
            Intent intent1 = new Intent(this,RegisterActivity.class);
            this.startActivity(intent1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
       switch (item.getItemId()){
           case R.id.nav_History:
               fragmentTransaction=getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.main_container,new History());
               fragmentTransaction.commit();
               getSupportActionBar().setTitle("History");
               item.setChecked(true);
               break;
           case R.id.nav_Profile:
               fragmentTransaction=getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.main_container,new MyProfile());
               fragmentTransaction.commit();
               getSupportActionBar().setTitle("Profile");
               item.setChecked(true);
               break;
           case R.id.nav_Setting:
               fragmentTransaction=getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.main_container,new Settings());
               fragmentTransaction.commit();
               getSupportActionBar().setTitle("Settings");
               item.setChecked(true);

               break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

