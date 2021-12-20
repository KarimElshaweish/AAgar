package com.sourcey.materiallogindemo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class OfferShowOnMapAct extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_show_on_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Shared.putOfferOnMap.getFlit(), Shared.putOfferOnMap.getFlon()))
                .zoom(16)
                .bearing(90)
                .tilt(40)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(Shared.putOfferOnMap.getFlit(), Shared.putOfferOnMap.getFlon()),
                        new LatLng(Shared.putOfferOnMap.getSlit(), Shared.putOfferOnMap.getSlon()),
                        new LatLng(Shared.putOfferOnMap.getTlit(), Shared.putOfferOnMap.getTlon()),
                        new LatLng(Shared.putOfferOnMap.getFtlit(), Shared.putOfferOnMap.getFtlon()))
                .strokeColor(Color.parseColor("#000000")).fillColor(Color.parseColor("#26E12929"))
                .strokeWidth(2));
    }
}
