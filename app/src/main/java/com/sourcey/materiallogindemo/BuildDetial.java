package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;



public class BuildDetial extends AppCompatActivity {


    OfferResult offerResult=Shared.offerKnow;
    TextView type,price,city,street,desc;
    CarouselView carouselView;
    FloatingActionButton fb;
    private void __init__(){
        type=findViewById(R.id.type);
        price=findViewById(R.id.price);
        city=findViewById(R.id.city);
        street=findViewById(R.id.street);
        desc=findViewById(R.id.desc);
        type.setText(offerResult.getSpinnerType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        street.setText(offerResult.getStreet());
        desc.setText(offerResult.getDescription());
        fb=findViewById(R.id.fb);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detial);
        __init__();
        if(Shared.user.getType().equals(Shared.Array[0]))
            fb.setVisibility(View.GONE);
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(offerResult.getImageList().size());

        carouselView.setImageListener(imageListener);

    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(BuildDetial.this).load(offerResult.getImageList().get(position)).into(imageView);
        }
    };

    public void Finish(View view) {
        finish();
    }

    public void Chat(View view) {
        Intent intent=new Intent(this,ChatAct.class);
        Shared.sent_id=offerResult.getuID();
        Shared.offerKnow=offerResult;
        Shared.fristTime=true;
        startActivity(intent);
    }
}
