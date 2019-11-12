package com.sourcey.materiallogindemo.Fragment.RecommendFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.OffersGridViewAdapter;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecomendFragment extends Fragment {

    GridView gv;
    HashMap<LatLng, com.sourcey.materiallogindemo.Model.OfferResult> hashMap;
    List<com.sourcey.materiallogindemo.Model.OfferResult> offerResultListReco;
    private void getData(final String category,final  String buildType) {
        hashMap = new HashMap<>();
        Shared.listReult = new ArrayList<>();
        offerResultListReco=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("OfferResult")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        offerResultListReco=new ArrayList<>();
                        for (DataSnapshot dt : dataSnapshot.getChildren()) {
                            com.sourcey.materiallogindemo.Model.OfferResult offer = dt.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class);
                            if (offer.getType().equals(category)&&offer.getBuildingType().equals(buildType)) {
                                Shared.listReult.add(offer);
                                LatLng latLng = new LatLng(offer.getLituide(), offer.getLongtuide());
                                hashMap.put(latLng, offer);
                                offerResultListReco.add(offer);
                            }

                        }
                        OffersGridViewAdapter offersGridViewAdapter=new OffersGridViewAdapter(offerResultListReco, getContext());
                        gv.setAdapter(offersGridViewAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_recomend, container, false);
        gv=root.findViewById(R.id.gv);
        com.sourcey.materiallogindemo.OfferResult activity=(com.sourcey.materiallogindemo.OfferResult)getActivity();
        getData(activity.getType(),activity.getBuildType());
        return root;
    }






}
