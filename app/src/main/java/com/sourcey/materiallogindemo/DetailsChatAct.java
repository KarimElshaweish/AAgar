package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class DetailsChatAct extends AppCompatActivity {

    TextView type,price,city,street,desc;
    CarouselView carouselView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_chat);
        type=findViewById(R.id.type);
        type.setText(Shared.offerKnow.getSpinnerType());
        price=findViewById(R.id.price);
        price.setText(Shared.offerKnow.getPrice());
        city=findViewById(R.id.city);
        city.setText(Shared.offerKnow.getCity());
        street=findViewById(R.id.street);
        street.setText(Shared.offerKnow.getStreet());
        desc=findViewById(R.id.desc);
        desc.setText(Shared.offerKnow.getBuildingType());
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(Shared.offerKnow.getImageList().size());
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(DetailsChatAct.this).load(Shared.offerKnow.getImageList().get(position)).into(imageView);
            }
        };
        carouselView.setImageListener(imageListener);

    }

    public void Map(View view) {
    }

    public void Finish(View view) {
        finish();
    }
}
