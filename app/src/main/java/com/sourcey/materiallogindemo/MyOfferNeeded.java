package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Adapter.OfferListChatAdapter;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOfferNeeded extends AppCompatActivity {

    public void Finish(View view){
        finish();
    }
    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;

        NavigationView navigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.profile:
                    startActivity(new Intent(MyOfferNeeded.this,Profile.class));
                    return true;
            }
            return false;
        }
    };
    RecyclerView rv;
    TextView noOffer;
    List<Offer>list;
    TextView txtLogout;
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
    TextView profile_nav,fav,order,chat_nav,notifcationTitle;
    CardView notifcation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_offer_needed);
        notifcation=findViewById(R.id.notifcation);
        notifcationTitle=findViewById(R.id.notifcationTitle);
        getNotification();
        order=findViewById(R.id.order);
        profile_nav=findViewById(R.id.profile_nav);
        fav=findViewById(R.id.fav);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOfferNeeded.this,ArchiveOrder.class));
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOfferNeeded.this,favActicity.class));
            }
        });
        profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOfferNeeded.this,Profile.class));
            }
        });
        chat_nav=findViewById(R.id.chat_nav);
        chat_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
        txtLogout=findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile_nav:
                        startActivity(new Intent(MyOfferNeeded.this,Profile.class));
                        return true;
                }
                return false;
            }
        });
        dl = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        getUserData();

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
                    for(DataSnapshot dt1:dt.getChildren()) {
                        Offer offer = dt1.getValue(Offer.class);
                        offer.setOfferID(dt1.getKey());
                        if (offer.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            list.add(offer);
                            Shared.keyList.add(dt1.getKey());
                            noOffer.setVisibility(View.GONE);
                        }
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

    private void getNotification() {
        FirebaseDatabase.getInstance().getReference("Notification_MSG").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null) {
                            notifcation.setVisibility(View.VISIBLE);
                            notifcationTitle.setText(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void openChat() {
        startActivity(new Intent(this, ChatList.class));
    }
    public void logout() {
        Shared.reset();
        FirebaseAuth.getInstance().signOut();
                        finishAffinity();
                       startActivity(new Intent(this,LoginActivity.class));
    }

    public void profile(View view) {
        startActivity(new Intent(this,Profile.class));
    }
    User user;
    CircleImageView navAvatar;
    TextView dlName;
    private void  getUserData(){
        navAvatar=findViewById(R.id.navAvatar);
        dlName=findViewById(R.id.dlName);
        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setMaxProgress(100)
                .show();
        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hud.dismiss();
                        user=dataSnapshot.getValue(User.class);
                        Glide.with(MyOfferNeeded.this).load(user.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(navAvatar);
                        dlName.setText(user.getName());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void seedl(View view) {
        dl.openDrawer(Gravity.END);
    }

    public void removeNotification(View view) {
        FirebaseDatabase.getInstance().getReference("Notification_MSG").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifcation.setVisibility(View.GONE);
            }
        });

    }
}
