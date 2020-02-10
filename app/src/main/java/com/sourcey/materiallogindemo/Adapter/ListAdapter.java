package com.sourcey.materiallogindemo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sourcey.materiallogindemo.MapsActivity;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewholder> {
    Context _ctx;
    LayoutInflater mlLayoutInflater;
    List<Offer>list;
    public ListAdapter(Context _ctx, List<Offer>list) {
        this._ctx=_ctx;
        this.list=list;
        this.mlLayoutInflater=LayoutInflater.from(_ctx);
    }

    @Override
   public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.list_item,parent,false) ;
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {

        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linst.setVisibility(View.VISIBLE);
                holder.lindesc.setVisibility(View.VISIBLE);
                holder.seeMore.setVisibility(View.GONE);
            }
        });

        holder.desc.setText(list.get(position).getBuildingTyp());
        holder.cityText.setText(list.get(position).getCity());
        holder.TypeText.setText(list.get(position).getType()+" "+list.get(position).getBuildingTyp());
        holder.priceText.setText(list.get(position).getPrice() + " ريال  ");
        holder.street.setText(list.get(position).getStreet());
        holder.area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.getPolygon=true;
                Shared.offer=list.get(position);
                ((Activity)_ctx).startActivity(new Intent(_ctx,MapsActivity.class));
                Shared.toID=list.get(position).getUID();
                Shared.offerID=Shared.keyList.get(position);
            }
        });
        holder.offerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.toID=list.get(position).getUID();
                Shared.offer=list.get(position);
                Shared.offerID=Shared.keyList.get(position);
                _ctx.startActivity(new Intent(_ctx,MapsActivity.class));
            }
        });
        if(Shared.customer){
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Shared.keyList!=null)
                    Shared.offerID=Shared.keyList.get(position);
                    Shared.offerNeed=list.get(position);
                    Intent intent=new Intent(_ctx,OfferResult.class);
                    intent.putExtra("type",list.get(position).getType());
                    intent.putExtra("build_type",list.get(position).getBuildingTyp());
                    intent.putExtra("price",list.get(position).getPrice());
                    intent.putExtra("city",list.get(position).getCity());
                    Shared.putOfferOnMap=list.get(position);
                    _ctx.startActivity(intent);
                    Shared.MyOffer=list.get(position);

                }
            });

        }
        else{
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Shared.toID=list.get(position).getUID();
                    Shared.offer=list.get(position);
                    Shared.offerID=Shared.keyList.get(position);
                    Intent intent=new Intent(_ctx,OfferResult.class);
                    intent.putExtra("type",list.get(position).getType());
                    intent.putExtra("build_type",list.get(position).getBuildingTyp());
                    intent.putExtra("price",list.get(position).getPrice());
                    intent.putExtra("city",list.get(position).getCity());
                    intent.putExtra("userID",list.get(position).getUID());
                    intent.putExtra("offerID",list.get(position).getOfferID());
                    Shared.putOfferOnMap=list.get(position);
                    _ctx.startActivity(intent);
                    Shared.MyOffer=list.get(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        TextView priceText,TypeText,cityText,street,desc,seeMore;
        ImageView img,area,offerImage;
        View bottom;
        CardView cv;
        LinearLayout lindesc,linst;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            area=itemView.findViewById(R.id.area_image);
            priceText=(TextView)itemView.findViewById(R.id.price);
            TypeText=itemView.findViewById(R.id.type);
            cityText=itemView.findViewById(R.id.city);
            img=itemView.findViewById(R.id.sentImage);
            cv=itemView.findViewById(R.id.cv);
            img.setVisibility(View.GONE);
            offerImage=itemView.findViewById(R.id.offer_image);
            bottom=itemView.findViewById(R.id.bottom);
            street=itemView.findViewById(R.id.street);
            bottom.setVisibility(View.GONE);
            desc=itemView.findViewById(R.id.desc);
            seeMore=itemView.findViewById(R.id.seeMore);
            lindesc=itemView.findViewById(R.id.lindesc);
            linst=itemView.findViewById(R.id.linst);
        }
    }
}
