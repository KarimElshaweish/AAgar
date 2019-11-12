package com.sourcey.materiallogindemo.Fragment.RecommendFragments;

import android.os.Bundle;
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
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.R;

import java.util.ArrayList;
import java.util.List;


public class SentOfferFragment extends Fragment {


    ProgressBar pb;
    RecyclerView rv;
    TextView noOffer;
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
    List<com.sourcey.materiallogindemo.Model.OfferResult>offerResultList;
    private void  getOfferResult(){
        OfferResult activity=(OfferResult) getActivity();
        pb.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("linkOffer")
                .child(activity.getUserID())
                .child(activity.getOfferID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offerResultList=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    com.sourcey.materiallogindemo.Model.OfferResult offerResult=
                            dt.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class);
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
                    offerAdapter Adapter=new offerAdapter(getContext(),offerResultList);
                    rv.setAdapter(Adapter);
                    pb.setVisibility(View.GONE);
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
