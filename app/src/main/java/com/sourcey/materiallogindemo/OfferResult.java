package com.sourcey.materiallogindemo;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
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
import com.sourcey.materiallogindemo.RealTimeServices.IInofrmationNotification;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.RealTimeServices.IOfferResult;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sourcey.materiallogindemo.Shared.customer;
import static java.lang.Thread.sleep;

public class OfferResult extends AppCompatActivity implements IOfferResult, IInofrmationNotification {

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
                        Glide.with(OfferResult.this).load(user.getProfilePic()).placeholder(R.drawable.avatar_logo)
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

    @Override
    public void onAllRecomendedSent() {
        tab.getTabAt(0).select();
    }

    @Override
    public void sentNotificationToHome() {
        RelativeLayout notificaitonRelativeLayout=findViewById(R.id.bottomUpNotification);
        notificaitonRelativeLayout.setVisibility(View.VISIBLE);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificaitonRelativeLayout.setVisibility(View.GONE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private int counter=2;



        public PagerAdapter(FragmentManager fm,Context _ctx) {
            super(fm);
            this._ctx=_ctx;
        }
        Context _ctx;
        public PagerAdapter(FragmentManager fm, int counter, Context _ctx) {
            super(fm);
            this.counter=counter;
            this._ctx=_ctx;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                default:
                    return new SentOfferFragment(ctab1);
                case 1:
                    return new RecomendFragment(ctab2,_ctx);
            }
        }

        @Override
        public int getCount() {
            return counter;
        }
    }

    TabLayout.Tab ctab1;
    TabLayout.Tab ctab2;
    private void __init__() {
        setProfileImage(Shared.MyOffer.getUID());
        tab = findViewById(R.id.tb);
        vp = findViewById(R.id.vp);
        boolean ignored=getIntent().getBooleanExtra("ignore",false);
        boolean wallet=getIntent().getBooleanExtra("wallet",false);
        if(!ignored) {
            if(wallet){
                vp.setAdapter(new PagerAdapter(getSupportFragmentManager(),1,this));
                tab.setupWithViewPager(vp);
                tab.getTabAt(0).setText("عرضى المرسل");
                tab.getTabAt(0).setCustomView(R.layout.custome_tab);
                ctab1 = tab.getTabAt(0);
            }else {
                vp.setAdapter(new PagerAdapter(getSupportFragmentManager(),this));
                tab.setupWithViewPager(vp);
                tab.getTabAt(1).setText("عروضى المتاحة");
                tab.getTabAt(0).setText("عروضى المرسله");
                tab.getTabAt(0).setCustomView(R.layout.custome_tab);
                tab.getTabAt(1).setCustomView(R.layout.custome_tab);
                ctab1 = tab.getTabAt(0);
                ctab2 = tab.getTabAt(1);
            }
        }
//        TextView b = (TextView) tab.getTabAt(0).getCustomView().findViewById(R.id.badge);
//        b.setText("10");
//        View v = tab.getTabAt(0).getCustomView().findViewById(R.id.badgeCotainer);
//        v.setVisibility(View.VISIBLE);

//        View v = tab.getTabAt(0).getCustomView();
//        BadgeView badge = new BadgeView(this, v);
//        badge.setText("1");
//       // badge.show();
        for (int i = 0; i < tab.getTabCount(); i++) {
            //noinspection ConstantConditions
//            tab.getTabAt(i).setCustomView(R.layout.custome_tab);
//            TextView b = (TextView) tab.getTabAt(i).getCustomView().findViewById(R.id.badge);
//            b.setText("offers"+"10");

        }
    }


    LinearLayout recommencLayout;
    String type, buildType,_offerID;
    public String getType(){
        return type;
    }
    public String getBuildType(){
        return buildType;
    }

