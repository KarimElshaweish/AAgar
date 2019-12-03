package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import org.w3c.dom.Text;

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
        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addListenerForSingleValueEvent(new ValueEventListener() {
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
    private void checkIfSent(final int positon, final RelativeLayout sentRl,final Button btn){
   //     finalI=positon;
        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                        OfferResult dataOfferResult = dt.getValue(OfferResult.class);
                        if (offerResultList.get(positon).getOfferID().equals(dataOfferResult.getOfferID())) {
                            sentRl.setVisibility(View.VISIBLE);
                            btn.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    int i=0;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root= LayoutInflater.from(_ctx).inflate(R.layout.offer_image_item,viewGroup,false);
        final OfferResult offerResult=offerResultList.get(i);
        RoundedImageView roundedImageView=root.findViewById(R.id.imageCover);
        Glide.with(_ctx).load(offerResult.getImageList().get(0)).into(roundedImageView);
        TextView price=root.findViewById(R.id.price);
        TextView type=root.findViewById(R.id.type);
        type.setText(offerResult.getType());
        TextView buildType=root.findViewById(R.id.buildType);
        buildType.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        Button share=root.findViewById(R.id.sent);
        RelativeLayout sentLayout=root.findViewById(R.id.sentLayout);
            checkIfSent(i, sentLayout,share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentOffer(offerResult);
            }
        });
        return root;
    }
}
