package com.sourcey.materiallogindemo.Fragment.RecommendFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class SentOfferFragment extends Fragment {


    ProgressBar pb;
    RecyclerView rv;
    TextView noOffer;
    TabLayout.Tab tab;

    @SuppressLint("ValidFragment")
    public SentOfferFragment(TabLayout.Tab tab) {
        this.tab = tab;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_sent_offer, container, false);
        rv=root.findViewById(R.id.rv1);
        pb=root.findViewById(R.id.pb);
        noOffer=root.findViewById(R.id.noOffer);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        getOfferResult();
        return root;
    }

    List<com.sourcey.materiallogindemo.model.OfferResult>offerResultList;
    offerAdapter Adapter;
    private void  getOfferResult(){
        offerResultList=new ArrayList<>();
        OfferResult activity=(OfferResult) getActivity();
        pb.setVisibility(View.VISIBLE);
        String uid=activity.getUserID();
        String offerid=activity.getOfferID();
        if(uid==null)
            uid= Shared.offerNeed.getUID();
        if(offerid==null)
            offerid=Shared.offerID;
        FirebaseDatabase.getInstance().getReference("linkOffer")
                .child(uid)
                .child(offerid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offerResultList=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    com.sourcey.materiallogindemo.model.OfferResult offerResult=
                            dt.getValue(com.sourcey.materiallogindemo.model.OfferResult.class);
                    String[]IDS=offerResult.getOfferID().split("\\*");
                    String clientID=IDS[0];
                    if(clientID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        offerResultList.add(offerResult);
                    }
                }
                if(offerResultList.size()==0)
                {
                    pb.setVisibility(View.GONE);
                    noOffer.setVisibility(View.VISIBLE);
                }else{
//                    TextView b = tab.getCustomView().findViewById(R.id.badge);
//                    b.setText(String.valueOf(offerResultList.size()));
//                    View v =tab.getCustomView().findViewById(R.id.badgeCotainer);
//                    v.setVisibility(View.VISIBLE);
                    try {
                        Adapter = new offerAdapter(getContext(), offerResultList);
                        rv.setAdapter(Adapter);
                        pb.setVisibility(View.GONE);
                        noOffer.setVisibility(View.GONE);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