    public String _getOfferID(){
        return _offerID;
    }
    LinearLayout CustomerLayout;
    TextView offerOrderName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_result);
        try {
            profileImage=findViewById(R.id.profileImage);
            if (Shared.user.getType().equals(Shared.Array[1])) {
                LinearLayout client_name_section = findViewById(R.id.client_name_section);
                client_name_section.setVisibility(View.GONE);
                profileImage.setVisibility(View.GONE);
            }
            CustomerLayout = findViewById(R.id.customerLayout);
            noOffer = findViewById(R.id.noOffer);
            rv = findViewById(R.id.customerRV);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(this));
            recommencLayout = findViewById(R.id.recommencLayout);
            offerState = findViewById(R.id.offerState);
            Intent intent = getIntent();

            String cuid=intent.getStringExtra("uid");
            if(cuid!=null)
                setProfileImage(cuid);
            boolean sold=intent.getBooleanExtra("sold",false);
            type = intent.getStringExtra("type");
            _offerID=intent.getStringExtra("offer_id");
            String price = intent.getStringExtra("price");
            buildType = intent.getStringExtra("build_type");
            String city = intent.getStringExtra("city");
            String userName = intent.getStringExtra("userName");
            offerOrderName = findViewById(R.id.offerOrderName);
            offerOrderName.setText(userName);
            offetText = findViewById(R.id.offerText);
            if (customer) {
                offerState.setText("العروض المقدمه من قبل الوسطاء");
                recommencLayout.setVisibility(View.GONE);
                CustomerLayout.setVisibility(View.VISIBLE);

            } else {
                offetText.setText(userName);
                offerState.setText("العروض المقدمة من قبلك ");
                recommencLayout.setVisibility(View.VISIBLE);
                CustomerLayout.setVisibility(View.GONE);
                __init__();
            }
            if(sold)
                offerState.setText("تم الشراء من قبل هذا العرض");
            offerType = findViewById(R.id.offerType);

            offerType.setText(type + " " + buildType);
            offerplace = findViewById(R.id.offerplace);
            offerplace.setText(city);
            offerPrice = findViewById(R.id.offerPrice);
            offerPrice.setText(price);
            navigationView = findViewById(R.id.navigationView);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.profile_nav:
                            startActivity(new Intent(OfferResult.this, Profile.class));
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
            getUserData();
            if (Shared.customer)
                getData();
            else {
                userID = intent.getStringExtra("userID");
                offerID = intent.getStringExtra("offerID");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
    public String getUserID(){
        return userID;
    }
    public String getOfferID(){
        return offerID;
    }
    String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<com.sourcey.materiallogindemo.model.OfferResult>list;
    String offerURL=currentUser;



    ImageView profileImage;
    private void setProfileImage(String UID){
        FirebaseDatabase.getInstance().getReference("user").child(UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user=dataSnapshot.getValue(User.class);
                        Glide.with(getApplicationContext()).load(user.getProfilePic()).into(profileImage);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    private void getData(){
//        pb.setVisibility(View.VISIBLE);
        String s="عروض لطلب";
        offetText.setText(s+" "+Shared.MyOffer.getType()+" "+Shared.MyOffer.getBuildingTyp());
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
                                if(dt.getKey().equals(_offerID)) {
                                    for (DataSnapshot dt1 : dt.getChildren()) {
                                                com.sourcey.materiallogindemo.model.OfferResult offer = dt1.getValue(com.sourcey.materiallogindemo.model.OfferResult.class);
                                        offerURL+="/"+Shared.offerID+"/"+dt1.getKey();
                                        offer.setUrl(offerURL);
                                        list.add(offer);
                                        offerURL=currentUser;
                                    }
                                }
                            }
                            if(list.size()==0)
                            {
                                pb.setVisibility(View.GONE);
                                noOffer.setVisibility(View.VISIBLE);
                            }
                                offerAdapter Adapter=new offerAdapter(OfferResult.this,list,true);
                                rv.setAdapter(Adapter);

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
        Shared.offerID=offerID;
        if(Shared.customer)
        startActivity(new Intent(this,OfferShowOnMapAct.class));
        else{
            startActivity(new Intent(this,MapsActivity.class));
            Shared.notCurrent=false;
        }
    }
    String offerID,userID;
    List<com.sourcey.materiallogindemo.model.OfferResult>offerResultList;
}
