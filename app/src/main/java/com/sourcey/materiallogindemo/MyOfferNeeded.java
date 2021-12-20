package com.sourcey.materiallogindemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import android.os.Bundle;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.model.Offer;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.RealTimeServices.IMyOfferNeed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOfferNeeded extends AppCompatActivity implements IMyOfferNeed {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        noOffer=findViewById(R.id.noOffer);
        getData();
    }

    boolean opend=false;
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
        //startActivity(new Intent(this,Add_Offers.class));
        startActivity(new Intent(this,ChosingOrderTypeActivity.class));
    }
    TextView profile_nav,fav,order,chat_nav,notifcationTitle,activeOrder,soldOrder,notifcation_nav,myWallet;
    CardView notifcation;
    Spinner spinner;
    List<String>spinnerList= Arrays.asList(new String[]{"الكل","فيلا ", "ارض ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "});

    MyOfferNeeded act=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offer_needed);
        spinner=findViewById(R.id.spinner);
        ArrayAdapter spinnerAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(genraOfferList!=null) {
                    if (i != 0) {
                        List<Offer> newOfferListr = new ArrayList<>();
                        for (Offer offer : genraOfferList) {
                            if (offer.getBuildingTyp().equals(spinnerList.get(i))) {
                                newOfferListr.add(offer);
                            }
                        }
                        ListAdapter adapter = new ListAdapter(act, newOfferListr);
                        if (rv == null) {
                            rv = findViewById(R.id.rv);
                            rv.setHasFixedSize(true);
                            rv.setLayoutManager(new LinearLayoutManager(MyOfferNeeded.this));
                        }
                        rv.setAdapter(adapter);
                    } else {
                        ListAdapter adapter = new ListAdapter(MyOfferNeeded.this, genraOfferList);
                        if (rv == null) {
                            rv = findViewById(R.id.rv);
                            rv.setHasFixedSize(true);
                            rv.setLayoutManager(new LinearLayoutManager(MyOfferNeeded.this));
                        }
                        rv.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setAdapter(spinnerAdapter);
        myWallet=findViewById(R.id.myWallet);
        myWallet.setVisibility(View.GONE);
        noOffer=findViewById(R.id.noOffer);
        rv=findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        try{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                return;
            }
            LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
            boolean enabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!enabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

            Shared.customer=true;
            notifcation=findViewById(R.id.notifcation);
            activeOrder=findViewById(R.id.activeOrder);
            activeOrder.setVisibility(View.GONE);
            soldOrder=findViewById(R.id.soldOrder);
            soldOrder.setVisibility(View.GONE);
            notifcation_nav=findViewById(R.id.notifcation_nav);
            TextView feedback=findViewById(R.id.feedback);
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MyOfferNeeded.this, com.sourcey.materiallogindemo.feedback.class));
                }
            });
            notifcation_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MyOfferNeeded.this,NotificationActivity.class));
                }
            });
            notifcationTitle=findViewById(R.id.notifcationTitle);
            getNotification();
            order=findViewById(R.id.order);
            order.setText("الطلبات المقفلة");
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
            TextView moneyGetWayText=findViewById(R.id.moneyGetWayText);
            moneyGetWayText.setVisibility(View.GONE);
//            moneyGetWayText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startActivity(new Intent(MyOfferNeeded.this, PaymentMethod.class));
//                }
//            });
            chat_nav=findViewById(R.id.chat_nav);
            chat_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openChat(v);
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


            getData();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }

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
    private void getData(){
        FirebaseDatabase.getInstance().getReference("OfferNeeded")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                Shared.keyList=new ArrayList<>();
                boolean find=false;
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    for(DataSnapshot dt1:dt.getChildren()) {
                        Offer offer = dt1.getValue(Offer.class);
                        offer.setOfferID(dt1.getKey());
                        if (
                                offer.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            list.add(offer);
                            find=true;
                            Shared.keyList.add(dt1.getKey());
                            if(noOffer!=null)
                            noOffer.setVisibility(View.GONE);
                        }
                    }
                }

                if(!find&&!opend){
                    opend=true;
                    startActivity(new Intent(MyOfferNeeded.this, ChosingOrderTypeActivity.class));
                }
                list=sort(list);
                genraOfferList=list;
                ListAdapter adapter = new ListAdapter(MyOfferNeeded.this, list);
                if(rv==null){
                    rv=findViewById(R.id.rv);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(MyOfferNeeded.this));
                }
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

    public void openChat(View v) {
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
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hud.dismiss();
                        try {
                            user = dataSnapshot.getValue(User.class);
                            Glide.with(MyOfferNeeded.this).load(user.getProfilePic()).placeholder(R.drawable.avatar_logo)
                                    .into(navAvatar);
                            dlName.setText(user.getName());
                        }catch (Exception ex){
                            System.out.println(ex.getMessage());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    @SuppressLint("WrongConstant")
    public void seedl(View view) {
        if(dl==null)
            dl = findViewById(R.id.drawer);
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

    public void openIgnored(View view) {
        startActivity(new Intent(this,IgnoredOrderActivity.class));
    }


    @Override
    public void onListEmpty() {
        noOffer.setVisibility(View.VISIBLE);
    }


    @Override
    public void onListHasData() {
        noOffer.setVisibility(View.GONE);
    }


}
