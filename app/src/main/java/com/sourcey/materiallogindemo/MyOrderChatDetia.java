package com.sourcey.materiallogindemo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MyOrderChatDetia extends AppCompatActivity {

    TextView type,price,city,street,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_chat_detia);
        try {
            type = findViewById(R.id.type);
            type.setText(Shared.MyOffer.getType());
            price = findViewById(R.id.price);
            price.setText(Shared.MyOffer.getPrice());
            city = findViewById(R.id.city);
            city.setText(Shared.MyOffer.getCity());
            street = findViewById(R.id.street);
            street.setText(Shared.MyOffer.getStreet());
            desc = findViewById(R.id.desc);
            desc.setText(Shared.MyOffer.getBuildingTyp());
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void Finish(View view) {
        finish();
    }
}
