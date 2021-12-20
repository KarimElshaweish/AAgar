package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.model.OfferResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    TextView addOfferFloationAction;
    TextView noOffer;
    ProgressBar pb;
    RecyclerView rv;
    List<OfferResult> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferNeeded");
    public void openMap() {
        Shared.offer=null;
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
                            list.add(dt1.getValue(com.sourcey.materiallogindemo.model.OfferResult.class));
                        }
                    }
                }
                if(list.size()==0){
                    pb.setVisibility(View.GONE);
                    noOffer.setVisibility(View.VISIBLE);
                }else
                    noOffer.setVisibility(View.GONE);

                activeAdapter = new offerAdapter(ActiveOrderActivity.this, list,1);
                rv.setAdapter(activeAdapter);
                pb.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    Button activeOrderButton,soldOrderButton;
    private void __init__(){
        activeOrderButton=findViewById(R.id.activeOrder);
        soldOrderButton=findViewById(R.id.soldOrder);
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
        getSoldData();
        Shared.owner=true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);
        try{
            __init__();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    List<String>spinnerList= Arrays.asList(new String[]{"الكل","فيلا ", "ارض ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "});

    public void openFilter(View view) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View customeLayout=getLayoutInflater().inflate(R.layout.custome_spinner_dialog,null);

        builder.setView(customeLayout);
        ListView listView=customeLayout.findViewById(R.id.typeList);
        final AlertDialog dialog=builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(activeOrder){
                    String temp = "النشطة";
                    temp += " ( " + spinnerList.get(i) + " ) ";
                    activeOrderButton.setText(temp);
                    if(i==0){
                        activeAdapter.reset();
                    }else {
                        activeAdapter.filter(spinnerList.get(i));
                    }
                    if(activeAdapter.getItemCount()==0)
                        noOffer.setVisibility(View.VISIBLE);
                    else
                        noOffer.setVisibility(View.GONE);
                }else{
                    String temp = "المباعة";
                    temp += " ( " + spinnerList.get(i) + " ) ";
                    soldOrderButton.setText(temp);
                    if(i==0)
                        soldAdapter.reset();
                    else {
                        soldAdapter.filter(spinnerList.get(i));
                    }
                    if(soldAdapter.getItemCount()==0)
                        noOffer.setVisibility(View.VISIBLE);
                    else
                        noOffer.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    offerAdapter soldAdapter;
    offerAdapter activeAdapter;
    private void getSoldData(){
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Sold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    if (dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for(DataSnapshot dt1:dt.getChildren()) {
                            list.add(dt1.getValue(com.sourcey.materiallogindemo.model.OfferResult.class));
                        }

                    }
                }
                if(list.size()==0){
                    pb.setVisibility(View.GONE);
                   // noOffer.setVisibility(View.VISIBLE);
                }else
                    noOffer.setVisibility(View.GONE);
                soldAdapter=new offerAdapter(ActiveOrderActivity.this,list,"",1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    Boolean activeOrder=true;
    public void tabClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.activeOrder:
                activeOrder=true;
                activeOrderButton.setTextColor(getResources().getColor(R.color.primary));
                soldOrderButton.setTextColor(Color.parseColor("#d3d3d3"));
                rv.setAdapter(activeAdapter);
                if(activeAdapter.getItemCount()==0)
                    noOffer.setVisibility(View.VISIBLE);
                else
                    noOffer.setVisibility(View.GONE);
                break;
            case R.id.soldOrder:
                activeOrder=false;
                soldOrderButton.setTextColor(getResources().getColor(R.color.primary));
                activeOrderButton.setTextColor(Color.parseColor("#d3d3d3"));
                rv.setAdapter(soldAdapter);
                if(soldAdapter.getItemCount()==0)
                    noOffer.setVisibility(View.VISIBLE);
                else
                    noOffer.setVisibility(View.GONE);
                break;
        }
    }
}
