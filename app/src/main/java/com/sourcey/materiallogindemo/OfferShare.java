package com.sourcey.materiallogindemo;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;

import java.util.ArrayList;
import java.util.List;

public class OfferShare extends AppCompatActivity {

    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_share);
        rv=findViewById(R.id.rv1);
        pb=findViewById(R.id.pb);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary),PorterDuff.Mode.MULTIPLY);
        noOffer=findViewById(R.id.noOffer);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getData();
        Shared.owner=true;
    }
    List<com.sourcey.materiallogindemo.Model.OfferResult> list;
    private void getData(){
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("OfferResult").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                offerAdapter Adapter=new offerAdapter(OfferShare.this,list);
                rv.setAdapter(Adapter);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Shared.owner=false;
        finish();
    }

    public void finish(View view) {
        finish();
    }
}
