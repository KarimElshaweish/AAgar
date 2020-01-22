package com.sourcey.materiallogindemo.OfferEdit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

public class EditMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    boolean editMode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        checkLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final View view=findViewById(R.id.parent);
        if(!editMode) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Snackbar.make(view,getString(R.string.please_be_in_edit_mode),Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(Shared.editOffer.getLituide(), Shared.editOffer.getLongtuide());
        createMarker(location);
    }

    private void createMarker(LatLng location) {
        mMap.addMarker(new MarkerOptions().position(location).title(Shared.editOffer.getBuildingType()));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)
                .zoom(17)
                .bearing(90)
                .tilt(40)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    LatLng fLating;
    public void changeLocation(View view) {
        Button btn=(Button)view;
        if(btn.getText().toString().equals(getString(R.string.changeText))){
            mMap.setMyLocationEnabled(true);
            Snackbar.make(view, "انت الان فى وضع تغير المكان", Snackbar.LENGTH_SHORT).show();
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    fLating=latLng;
                    createMarker(latLng);
                }
            });
            btn.setText("حفظ التعديل");
        }else{
            Snackbar.make(view, "تم تعديل المكان", Snackbar.LENGTH_SHORT).show();
            Shared.editOffer.setLituide(fLating.latitude);
            Shared.editOffer.setLongtuide(fLating.longitude);
            new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  finish();
              }
            },2000);
        }
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(EditMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this,"لاتسطيع العمل بدون GPS",Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

}
