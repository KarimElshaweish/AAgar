package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Model.LinkOffer;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sourcey.materiallogindemo.Shared.offerID;
import static com.sourcey.materiallogindemo.Shared.user;

public class OfferResult extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("linkOffer");
    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;
    private DrawerLayout dl;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;



    List<LinkOffer>getMyResult(){
        final List<LinkOffer>idList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("linkOffer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    LinkOffer linkOffer=dt.getValue(LinkOffer.class);
                    if(linkOffer.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        idList.add(new LinkOffer(linkOffer.getOfferID(),dt.getKey()));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return idList;
    }
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
                        Glide.with(OfferResult.this).load(user.getProfilePic()).placeholder(R.drawable.avatar)
                                .into(navAvatar);
                        dlName.setText(user.getName());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    TextView offetText,offerType,offerplace,offerPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_result);
        offerType=findViewById(R.id.offerType);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        String price=intent.getStringExtra("price");
        String buildType=intent.getStringExtra("type");
        String city=intent.getStringExtra("city");
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
        rv=findViewById(R.id.rv1);
        pb=findViewById(R.id.pb);
        noOffer=findViewById(R.id.noOffer);
        offetText=findViewById(R.id.offerText);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getUserData();
        getData();

    }
    String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<com.sourcey.materiallogindemo.Model.OfferResult>list;
    private void getData(){
        String s="عروض لطلب";
        offetText.setText(s+" "+Shared.MyOffer.getType());
        list=new ArrayList<>();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
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
                                pb.setVisibility(View.GONE);
                            }
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
    }
}
