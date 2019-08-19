package com.sourcey.materiallogindemo.Adapter;

import android.app.Activity;
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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.ui.IconGenerator;
import com.sourcey.materiallogindemo.AddResultOffer;
import com.sourcey.materiallogindemo.MapsActivity;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.OfferShare;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;


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
    public void onBindViewHolder(@NonNull viewholder holder, final int position) {
            //  holder.dateTextView.setText(list.get(position).getDate());

        holder.cityText.setText(list.get(position).getCity());
        holder.TypeText.setText(list.get(position).getType());
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
                    Shared.offerID=Shared.keyList.get(position);
                    _ctx.startActivity(new Intent(_ctx,OfferResult.class));

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
                    _ctx.startActivity(new Intent(_ctx,MapsActivity.class));
//                    new OoOAlertDialog.Builder((Activity)_ctx)
//                            .setTitle("قم بالاختيار")
//                            .setAnimation(Animation.SIDE)
//                            .setPositiveButton("النطاق الجغرافى", new OnClickListener() {
//                                @Override
//                                public void onClick() {
//                                    Shared.toID=list.get(position).getUID();
//                                    Shared.offer=list.get(position);
//                                    Shared.offerID=Shared.keyList.get(position);
//                                    _ctx.startActivity(new Intent(_ctx,MapsActivity.class));
//
//                                }
//                            })
//                            .setNegativeButton("مشاركة عرض", new OnClickListener() {
//                                @Override
//                                public void onClick() {
//                                    Shared.offer=list.get(position);
//                                    Shared.offerID=Shared.keyList.get(position);
//                                    Shared.toID=list.get(position).getUID();
//                                    _ctx.startActivity(new Intent(_ctx,OfferResult.class));
//                                }
//                            })
//                            .setPositiveButtonColor(R.color.primary)
//                            .setNegativeButtonColor(R.color.jet)
//                            .build();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView priceText,TypeText,cityText,street;
        ImageView img,area,offerImage;
        View bottom;
        CardView cv;
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

        }
    }
}
