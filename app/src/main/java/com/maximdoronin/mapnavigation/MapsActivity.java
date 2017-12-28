// Copyright (c) 2017. All rights reserved.
// Author: Maxim Doronin <maximdoronin@yandex-team.ru>

package com.maximdoronin.mapnavigation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerManager mMarkerManager;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_screen) {
            mMarkerManager.clearScreen();
            return true;
        } else if (id == R.id.restore_locations) {
            mMarkerManager.restoreLocations();
            return true;
        } else if (id == R.id.clear_store) {
            mMarkerManager.clearStore();
            return true;
        } else if (id == R.id.save_my_location) {
            Criteria criteria = new Criteria();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            Location location = mLocationManager.getLastKnownLocation(mLocationManager
                    .getBestProvider(criteria, false));
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            createDialogAndSave(new LatLng(latitude, longitude));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createDialogAndSave(final LatLng latLng) {
        final EditText et = new EditText(MapsActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setView(et)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String input = et.getText().toString();
                        mMarkerManager.addMarker(new Marker(latLng, input));
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMarkerManager = new MarkerManager(this, mMap);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                createDialogAndSave(latLng);
            }
        });
    }
}
