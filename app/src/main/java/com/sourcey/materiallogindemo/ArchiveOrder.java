package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.graphics.PorterDuff;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.IgnoredListAdapter;
import com.sourcey.materiallogindemo.model.Offer;
import com.sourcey.materiallogindemo.RealTimeServices.IIgnoredServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArchiveOrder extends AppCompatActivity implements IIgnoredServices {

    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;

    NavigationView navigationView;
    TextView profile_nav,order;
    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.profile:
                    startActivity(new Intent(ArchiveOrder.this,Profile.class));
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_order);
        try{
            rv=findViewById(R.id.rv1);
            pb=findViewById(R.id.pb);
            pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary),PorterDuff.Mode.MULTIPLY);
            noOffer=findViewById(R.id.noOffer);
            rv.setLayoutManager(new LinearLayoutManager(this));
            getData();
            navigationView=findViewById(R.id.navigationView);
            dl = findViewById(R.id.drawer);
            toggle = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
            dl.addDrawerListener(toggle);
            toggle.syncState();
            order=findViewById(R.id.order);
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ArchiveOrder.this,MyOfferNeeded.class));
                }
            });
            profile_nav=findViewById(R.id.profile_nav);
            profile_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ArchiveOrder.this,Profile.class));
                }
            });
        }catch (Exception ex){
            System.out.println(ex);
        }

    }
    HashMap<String,Offer>hashMap;
    List<Offer> list;
    private void getData() {
        list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("ArchiveOrder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hashMap=new HashMap<>();
                list=new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                        Offer offerResult = dt.getValue(Offer.class);
                        list.add(offerResult);
                        hashMap.put(dt.getKey(), offerResult);
                        Shared.keyList.add(dt.getKey());


                }
                if (list.size() == 0) {
                    pb.setVisibility(View.GONE);
                    noOffer.setVisibility(View.VISIBLE);
                }
                IgnoredListAdapter Adapter = new IgnoredListAdapter(list,ArchiveOrder.this,true);
                rv.setAdapter(Adapter);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void finish(View view) {
        finish();
    }

    @Override
    public void onListEmpty() {

    }

    @Override
    public void onListHasData() {

    }
}
