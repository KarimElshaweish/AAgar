package com.sourcey.materiallogindemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Model.Offer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyOfferNeeded extends AppCompatActivity {

    public void Finish(View view){
        finish();
    }
    RecyclerView rv;
    TextView noOffer;
    List<Offer>list;
    public void OpenAddOffer(View view){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }else
        startActivity(new Intent(this,Add_Offers.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offer_needed);
        noOffer=findViewById(R.id.noOffer);
        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Shared.customer=true;
        FirebaseDatabase.getInstance().getReference("OfferNeeded").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                Shared.keyList=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Offer offer=dt.getValue(Offer.class);
                    if(offer.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        list.add(offer);
                        Shared.keyList.add(dt.getKey());
                        noOffer.setVisibility(View.GONE);
                    }
                }
                ListAdapter adapter = new ListAdapter(MyOfferNeeded.this, list);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void openChat(View view) {
        startActivity(new Intent(this, ChatList.class));
    }
    public void logout(View view) {
        Shared.reset();
        FirebaseAuth.getInstance().signOut();
                        finishAffinity();
                       startActivity(new Intent(this,LoginActivity.class));
    }
}
