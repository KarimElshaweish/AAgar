package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    ProgressDialog progressDialog;
    User user;
    CircleImageView navAvatar;
    TextView dlName;
    private void  getUserData(){
        navAvatar=findViewById(R.id.navAvatar);
        dlName=findViewById(R.id.dlName);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("جارى تحميل الصفحه الشخصيه");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        user=dataSnapshot.getValue(User.class);
                        Glide.with(goodOffersAct.this).load(user.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(navAvatar);
                        dlName.setText(user.getName());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    TextView profile_nav,fav,order,chat_nav,txtLogout,myWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_offers);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile_nav:
                        startActivity(new Intent(goodOffersAct.this,Profile.class));
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
        chat_nav=findViewById(R.id.chat_nav);
        chat_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
        profile_nav=findViewById(R.id.profile_nav);
        profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(goodOffersAct.this,Profile.class));
            }
        });
        txtLogout=findViewById(R.id.txtLogout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        myWallet=findViewById(R.id.myWallet);
        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(goodOffersAct.this,MyWalletAct.class));
            }
        });
        getUserData();
    }
    List<Offer> list;
    private void getData(final String city){
        list=new ArrayList<>();
        Shared.keyList=new ArrayList<>();
        final ListAdapter adapter = new ListAdapter(goodOffersAct.this, list);
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
    EditText price;
    public void Filter(View view){
        price=findViewById(R.id.price);
        if(!price.getText().equals("")){
            List<Offer>listFilter=new ArrayList<>();
            for (Offer offer:
                    list) {
                if(Double.parseDouble(offer.getPrice())<=Double.parseDouble(price.getText().toString())){
                        listFilter.add(offer);

                }
            }
            ListAdapter adapter=new ListAdapter(this,listFilter);
            rv.setAdapter(adapter);
        }
    }

    public void openMap(View view) {
        Shared.AddRandomButton=true;
        Shared.addOfferNewRandom=true;
        Shared.random=true;
        startActivity(new Intent(this,MapsActivity.class));
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
