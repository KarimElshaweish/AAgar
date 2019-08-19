package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import static com.sourcey.materiallogindemo.Shared.offerID;
import static com.sourcey.materiallogindemo.Shared.user;

public class OfferResult extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("linkOffer");
    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;




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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_result);
        rv=findViewById(R.id.rv1);
        pb=findViewById(R.id.pb);
        noOffer=findViewById(R.id.noOffer);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getData();

    }
    String currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<com.sourcey.materiallogindemo.Model.OfferResult>list;
    private void getData(){
        list=new ArrayList<>();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUser)) {
                for (DataSnapshot dt:dataSnapshot.getChildren()) {
                    if(dt.hasChild(Shared.offerID)) {
                        for (DataSnapshot dt1 : dt.getChildren()) {
                                    if(dt1.getKey().equals(Shared.offerID)){
                                        for(DataSnapshot dt2:dt1.getChildren()) {
                                            com.sourcey.materiallogindemo.Model.OfferResult offer = dt2.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class);
                                            list.add(offer);
                                        }
                                }
                            }
                        }
                    }
                    if(list.size()==0)
                    {
                        pb.setVisibility(View.GONE);
                        noOffer.setVisibility(View.VISIBLE);
                    }
                }
                offerAdapter Adapter=new offerAdapter(OfferResult.this,list);
                rv.setAdapter(Adapter);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
