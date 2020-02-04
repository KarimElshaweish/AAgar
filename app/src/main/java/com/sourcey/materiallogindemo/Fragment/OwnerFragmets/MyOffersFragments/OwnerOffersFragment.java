package com.sourcey.materiallogindemo.Fragment.OwnerFragmets.MyOffersFragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.MapsActivity;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.ArrayList;
import java.util.List;

public class OwnerOffersFragment extends Fragment {


    RecyclerView rv;
    ProgressBar pb;
    TextView noOffer;
    List<OfferResult> list;
    FloatingActionButton addOfferFloationAction;
    private void getData(){
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("OfferResult").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
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
                if (getContext()!=null) {
                    offerAdapter Adapter = new offerAdapter(getContext(), list);
                    rv.setAdapter(Adapter);
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void __init__(View root){
        addOfferFloationAction=root.findViewById(R.id.addOffer);
        addOfferFloationAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });
        rv=root.findViewById(R.id.rv1);
        pb=root.findViewById(R.id.pb);
        pb.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.MULTIPLY);
        noOffer=root.findViewById(R.id.noOffer);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
        Shared.owner=true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_owner_offers, container, false);
        __init__(root);
        return root;
    }
    public void openMap() {
        Shared.AddRandomButton=true;
        Shared.addOfferNewRandom=true;
        Shared.random=true;
        startActivity(new Intent(getContext(), MapsActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Shared.owner=false;
    }

}
