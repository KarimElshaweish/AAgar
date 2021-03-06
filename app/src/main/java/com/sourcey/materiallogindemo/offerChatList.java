package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Adapter.OfferListChatAdapter;
import com.sourcey.materiallogindemo.Model.Offer;

import java.util.ArrayList;
import java.util.List;

public class offerChatList extends AppCompatActivity {

    RecyclerView rv;
    List<Offer>offerList;
    OfferListChatAdapter offerListChatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_chat_list);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        offerList=new ArrayList<>();
        getData();
    }
    private void getData(){
        FirebaseDatabase.getInstance().getReference("OfferNeeded").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offerList=new ArrayList<>();
                Shared.keyList=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Offer offer=dt.getValue(Offer.class);
                    offer.setOfferID(dt.getKey());
                    if(offer.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        offerList.add(offer);
                    }
                }
                offerListChatAdapter=new OfferListChatAdapter(offerChatList.this,offerList);
                rv.setAdapter(offerListChatAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void Finish(View view) {
        finish();
    }
}
