package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Fragment.RecommendFragments.RecomendFragment;
import com.sourcey.materiallogindemo.Fragment.RecommendFragments.SentOfferFragment;
import com.sourcey.materiallogindemo.Model.LinkOffer;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sourcey.materiallogindemo.Shared.customer;

public class OfferResult extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("linkOffer");
    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;
    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
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
                        Glide.with(OfferResult.this).load(user.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(navAvatar);
                        dlName.setText(user.getName());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    TextView offetText,offerType,offerplace,offerPrice,offerState;



    ViewPager vp;
    TabLayout tab;

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SentOfferFragment();
                case 1:
                default:
                    return new RecomendFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void __init__() {
        tab = findViewById(R.id.tb);
        vp = findViewById(R.id.vp);
        vp.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tab.setupWithViewPager(vp);
        tab.getTabAt(1).setText("عروضى المتاحة");
        tab.getTabAt(0).setText("عروضى المرسله");
        for (int i = 0; i < tab.getTabCount(); i++) {
            //noinspection ConstantConditions
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custome_tab, null);
            tab.getTabAt(i).setCustomView(tv);
        }
    }


    LinearLayout recommencLayout;
    String type, buildType;
    public String getType(){
        return type;
    }
    public String getBuildType(){
        return buildType;
    }
    LinearLayout CustomerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_result);
        CustomerLayout=findViewById(R.id.customerLayout);
        noOffer=findViewById(R.id.noOffer);
        rv=findViewById(R.id.customerRV);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        recommencLayout=findViewById(R.id.recommencLayout);
        offerState=findViewById(R.id.offerState);
        Intent intent=getIntent();
         type=intent.getStringExtra("type");
        String price=intent.getStringExtra("price");
         buildType=intent.getStringExtra("build_type");
        String city=intent.getStringExtra("city");
        if(customer){
            offerState.setText("العروض المقدمه من قبل الوسطاء");
            recommencLayout.setVisibility(View.GONE);
            CustomerLayout.setVisibility(View.VISIBLE);
        }else{
            offerState.setText("العروض المقدمة من قبلك ");
            recommencLayout.setVisibility(View.VISIBLE);
            CustomerLayout.setVisibility(View.GONE);
            __init__();
        }
        offerType=findViewById(R.id.offerType);

        offerType.setText(type+" "+buildType);
        offerplace=findViewById(R.id.offerplace);
        offerplace.setText(city);
        offerPrice=findViewById(R.id.offerPrice);
        offerPrice.setText(price);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile_nav:
                        startActivity(new Intent(OfferResult.this,Profile.class));
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
        pb=findViewById(R.id.pb);
        offetText=findViewById(R.id.offerText);
        getUserData();
        if(Shared.customer)
        getData();
        else {
            userID=intent.getStringExtra("userID");
            offerID=intent.getStringExtra("offerID");
        }

    }
    public String getUserID(){
        return userID;
    }
    public String getOfferID(){
        return offerID;
    }
    String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<com.sourcey.materiallogindemo.Model.OfferResult>list;
    private void getData(){
//        pb.setVisibility(View.VISIBLE);
        String s="عروض لطلب";
        offetText.setText(s+" "+Shared.MyOffer.getType());
        list=new ArrayList<>();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
              pb.setVisibility(View.GONE);
                if(dataSnapshot.hasChild(currentUser)) {
                    mReference.child(currentUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            for (DataSnapshot dt:dataSnapshot2.getChildren()) {
                                if(dt.getKey().equals(Shared.offerID)) {
                                    for (DataSnapshot dt1 : dt.getChildren()) {
                                                com.sourcey.materiallogindemo.Model.OfferResult offer = dt1.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class);
                                                list.add(offer);
                                    }
                                }
                            }
                            if(list.size()==0)
                            {
                                pb.setVisibility(View.GONE);
                                noOffer.setVisibility(View.VISIBLE);
                            }else{
                                offerAdapter Adapter=new offerAdapter(OfferResult.this,list);

                                rv.setAdapter(Adapter);
                            }
                            pb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void chat(View view) {
        startActivity(new Intent(this,ChatList.class));
    }

    public void finish(View view) {
        finish();
        if(customer) {
            Intent intent = new Intent(this,MyOfferNeeded.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    public void showOnMap(View view) {
        if(Shared.customer)
        startActivity(new Intent(this,OfferShowOnMapAct.class));
        else{
            startActivity(new Intent(this,MapsActivity.class));
            Shared.notCurrent=false;
        }
    }
    String offerID,userID;
    List<com.sourcey.materiallogindemo.Model.OfferResult>offerResultList;
}
