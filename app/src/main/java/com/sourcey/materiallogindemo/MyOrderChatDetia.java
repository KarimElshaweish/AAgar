package com.sourcey.materiallogindemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MyOrderChatDetia extends AppCompatActivity {

    TextView type,price,city,street;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_chat_detia);
        type=findViewById(R.id.type);
        type.setText(Shared.MyOffer.getType());
        price=findViewById(R.id.price);
        price.setText(Shared.MyOffer.getPrice());
        city=findViewById(R.id.city);
        city.setText(Shared.MyOffer.getCity());
        street=findViewById(R.id.street);
        street.setText(Shared.MyOffer.getStreet());
    }

    public void Finish(View view) {
        finish();
    }
}
