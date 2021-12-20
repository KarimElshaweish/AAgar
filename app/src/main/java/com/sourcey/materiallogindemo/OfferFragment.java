package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.sourcey.materiallogindemo.databinding.FragmentOfferBinding;

public class OfferFragment extends Fragment implements View.OnClickListener {



    FragmentOfferBinding fragmentOfferBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOfferBinding= DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.fragment_offer,null,false);

        fragmentOfferBinding.setOfferNeed(Shared.offerNeed);
        fragmentOfferBinding.setMyOffer(Shared.offerKnow);
        fragmentOfferBinding.chatFragment.setOnClickListener(this);
        fragmentOfferBinding.cv.setOnClickListener(this);
        fragmentOfferBinding.cv2.setOnClickListener(this);
        return fragmentOfferBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chatFragment:
                Navigation.findNavController(fragmentOfferBinding.getRoot()).navigate(R.id.action_offerFragment_to_chatFragment);
                break;
            case R.id.cv:
                openDetails();
                break;
            case R.id.cv2:
                openOfferDetalis();
                break;

        }
    }
    public void openDetails() {
        if (Shared.customer) {
            Shared.offerID = Shared.offerNeed.getOfferID();
            Intent intent = new Intent(getActivity(), com.sourcey.materiallogindemo.OfferResult.class);
            intent.putExtra("type", Shared.offerNeed.getType());
            intent.putExtra("build_type", Shared.offerNeed.getBuildingTyp());
            intent.putExtra("price", Shared.offerNeed.getPrice());
            intent.putExtra("city", Shared.offerNeed.getCity());
            intent.putExtra("offer_id",Shared.offerNeed.getOfferID());
            Shared.putOfferOnMap = Shared.offerNeed;
            startActivity(intent);
            Shared.MyOffer = Shared.offerNeed;

        } else {
            Shared.toID = Shared.offerNeed.getUID();
            Shared.offer = Shared.offerNeed;
            Shared.offerID =Shared.offerNeed.getOfferID();
            Intent intent = new Intent(getActivity(), com.sourcey.materiallogindemo.OfferResult.class);
            intent.putExtra("type", Shared.offerNeed.getType());
            intent.putExtra("build_type", Shared.offerNeed.getBuildingTyp());
            intent.putExtra("price",Shared.offerNeed.getPrice());
            intent.putExtra("city", Shared.offerNeed.getCity());
            intent.putExtra("userID", Shared.offerNeed.getUID());
            intent.putExtra("offerID",Shared.offerNeed.getOfferID());
            Shared.putOfferOnMap = Shared.offerNeed;
            startActivity(intent);
            Shared.MyOffer =Shared.offerNeed;
        }
    }
    public void openOfferDetalis() {
        Intent intent=new Intent(getContext(),BuildDetial.class);
        Gson gson=new Gson();
        intent.putExtra("enable","0");
        intent.putExtra("offer_result",gson.toJson(Shared.offerKnow));
        startActivity(intent);
    }

}