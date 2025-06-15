package com.s22010040.test5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map extends AppCompatActivity implements  OnMapReadyCallback {
    private GoogleMap myMap;
    private EditText editTextAddress;
    private Button buttonShowLocation;
    private Geocoder geocoder;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        editTextAddress = findViewById(R.id.editTextAddress);
        buttonShowLocation = findViewById(R.id.buttonShowLocation);


        if (Geocoder.isPresent()) {
            geocoder = new Geocoder(this);
        } else {
            Toast.makeText(this, "Geocoder not available", Toast.LENGTH_SHORT).show();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        Button sensorButton = findViewById(R.id.sensorBtn);
        sensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, SensorActivity.class);
                startActivity(intent);
            }
        });


        buttonShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressText = editTextAddress.getText().toString().trim();
                if (!addressText.isEmpty()) {
                    showLocationOnMap(addressText);
                } else {
                    Toast.makeText(Map.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });


        checkLocationPermissions();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;


        LatLng anuradhpura = new LatLng(8.3114, 80.4037);
        myMap.addMarker(new MarkerOptions()
                .position(anuradhpura)
                .title("Anuradhpura")
                .snippet("My Hometown - Ancient Capital of Sri Lanka"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(anuradhpura, 13));

        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setMapToolbarEnabled(true);


        enableMyLocation();
    }

    private void showLocationOnMap(String address) {

        if (geocoder == null) {
            Toast.makeText(this, "Geocoding service not available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


                LatLng locationLatLng = new LatLng(latitude, longitude);


                if (myMap != null) {
                    myMap.clear();

                    // Add marker for the new location
                    myMap.addMarker(new MarkerOptions()
                            .position(locationLatLng)
                            .title("Marker in " + address)
                            .snippet("Lat: " + String.format("%.6f", latitude) +
                                    ", Lng: " + String.format("%.6f", longitude)));


                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));

                    Toast.makeText(this, "Location found: " + address, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Location not found. Try a different address.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error finding location. Check your internet connection.", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid address format. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void enableMyLocation() {
        if (myMap != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                myMap.setMyLocationEnabled(true);
                myMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                enableMyLocation();
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, "Location permission denied. Some features may not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
