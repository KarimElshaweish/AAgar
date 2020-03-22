package com.sourcey.materiallogindemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActiveOrderActivity extends AppCompatActivity implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    FloatingActionButton addOfferFloationAction;
    TextView noOffer;
    ProgressBar pb;
    RecyclerView rv;
    List<OfferResult> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferNeeded");
    public void openMap() {
        Shared.AddRandomButton=true;
        Shared.addOfferNewRandom=true;
        Shared.random=true;
        startActivity(new Intent(this, MapsActivity.class));
    }
    private void getData(){
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("OfferResult").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    if (dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for(DataSnapshot dt1:dt.getChildren()) {
                            list.add(dt1.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class));
                        }
                    }
                }
                if(list.size()==0){
                    pb.setVisibility(View.GONE);
                    noOffer.setVisibility(View.VISIBLE);
                }

                offerAdapter Adapter = new offerAdapter(ActiveOrderActivity.this, list);
                rv.setAdapter(Adapter);
                pb.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void __init__(){
        addOfferFloationAction=findViewById(R.id.addOffer);
        addOfferFloationAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });
        rv=findViewById(R.id.rv1);
        pb=findViewById(R.id.pb);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.MULTIPLY);
        noOffer=findViewById(R.id.noOffer);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getData();
        Shared.owner=true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);
        __init__();
    }
}
