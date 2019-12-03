package com.sourcey.materiallogindemo.Fragment.ProfileFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Adapter.offerAdapter;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.MyOfferNeeded;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.ArrayList;
import java.util.List;

import static com.sourcey.materiallogindemo.Shared.Array;


public class myOfferFragment extends Fragment {

    RecyclerView rv;
    List<Offer> list;
    TextView noOffer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_my_offer, container, false);
         noOffer=view.findViewById(R.id.noOffer);
         rv=view.findViewById(R.id.rv);
         rv.setHasFixedSize(true);
         rv.setLayoutManager(new LinearLayoutManager(getContext()));
         if(Shared.user.getType().equals(Array[1])){
             getClientData();
         }else{
             getOwnerData();
         }
         return view;
    }

    private void getClientData() {
        Shared.customer=true;
        FirebaseDatabase.getInstance().getReference("OfferNeeded").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list=new ArrayList<>();
                Shared.keyList=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    Offer offer=dt.getValue(Offer.class);
                    offer.setOfferID(dt.getKey());
                    if(offer!=null&&offer.getUID()!=null&&offer.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        list.add(offer);
                        Shared.keyList.add(dt.getKey());
                    }
                }
                if(list.size()==0)
                    noOffer.setVisibility(View.VISIBLE);
                ListAdapter adapter = new ListAdapter(getContext(), list);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    List<OfferResult>list2;
    private void getOwnerData(){
        list2=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("OfferResult").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list2=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    if (dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for(DataSnapshot dt1:dt.getChildren()) {
                            list2.add(dt1.getValue(com.sourcey.materiallogindemo.Model.OfferResult.class));
                        }

                    }
                }
                if(list2.size()==0){
                    noOffer.setVisibility(View.VISIBLE);
                }
                if (getContext()!=null) {
                    offerAdapter Adapter = new offerAdapter(getContext(), list2);
                    rv.setAdapter(Adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
