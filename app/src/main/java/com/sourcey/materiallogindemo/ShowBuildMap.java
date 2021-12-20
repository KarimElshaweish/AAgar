package com.sourcey.materiallogindemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowBuildMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_build_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String type=getIntent().getStringExtra("type");
        String build=getIntent().getStringExtra("build");

        String price=getIntent().getStringExtra("price");
        price=price+" ريال ";

        TextView priceTextView=findViewById(R.id.price);
        priceTextView.setText(price);

        TextView txt1=findViewById(R.id.build);
        txt1.setText(build);


        TextView txt2=findViewById(R.id.type);
        txt2.setText(type);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Shared.offerKnow.getLituide(), Shared.offerKnow.getLongtuide())
                , 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Shared.offerKnow.getLituide(), Shared.offerKnow.getLongtuide()))
                .zoom(17)
                .bearing(90)
                .tilt(40)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        LatLng sydney = new LatLng(Shared.offerKnow.getLituide(), Shared.offerKnow.getLongtuide());
        mMap.addMarker(new MarkerOptions().position(sydney).title(Shared.offerKnow.getSpinnerType()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
