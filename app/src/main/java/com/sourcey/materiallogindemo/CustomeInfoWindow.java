package com.sourcey.materiallogindemo;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;

public class CustomeInfoWindow  implements GoogleMap.InfoWindowAdapter {
    private  final  View window;
    private Context _ctx;
    private OfferResult offerResult;
    public CustomeInfoWindow(OfferResult offerResult, Context _ctx) {
        this.window = LayoutInflater.from(_ctx).inflate(R.layout.list_item,null);
        this._ctx = _ctx;
        this.offerResult=offerResult;
    }

    private void renderWindowText(OfferResult offer,View window,Marker marker){
       TextView priceText=window.findViewById(R.id.price);
        TextView  TypeText=window.findViewById(R.id.type);
        TextView cityText=window.findViewById(R.id.city);
        TextView desc=window.findViewById(R.id.desc);
        priceText.setText(offer.getPrice());
        TypeText.setText(offer.getSpinnerType());
        cityText.setText(offer.getCity());
        desc.setText(offer.getDescription());
    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(offerResult,window,marker);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(offerResult,window,marker);
        return window;
    }
}
