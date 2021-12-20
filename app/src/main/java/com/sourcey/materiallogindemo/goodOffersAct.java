package com.sourcey.materiallogindemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.IgnoredListAdapter;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.model.Offer;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.Payment.PaymentMethod;
import com.sourcey.materiallogindemo.RealTimeServices.IIgnoredServices;
import com.sourcey.materiallogindemo.RealTimeServices.IMyOfferNeed;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class goodOffersAct extends AppCompatActivity implements LocationListener, IMyOfferNeed, IIgnoredServices {


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                        } else {
                            finish();
                            startActivity(new Intent(goodOffersAct.this, goodOffersAct.class));
                        }
                    }
                });
    }

    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public void logout() {
        Shared.reset();
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void openChat(View v) {
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
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hud.dismiss();
                        user = dataSnapshot.getValue(User.class);
                        Glide.with(goodOffersAct.this).load(user.getProfilePic()).placeholder(R.drawable.avatar_logo)
                                .into(navAvatar);
                        dlName.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    TextView profile_nav,feedback_nav,active_order,soldOrder, notifcation_nav, order, chat_nav, txtLogout, myWallet;
    private List<Offer>sort(List<Offer> offerList){
        boolean swap=true;
        while (swap){
            swap=false;
            for(int i=0;i<offerList.size()-1;i++){
                Offer offer1=offerList.get(i);
                Offer offer2=offerList.get(i+1);

                String time1=offer1.getTime();
                String time2=offer2.getTime();

                SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

                try{
                    Date dt1=sdf.parse(time1);
                    Date dt2=sdf.parse(time2);
                    if(dt1.before(dt2)){
                        Offer temp=offerList.get(i);
                        offerList.set(i,offerList.get(i+1));
                        offerList.set(i+1,temp);
                        swap=true;
                    }
                }catch (Exception ex){
                }
            }
        }
        return offerList;
    }
    List<Offer>genraOfferList;
    Spinner spinner;
    List<String>spinnerList= Arrays.asList(new String[]{"الكل","فيلا ", "ارض ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_offers);
        try {
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
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
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
                    openChat(v);
                }
            });
            profile_nav = findViewById(R.id.profile_nav);
            TextView moneyGetWayText=findViewById(R.id.moneyGetWayText);
            moneyGetWayText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(goodOffersAct.this, PaymentMethod.class));
                }
            });
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
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }

    EditText price;


    public void profile(View view) {
        startActivity(new Intent(this,Profile.class));
    }

    @SuppressLint("WrongConstant")
    public void seedl(View view) {
        dl.openDrawer(Gravity.END);

    }
    List<Offer> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferNeeded");
     ListAdapter adapter;
    List<Offer>ignoredOfferList;
    IgnoredListAdapter listAdapter;
    boolean getDefualt=false;
    private void getIgnoredData(final String city){

        FirebaseDatabase.getInstance().getReference("Ignored").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ignoredOfferList=new ArrayList<>();
                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                                Offer offer = dt.getValue(Offer.class);
                                ignoredOfferList.add(offer);
                        }
                        ignoredOfferList=sort(ignoredOfferList);
                        listAdapter=new IgnoredListAdapter(ignoredOfferList,goodOffersAct.this,"home");
                        getData(city);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    boolean checkIgnoredOffer(Offer offer){
        for(Offer offer1 :ignoredOfferList){
            if(offer1.getOfferID().equals(offer.getOfferID()))
                return true;
        }
        return  false;
    }
    private void getData(final String city){
        list=new ArrayList<>();
        adapter = new ListAdapter(this, list);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pb.setVisibility(View.VISIBLE);
                list.clear();
                Shared.keyList=new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot dt2 : dataSnapshot1.getChildren()) {
                        pb.setVisibility(View.GONE);
                        Offer offer = dt2.getValue(Offer.class);
                        if (!checkIgnoredOffer(offer)&&city.equals(offer.getCity())) {
                            list.add(offer);
                            Shared.keyList.add(dt2.getKey());
                            list=sort(list);
                            adapter = new ListAdapter(goodOffersAct.this, list);
                            if(!ignored&&!getDefualt) {
                                getDefualt=true;
                                rv.setAdapter(adapter);

                                if (list.size() > 0) {
                                    pb.setVisibility(View.GONE);
                                    noOrder.setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                }
                if(!ignored) {
                    genraOfferList = list;
                    pb.setVisibility(View.GONE);
                    noOrder.setText("لاتوجد طلبات فى هذه المنطقة");
                }
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
            if(city!=null) {
                getIgnoredData(city);
            }


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
        if (
                ActivityCompat.checkSelfPermission(goodOffersAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(goodOffersAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(goodOffersAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;}
        else{
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

    public void openIgnored(View view) {
        Shared.ignoredList=ignoredOfferList;
        startActivity(new Intent(this,IgnoredOrderActivity.class));
    }

    @Override
    public void onListEmpty() {
        noOrder.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListHasData() {
        noOrder.setVisibility(View.GONE);
    }

    Boolean ignored=false;
    @SuppressLint("ResourceType")
    public void tabClick(View view) {
        switch (view.getId()){
            case R.id.ignoredButton:
                ignored=true;
                ((Button)findViewById(R.id.ordersButton)).setTextColor(Color.parseColor("#d3d3d3"));
                ((Button)findViewById(R.id.ignoredButton)).setTextColor(Color.parseColor(getResources().getString(R.color.primary)));
                if(rv!=null)
               rv.setAdapter(listAdapter);
                if(listAdapter!=null&&listAdapter.getItemCount()>0)
                    noOrder.setVisibility(View.GONE);
                else
                    noOrder.setVisibility(View.VISIBLE);
                break;
            default:
                ignored=false;
                if(rv!=null)
                    rv.setAdapter(adapter);
                ((Button)findViewById(R.id.ignoredButton)).setTextColor(Color.parseColor("#d3d3d3"));
                ((Button)findViewById(R.id.ordersButton)).setTextColor(Color.parseColor(getResources().getString(R.color.primary)));
                if(adapter!=null&&adapter.getItemCount()>0)
                    noOrder.setVisibility(View.GONE);
                else
                    noOrder.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void openFilter(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View customeLayout=getLayoutInflater().inflate(R.layout.custome_spinner_dialog,null);

        builder.setView(customeLayout);
        ListView listView=customeLayout.findViewById(R.id.typeList);
        final AlertDialog dialog=builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(genraOfferList!=null) {
                    if (i != 0) {
                        List<Offer> newOfferListr = new ArrayList<>();
                        if(!ignored) {
                            for (Offer offer : genraOfferList) {
                                if (offer.getBuildingTyp().equals(spinnerList.get(i))) {
                                    newOfferListr.add(offer);
                                }
                            }
                            ListAdapter adapter = new ListAdapter(goodOffersAct.this, newOfferListr);
                            if (rv == null) {
                                rv = findViewById(R.id.rv);
                                rv.setHasFixedSize(true);
                                rv.setLayoutManager(new LinearLayoutManager(goodOffersAct.this));
                            }
                            String noType="لا توجد طالبات ";
                            noType+=spinnerList.get(i);
                            noOrder.setText(noType);
                            if(newOfferListr.size()==0)
                                noOrder.setVisibility(View.VISIBLE);
                            else
                                noOrder.setVisibility(View.GONE);
                            rv.setAdapter(adapter);
                        }
                        else{

                            for (Offer offer : ignoredOfferList) {
                                if (offer.getBuildingTyp().equals(spinnerList.get(i))) {
                                    newOfferListr.add(offer);
                                }
                            }
                            IgnoredListAdapter adapter=new IgnoredListAdapter(newOfferListr,goodOffersAct.this);
                            if (rv == null) {
                                rv = findViewById(R.id.rv);
                                rv.setHasFixedSize(true);
                                rv.setLayoutManager(new LinearLayoutManager(goodOffersAct.this));
                            }
                            String noType="لا توجد طالبات ";
                            noType+=spinnerList.get(i);
                            noOrder.setText(noType);
                            if(newOfferListr.size()==0)
                                noOrder.setVisibility(View.VISIBLE);
                            else
                                noOrder.setVisibility(View.GONE);
                            rv.setAdapter(adapter);
                        }

                    } else {
                        noOrder.setVisibility(View.GONE);
                        if(!ignored) {
                            ListAdapter adapter = new ListAdapter(goodOffersAct.this, genraOfferList);
                            if (rv == null) {
                                rv = findViewById(R.id.rv);
                                rv.setHasFixedSize(true);
                                rv.setLayoutManager(new LinearLayoutManager(goodOffersAct.this));
                            }
                            rv.setAdapter(adapter);
                        }else{
                            IgnoredListAdapter adapter = new IgnoredListAdapter(ignoredOfferList,goodOffersAct.this,"home" );
                            if(ignoredOfferList.size()>0)
                                noOrder.setVisibility(View.GONE);
                            else
                                noOrder.setVisibility(View.VISIBLE);
                            if (rv == null) {
                                rv = findViewById(R.id.rv);
                                rv.setHasFixedSize(true);
                                rv.setLayoutManager(new LinearLayoutManager(goodOffersAct.this));
                            }
                            rv.setAdapter(adapter);
                        }
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
