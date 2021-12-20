package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.materiallogindemo.ActiveOrderActivity;
import com.sourcey.materiallogindemo.BuildDetial;
import com.sourcey.materiallogindemo.ChatAct;
import com.sourcey.materiallogindemo.MySoldOfferActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;
import com.sourcey.materiallogindemo.model.OfferResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class offerAdapter extends RecyclerView.Adapter<offerAdapter.ViewhHolder> {

    Context _ctx;
    List<OfferResult>offerResultList;
    LayoutInflater mlLayoutInflater;



    Boolean sold=false;
    public offerAdapter(Context _ctx, List<OfferResult> offerResultList) {
        this._ctx = _ctx;
        this.offerResultList = offerResultList;
        this.mlLayoutInflater=LayoutInflater.from(_ctx);
        if(_ctx.getClass()== MySoldOfferActivity.class){
            sold=true;
        }
    }
    public offerAdapter(Context _ctx, List<OfferResult> offerResultList,String soldStr) {
        this._ctx = _ctx;
        this.offerResultList = offerResultList;
        this.mlLayoutInflater=LayoutInflater.from(_ctx);
        sold=true;
    }
    boolean seen=false;
    public offerAdapter(Context _ctx, List<OfferResult> offerResultList,boolean seen) {
        this._ctx = _ctx;
        this.offerResultList = offerResultList;
        this.mlLayoutInflater=LayoutInflater.from(_ctx);
        this.seen=seen;
    }

    int k=-1;
    public offerAdapter(Context context, List<OfferResult> list, int i) {
        this._ctx = context;
        this.offerResultList = list;
        this.mlLayoutInflater=LayoutInflater.from(_ctx);
        if(_ctx.getClass()== MySoldOfferActivity.class){
            sold=true;
        }
        k=i;
    }

    public offerAdapter(Context context, List<OfferResult> list, String s, int i) {
        this._ctx=context;
        this.offerResultList=list;
        sold=true;
        k=i;
    }

    @NonNull
    @Override
    public ViewhHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.list_item,parent,false) ;
        return new offerAdapter.ViewhHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewhHolder holder, final int position) {
        if(k!=-1)
            holder.matlopText.setText("عرض");
        if(sold)
            holder.saleDoneText.setVisibility(View.VISIBLE);
        holder.close_ignore_relative.setVisibility(View.GONE);
        holder.desc.setText(offerResultList.get(position).getBuildingType());
        holder.cityText.setText(offerResultList.get(position).getCity());
        if(offerResultList.get(position).getPrice().isEmpty()) {
            Map<Object, Object> map = (Map<Object, Object>) offerResultList.get(position).getAspect();
            if(map.get("groundMeterPrice")!=null)
            holder.priceText.setText(map.get("groundMeterPrice").toString() + " " + "ريال");
        }else{
            holder.priceText.setText(offerResultList.get(position).getPrice()+ " " + "ريال");
        }
        holder.TypeText.setText(offerResultList.get(position).getType());
        if(offerResultList.get(position).isSeen())
            holder.logo_typ.setBackgroundColor(Color.parseColor("#d3d3d3"));
            else
          holder.logo_typ.setBackground(_ctx.getResources().getDrawable(R.drawable.typ_rent_border));
        if(holder.TypeText.getText().toString().trim().equals("شراء")) {
          //  holder.TypeText.setBackground(_ctx.getResources().getDrawable(R.drawable.typ_buy_border));
         //   holder.logo_typ.setBackground(_ctx.getResources().getDrawable(R.drawable.typ_buy_border));
        }else {
          //  holder.TypeText.setBackground(_ctx.getResources().getDrawable(R.drawable.typ_rent_border));
          //  holder.logo_typ.setBackground(_ctx.getResources().getDrawable(R.drawable.typ_rent_border));
        }
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
                                Toasty.success(_ctx, "تمت الاضافه!", Toast.LENGTH_SHORT, true).show();
                               // Toast.makeText(_ctx, "تمت الاضافه", Toast.LENGTH_SHORT).show();
                                
                            }
                        });
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(_ctx,BuildDetial.class);
                intent.putExtra("position",String.valueOf(position));
                if (sold)
                    intent.putExtra("sold",true);
                Shared.offerKnow=new OfferResult();
                try {
                    Shared.offerKnow= (OfferResult) offerResultList.get(position).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                if (seen) {
                    Shared.offerKnow.setSeen(true);
                    FirebaseDatabase.getInstance().getReference("linkOffer").child(offerResultList.get(position).getUrl()).setValue(Shared.offerKnow);
                }
                _ctx.startActivity(intent);
            }
        });

    }

    List<OfferResult>orignalList;
    public  void reset(){
        this.offerResultList=orignalList;
        notifyDataSetChanged();
    }
    public void filter(String filterStr){
        orignalList=new ArrayList<>();
        orignalList=this.offerResultList;
        List<OfferResult>filtertList=new ArrayList<>();
        for(OfferResult offerResult:offerResultList)
            if(offerResult.getBuildingType().equals(filterStr))
                filtertList.add(offerResult);

        this.offerResultList=filtertList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return offerResultList.size();
    }

    public class ViewhHolder extends RecyclerView.ViewHolder {
        TextView desc, vs;
        TextView priceText, TypeText, cityText, streetText, seeMore;
        ImageView chat, offerImage, img_share, img;
        CardView cv;
        RelativeLayout logo_typ,close_ignore_relative;
        TextView saleDoneText,matlopText;

        public ViewhHolder(View itemView) {
            super(itemView);
            matlopText=itemView.findViewById(R.id.matlop);
            saleDoneText=itemView.findViewById(R.id.saleDoneText);
            logo_typ=itemView.findViewById(R.id.logo_typ);
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
            close_ignore_relative=itemView.findViewById(R.id.close_ignore_relative);
            if(Shared.owner)
                img_share.setVisibility(View.VISIBLE);

        }
    }
}
