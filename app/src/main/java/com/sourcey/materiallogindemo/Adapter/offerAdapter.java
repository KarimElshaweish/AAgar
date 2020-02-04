package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.materiallogindemo.BuildDetial;
import com.sourcey.materiallogindemo.ChatAct;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;
import java.util.Map;

public class offerAdapter extends RecyclerView.Adapter<offerAdapter.ViewhHolder> {

    Context _ctx;
    List<OfferResult>offerResultList;
    LayoutInflater mlLayoutInflater;
    public offerAdapter(Context _ctx, List<OfferResult> offerResultList) {
        this._ctx = _ctx;
        this.offerResultList = offerResultList;
        this.mlLayoutInflater=LayoutInflater.from(_ctx);
    }

    @NonNull
    @Override
    public ViewhHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.list_item,parent,false) ;
        return new offerAdapter.ViewhHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewhHolder holder, final int position) {
        holder.desc.setText(offerResultList.get(position).getBuildingType());
        holder.cityText.setText(offerResultList.get(position).getCity());
        if(offerResultList.get(position).getPrice().isEmpty()) {
            Map<Object, Object> map = (Map<Object, Object>) offerResultList.get(position).getAspect();
            holder.priceText.setText(map.get("groundMeterPrice").toString() + " " + "ريال");
        }else{
            holder.priceText.setText(offerResultList.get(position).getPrice()+ " " + "ريال");
        }
        holder.TypeText.setText(offerResultList.get(position).getType());
        holder.streetText.setText(offerResultList.get(position).getStreet());
        holder.vs.setVisibility(View.GONE);
        if(offerResultList.get(position).getImageList()!=null&&
                offerResultList.get(position).getImageList().size()>0){
            Glide.with(_ctx).load(offerResultList.get(position).getImageList().get(0)).into(holder.img);
        }
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(_ctx,ChatAct.class);
                Shared.sent_id=offerResultList.get(position).getuID();
                Shared.offerKnow=offerResultList.get(position);
                Shared.fristTime=true;
                _ctx.startActivity(intent);
            }
        });
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(offerResultList.get(position).getDescription()).setValue(offerResultList.get(position))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(_ctx, "تمت الاضافه", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.offerKnow=new OfferResult();
                try {
                    Shared.offerKnow= (OfferResult) offerResultList.get(position).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                _ctx.startActivity(new Intent(_ctx,BuildDetial.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return offerResultList.size();
    }

    public class ViewhHolder extends RecyclerView.ViewHolder {
        TextView desc,vs;
        TextView priceText,TypeText,cityText,streetText,seeMore;
        ImageView chat,offerImage,img_share,img;
        CardView cv;
        public ViewhHolder(View itemView) {
            super(itemView);
            priceText=itemView.findViewById(R.id.price);
            TypeText=itemView.findViewById(R.id.type);
            cityText=itemView.findViewById(R.id.city);
            desc=itemView.findViewById(R.id.desc);
            streetText=itemView.findViewById(R.id.street);
            chat=itemView.findViewById(R.id.sentImage);
            offerImage=itemView.findViewById(R.id.offer_image);
            offerImage.setVisibility(View.GONE);
            img_share=itemView.findViewById(R.id.img_share);
            cv=itemView.findViewById(R.id.cv);
            vs=itemView.findViewById(R.id.vs);
            img=itemView.findViewById(R.id.img);
            seeMore=itemView.findViewById(R.id.seeMore);
            seeMore.setVisibility(View.GONE);
            if(Shared.owner)
                img_share.setVisibility(View.VISIBLE);

        }
    }
}
