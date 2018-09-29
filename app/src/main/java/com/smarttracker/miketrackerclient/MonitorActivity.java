package com.smarttracker.miketrackerclient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonitorActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        spinner=findViewById(R.id.spinner);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference inputName = database.getReference("users/");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                // Get Post object and use the values to update the UI
                final List<TrackingRecord> recordList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    TrackingRecord tr = data.getValue(TrackingRecord.class);
                    recordList.add(tr);
                    LatLng beaconLocation = new LatLng(tr.getLat(), tr.getLon());
                    mMap.addMarker(new MarkerOptions().position(beaconLocation).title("Marker in " + tr.getName() + " location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(beaconLocation));
                }
                spinner.setAdapter(new ArrayAdapter<TrackingRecord>(MonitorActivity.this, android.R.layout.simple_spinner_item,recordList));
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                    new LatLng( recordList.get(position).getLat(),recordList.get(position).getLon())));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        inputName.addValueEventListener(postListener);

        // Add a marker in Sydney, Australia, and move the camera.


    }
}
