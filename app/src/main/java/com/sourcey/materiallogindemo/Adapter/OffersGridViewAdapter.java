package com.sourcey.materiallogindemo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sourcey.materiallogindemo.BuildDetial;
import com.sourcey.materiallogindemo.RealTimeServices.IInofrmationNotification;
import com.sourcey.materiallogindemo.model.Notification;
import com.sourcey.materiallogindemo.model.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.RealTimeServices.IOfferResult;
import com.sourcey.materiallogindemo.Shared;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OffersGridViewAdapter extends BaseAdapter {
    List<OfferResult>offerResultList;
    Context _ctx;
    TextView badgeText;
    View v;
    IOfferResult iOfferResult;
    TextView noOffer;
    String offerID;
    IInofrmationNotification iInofrmationNotification;
    List<OfferResult>sentedList;
    public OffersGridViewAdapter(List<OfferResult> offerResultList, Context _ctx, TextView badgeText, View v, Activity activity,TextView noOffer,String offerID
    ,IInofrmationNotification iInofrmationNotification,List<OfferResult>sentedList) {
        this.offerResultList = offerResultList;
        this._ctx = _ctx;
        this.badgeText=badgeText;
        this.v=v;
        this.iOfferResult= (IOfferResult) activity;
        this.noOffer=noOffer;
        this.offerID=offerID;
        this.iInofrmationNotification=iInofrmationNotification;
        this.sentedList=sentedList;
    }

    public void clear(){
        this.offerResultList.clear();
        notifyDataSetChanged();
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
       final FirebaseDatabase ref=FirebaseDatabase.getInstance();
        ref.getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(noffer.getDescription())){
                    Toast.makeText(_ctx, "تم اضافه هذا العرض من قبل ", Toast.LENGTH_SHORT).show();
                }else{
                   ref.getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(noffer.getDescription()).setValue(noffer)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                  //  Toast.makeText(_ctx, "تمت الاضافه", Toast.LENGTH_SHORT).show();
                                    iInofrmationNotification.sentNotificationToHome();
                                    String time=Calendar.getInstance().getTime().toString();
                                    Notification notification=new Notification(noffer.getBuildingType(),noffer.getPrice(),Shared.offerID,time);
                                    ref.getReference("Notification").child(Shared.toID).child(time)
                                            .setValue(notification);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkIfSent(final int position, final RelativeLayout sentRl,final Button btn){
   //     finalI=positon;
        for(OfferResult offerResult:sentedList){
            String currentPositionID=offerResultList.get(position).getOfferID();
            String comingID=offerResult.getOfferID();
            if(currentPositionID.equals(comingID)){
                sentRl.setVisibility(View.GONE);
                notifyDataSetChanged();
                break;
            }
        }
//        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dt:dataSnapshot.getChildren()){
//                        OfferResult dataOfferResult = dt.getValue(OfferResult.class);
//                        resultList.add(dataOfferResult);
//
//                    boolean flagExit=false;
//                        if (!visitedList.contains(comingID)&&currentPositionID.equals(comingID)) {
//                            flagExit=true;
//                            visitedList.add(comingID);
//                            sentRl.setVisibility(View.GONE);
//                         //   offerResultList.remove(i);
//                            int size=Integer.parseInt(badgeText.getText().toString());
//                            size -= 1;
//                            if(size<0)
//                                size=0;
//                            badgeText.setText(String.valueOf(size));
//                            if(size==0) {
//                                v.setVisibility(View.GONE);
//                                iOfferResult.onAllRecomendedSent();
//                                noOffer.setVisibility(View.VISIBLE);
//                            }else{
//                                noOffer.setVisibility(View.GONE);
//                            }
//                           // notifyDataSetChanged();
//                          //  btn.setVisibility(View.GONE);
//                    }else {
//                            sentRl.setVisibility(View.VISIBLE);
//                        }
//                        if(flagExit)
//                            break;
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
   //     notifyDataSetChanged();
    }
    int i=0;
    boolean flagCheck=false;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root= LayoutInflater.from(_ctx).inflate(R.layout.offer_image_item,viewGroup,false);
        final OfferResult offerResult=offerResultList.get(i);
        TextView price=root.findViewById(R.id.price);
        TextView type=root.findViewById(R.id.type);
        type.setText(offerResult.getType());
        TextView buildType=root.findViewById(R.id.buildType);
        buildType.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        Button share=root.findViewById(R.id.sent);
        RelativeLayout sentLayout=root.findViewById(R.id.sentLayout);
        RelativeLayout allLayout=root.findViewById(R.id.rl);
        ImageView infomation = (ImageView) root.findViewById(R.id.information);
        badgeText.setText(String.valueOf(offerResultList.size()-sentedList.size()));

        infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(_ctx,BuildDetial.class);
                Gson gson=new Gson();
                intent.putExtra("enable","0");
                intent.putExtra("offer_result",gson.toJson(offerResult));
                intent.putExtra("position",String.valueOf(i));
                _ctx.startActivity(intent);
            }
        });
        checkIfSent(i, allLayout, share);
        int size=offerResultList.size()-sentedList.size();
        if(size==0) {
            v.setVisibility(View.GONE);
            iOfferResult.onAllRecomendedSent();
            noOffer.setVisibility(View.VISIBLE);
        }else{
            noOffer.setVisibility(View.GONE);
        }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentOffer(offerResult);
            }
        });
        return root;
    }
}
