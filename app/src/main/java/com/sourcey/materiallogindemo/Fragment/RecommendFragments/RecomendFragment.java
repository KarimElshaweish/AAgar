package com.sourcey.materiallogindemo.Fragment.RecommendFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.OffersGridViewAdapter;
import com.sourcey.materiallogindemo.Design.CustomeListView;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.RealTimeServices.IInofrmationNotification;
import com.sourcey.materiallogindemo.RealTimeServices.IOfferResult;
import com.sourcey.materiallogindemo.Shared;
import com.sourcey.materiallogindemo.model.OfferResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressLint("ValidFragment")
public class RecomendFragment extends Fragment implements IOfferResult  {

    ListView gv;
    HashMap<LatLng, com.sourcey.materiallogindemo.model.OfferResult> hashMap;
    List<com.sourcey.materiallogindemo.model.OfferResult> offerResultListReco;
    TabLayout.Tab tab;

    IOfferResult _ctx;
    IInofrmationNotification iInofrmationNotification;
    @SuppressLint("ValidFragment")
    public RecomendFragment(TabLayout.Tab tab, Context _ctx) {
        this.tab = tab;
        this._ctx= (IOfferResult) _ctx;
        this.iInofrmationNotification=(IInofrmationNotification)_ctx;
    }
    OffersGridViewAdapter offersGridViewAdapter;
    TextView b;
    View v;
    private void getData(final String category, final  String buildType) {
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
                            com.sourcey.materiallogindemo.model.OfferResult offer = dt.getValue(com.sourcey.materiallogindemo.model.OfferResult.class);
                            String type1=offer.getType().trim();
                            String buildType1=offer.getBuildingType().trim();
                            if (type1.equals(category.trim()) &&buildType1.equals(buildType.trim())) {
                                Shared.listReult.add(offer);
                                LatLng latLng = new LatLng(offer.getLituide(), offer.getLongtuide());
                                hashMap.put(latLng, offer);
                                offerResultListReco.add(offer);
                            }

                        }
                        String count=String.valueOf(offerResultListReco.size());
                         b = tab.getCustomView().findViewById(R.id.badge);

                        b.setText(count);
                         v =tab.getCustomView().findViewById(R.id.badgeCotainer);
                        v.setVisibility(View.VISIBLE);
                        if(count.equals("0")) {
                            v.setVisibility(View.GONE);
                        //    noOffer.setVisibility(View.VISIBLE);
                        }
                        offersGridViewAdapter=new OffersGridViewAdapter(offerResultListReco, getContext(),b,v,getActivity(),noOffer,offerId,iInofrmationNotification,
                                new ArrayList<OfferResult>());
                        gv.setAdapter(offersGridViewAdapter);
                        getSented();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
    List<OfferResult>resultList;
    void getSented(){
        resultList=new ArrayList<>();
        com.sourcey.materiallogindemo.OfferResult activity=(com.sourcey.materiallogindemo.OfferResult) getActivity();
        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(activity.getOfferID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    OfferResult dataOfferResult = dt.getValue   (OfferResult.class);
                    resultList.add(dataOfferResult);
                    offersGridViewAdapter=new OffersGridViewAdapter(offerResultListReco, getContext(),b,v,getActivity(),noOffer,offerId,iInofrmationNotification,
                            resultList);
                    gv.setAdapter(offersGridViewAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    TextView noOffer;
    String offerId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_recomend, container, false);
        gv=root.findViewById(R.id.gv);
        noOffer=root.findViewById(R.id.noOffer);
        com.sourcey.materiallogindemo.OfferResult activity=(com.sourcey.materiallogindemo.OfferResult)getActivity();
        offerId=activity.getOfferID();
        getData(activity.getType(),activity.getBuildType());
        return root;
    }


    @Override
    public void onAllRecomendedSent() {
        offersGridViewAdapter.clear();
        noOffer.setVisibility(View.VISIBLE);
    }
}
