package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;
import com.sourcey.materiallogindemo.MapsActivity;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

public class OffersGridViewAdapter extends BaseAdapter {
    List<OfferResult>offerResultList;
    Context _ctx;

    public OffersGridViewAdapter(List<OfferResult> offerResultList, Context _ctx) {
        this.offerResultList = offerResultList;
        this._ctx = _ctx;
    }

    @Override
    public int getCount() {
        return offerResultList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private void sentOffer(final OfferResult noffer){
        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(noffer.getDescription())){
                    Toast.makeText(_ctx, "تم اضافه هذا العرض من قبل ", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(noffer.getDescription()).setValue(noffer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(_ctx, "تمت الاضافه", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkIfSent(final OfferResult offerResult, final RelativeLayout sentRl){
        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(offerResult.getDescription())){
                    sentRl.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root= LayoutInflater.from(_ctx).inflate(R.layout.offer_image_item,viewGroup,false);
        final OfferResult offerResult=offerResultList.get(i);
        RoundedImageView roundedImageView=root.findViewById(R.id.imageCover);
        Glide.with(_ctx).load(offerResult.getImageList().get(0)).into(roundedImageView);
        TextView price=root.findViewById(R.id.price);
        price.setText(offerResult.getPrice());
        RelativeLayout share=root.findViewById(R.id.rl);
        RelativeLayout sentLayout=root.findViewById(R.id.sentLayout);
        checkIfSent(offerResult,sentLayout);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentOffer(offerResult);
            }
        });
        return root;
    }
}
