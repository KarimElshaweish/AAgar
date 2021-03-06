package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class goodOffersAct extends AppCompatActivity implements LocationListener {


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

    TextView profile_nav,feedback_nav,active_order,soldOrder, notifcation_nav, order, chat_nav, txtLogout, myWallet;

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
        notifcation_nav=findViewById(R.id.notifcation_nav);
        notifcation_nav.setVisibility(View.GONE);
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


    public void profile(View view) {
        startActivity(new Intent(this,Profile.class));
    }

    public void seedl(View view) {
        dl.openDrawer(Gravity.END);

    }
    List<Offer> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferNeeded");

    private void getData(final String city){
        list=new ArrayList<>();
        Shared.keyList=new ArrayList<>();
        final ListAdapter adapter = new ListAdapter(this, list);
        rv.setAdapter(adapter);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pb.setVisibility(View.VISIBLE);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dt2 : dataSnapshot1.getChildren()) {
                        pb.setVisibility(View.GONE);
                        Offer offer = dt2.getValue(Offer.class);
                        if (city.equals(offer.getCity())) {
                            list.add(offer);
                            Shared.keyList.add(dt2.getKey());
                            adapter.notifyDataSetChanged();
                            if (list.size() > 0) {
                                pb.setVisibility(View.GONE);
                                noOrder.setVisibility(View.GONE);
                            }
                        }

                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale=new Locale("ar");
        Geocoder geoCoder = new Geocoder(this, locale);
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String city = address.get(0).getAdminArea();
            if(city!=null)
                getData(city);

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    ProgressBar pb;
    RecyclerView rv;
    TextView noOrder;
    private void __init__(){
        pb = findViewById(R.id.pb);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        noOrder = findViewById(R.id.noOrder);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            onLocationChanged(location);
                        }
                    }
                });
    }
}
