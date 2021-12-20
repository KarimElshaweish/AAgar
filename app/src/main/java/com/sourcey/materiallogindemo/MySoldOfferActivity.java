package com.sourcey.materiallogindemo;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.model.OfferResult;

import java.util.ArrayList;
import java.util.List;

public class MySoldOfferActivity extends AppCompatActivity {
    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;
    List<OfferResult> list;
    private void __init__(){
        rv=findViewById(R.id.rv1);
        pb=findViewById(R.id.pb);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.MULTIPLY);
        noOffer=findViewById(R.id.noOffer);
        rv.setLayoutManager(new LinearLayoutManager(this));
        getData();
        Shared.owner=true;
    }
    private void getData(){
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Sold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    if (dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for(DataSnapshot dt1:dt.getChildren()) {
                            list.add(dt1.getValue(com.sourcey.materiallogindemo.model.OfferResult.class));
                        }

                    }
                }
                if(list.size()==0){
                    pb.setVisibility(View.GONE);
                    noOffer.setVisibility(View.VISIBLE);
                }
                offerAdapter Adapter=new offerAdapter(MySoldOfferActivity.this,list,1);
                rv.setAdapter(Adapter);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sold_offer);
        try {
            __init__();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
