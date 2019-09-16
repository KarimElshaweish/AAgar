package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;



public class BuildDetial extends AppCompatActivity {


    OfferResult offerResult=Shared.offerKnow;
    TextView type,price,city,street,desc;
    CarouselView carouselView;
    LinearLayout fb;
    ImageView favImage;
    private void __init__(){
        favImage=findViewById(R.id.faviamge);
        type=findViewById(R.id.type);
        price=findViewById(R.id.price);
        city=findViewById(R.id.city);
        street=findViewById(R.id.street);
        desc=findViewById(R.id.desc);
        type.setText(offerResult.getSpinnerType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        street.setText(offerResult.getStreet());
        desc.setText(offerResult.getBuildingType());
        fb=findViewById(R.id.bottomlin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detial);
        __init__();
        if(offerResult.isFav()){
            favImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_yallewo));
        }else{
            favImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_white));
        }
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

    public void Map(View view) {
        startActivity(new Intent(this,ShowBuildMap.class));
    }

    String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
    public void choseOffer(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("جارى الاختيار");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("linkOffer").child(user).child(Shared.offerID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference("linkOffer").child(user).child(Shared.offerID).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference("ArchiveOrder").child(user).child(Shared.MyOffer.getOfferID()).setValue(Shared.MyOffer).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(BuildDetial.this, "تم إختيار هذا العرض فقط ", Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("OfferNeeded").child(user)
                                        .child(Shared.MyOffer.getOfferID()).setValue(null);
                            }
                        });


                    }
                });

            }
        });
    }
    ImageView fav;
    ProgressDialog progressDialog;
    public void AddToFavoutit(View view) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        fav=(ImageView)view;
        if(offerResult.isFav()){
            offerResult.setFav(false);
            progressDialog.setTitle("جارى الحذف من المفضله");
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference("Fav")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(offerResult.getId())
                    .setValue(null)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference("linkOffer")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(Shared.offerID)
                            .child(offerResult.getDescription())
                            .setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_white));
                        }
                    });

                }
            });
        }else{
            progressDialog.setTitle("اضافة الى المفضله....");
            progressDialog.show();
            offerResult.setFav(true);
            FirebaseDatabase.getInstance().getReference("Fav").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(offerResult.getId()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference("Fav").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(offerResult.getId()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("linkOffer")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(Shared.offerID)
                                    .child(offerResult.getDescription())
                                    .setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_yallewo));

                                }
                            });
                        }
                    });
                }
            });
        }
    }
}
