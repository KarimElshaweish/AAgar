package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.Wallet;
import com.sourcey.materiallogindemo.Model.Deals;

import java.util.ArrayList;
import java.util.List;

public class MyWalletAct extends AppCompatActivity {

    List<Deals>list;
    RecyclerView rv;

    private void getData(){
        FirebaseDatabase.getInstance().getReference("Deals")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Double price=0.0;
                        list=new ArrayList<>();
                        for(DataSnapshot dt:dataSnapshot.getChildren()){
                            for(DataSnapshot dt1:dt.getChildren()) {
                                if(dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    Deals deals = dt1.getValue(Deals.class);
                                    deals.setKey(dt1.getKey());
                                    list.add(deals);
                                    if(deals.isAgree()){
                                        price+=deals.getPrice();
                                    }
                                }
                            }
                        }
                        Wallet adpter=new Wallet(list,MyWalletAct.this);
                        rv.setAdapter(adpter);
                        totalTxt.setText(price.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }
    TextView totalTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        totalTxt=findViewById(R.id.total);
        getData();

    }

    public void Finish(View view) {
        finish();
    }
}
