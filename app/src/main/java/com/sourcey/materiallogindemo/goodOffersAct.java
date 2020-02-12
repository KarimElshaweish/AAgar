package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Fragment.OwnerFragmets.ActiveNeedsFragments;
import com.sourcey.materiallogindemo.Fragment.OwnerFragmets.MyAllOfferFragment;
import com.sourcey.materiallogindemo.Fragment.OwnerFragmets.MyOffersFragments.OwnerOffersFragment;
import com.sourcey.materiallogindemo.Fragment.OwnerFragmets.MyOffersFragments.OwnerSoldFragment;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class goodOffersAct extends AppCompatActivity implements LocationListener {

    ProgressBar pb;
    RecyclerView rv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferNeeded");
    TextView noOrder;
    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public void logout() {
        Shared.reset();
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void openChat() {
        startActivity(new Intent(this, ChatList.class));
    }

    public void Share(View view) {
        startActivity(new Intent(this, OfferShare.class));
    }

    private FusedLocationProviderClient fusedLocationClient;
    TextView noOffer;
    List<OfferResult> list;
    FloatingActionButton addOfferFloationAction;
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

                    offerAdapter Adapter = new offerAdapter(goodOffersAct.this, list);
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

    User user;
    CircleImageView navAvatar;
    TextView dlName;

    private void getUserData() {
        navAvatar = findViewById(R.id.navAvatar);
        dlName = findViewById(R.id.dlName);
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
                        user = dataSnapshot.getValue(User.class);
                        Glide.with(goodOffersAct.this).load(user.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(navAvatar);
                        dlName.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    TextView profile_nav,feedback_nav,active_order,soldOrder, fav, order, chat_nav, txtLogout, myWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_offers);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile_nav:
                        startActivity(new Intent(goodOffersAct.this, Profile.class));
                        return true;
                    case R.id.feedback:
                        startActivity(new Intent(goodOffersAct.this,feedback.class));
                        return true;
                }
                return false;
            }
        });
        dl = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        toggle = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        pb = findViewById(R.id.pb);
        soldOrder=findViewById(R.id.soldOrder);
        soldOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(goodOffersAct.this,MySoldOfferActivity.class));
            }
        });
        noOrder = findViewById(R.id.noOrder);
        chat_nav = findViewById(R.id.chat_nav);
        chat_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
        profile_nav = findViewById(R.id.profile_nav);
        profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(goodOffersAct.this, Profile.class));
            }
        });
        active_order=findViewById(R.id.activeOrder);
        active_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(goodOffersAct.this,ActiveOrderActivity.class));
            }
        });
        feedback_nav=findViewById(R.id.feedback);
        feedback_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(goodOffersAct.this,feedback.class));
            }
        });
        txtLogout = findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        myWallet = findViewById(R.id.myWallet);
        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(goodOffersAct.this, MyWalletAct.class));
            }
        });
        getUserData();
        __init__();
    }

    EditText price;
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

    public void profile(View view) {
        startActivity(new Intent(this,Profile.class));
    }

    public void seedl(View view) {
        dl.openDrawer(Gravity.END);

    }
}
