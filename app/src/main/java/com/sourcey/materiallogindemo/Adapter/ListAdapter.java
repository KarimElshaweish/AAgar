package com.sourcey.materiallogindemo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vignesh_iopex.confirmdialog.Confirm;
import com.github.vignesh_iopex.confirmdialog.Dialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.MapsActivity;
import com.sourcey.materiallogindemo.model.Offer;
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.RealTimeServices.IMyOfferNeed;
import com.sourcey.materiallogindemo.Shared;

import java.util.Calendar;
import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.viewholder> {
    Context _ctx;
    LayoutInflater mlLayoutInflater;
    List<Offer>list;

    IMyOfferNeed iMyOfferNeed;

    public ListAdapter(Context _ctx, List<Offer>list) {
        iMyOfferNeed= (IMyOfferNeed) _ctx;
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
        if(!Shared.customer){
            holder.close_ignore_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(_ctx,"تم نقل الى قائمة التجاهل",Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Ignored").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(list.get(position).getOfferID()).setValue(list.get(position));
                    list.remove(list.get(position));
                    notifyDataSetChanged();

                    if(list.size()==0)
                        iMyOfferNeed.onListEmpty();
                    else
                        iMyOfferNeed.onListHasData();
                }
            });
            FirebaseDatabase.getInstance().getReference().child("NeedsSeen").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dt:dataSnapshot.getChildren()){
                                String dtOfferId=dt.getValue().toString();
                                if(list.size()>0)
                                if(list.get(position).getOfferID().equals(dtOfferId)){
                                    GradientDrawable gd= (GradientDrawable) holder.logo_typ.getBackground();
                                    gd.setColor(Color.parseColor("#d3d3d3"));
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        else {
            holder.close_ignore_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Confirm.using((Activity) _ctx).ask("هل انت متأكد").onPositive("نعم", new Dialog.OnClickListener() {
                        @Override public void onClick(Dialog dialog, int which) {
                            deleteItem(position);
                        }}).onNegative("No",  new Dialog.OnClickListener() {
                        @Override public void onClick(Dialog dialog, int which) {
                            Toast.makeText(_ctx, "تم الإلغاء", Toast.LENGTH_SHORT).show();
                        }}).build().show();
                }
            });

            holder.logo_typ.setVisibility(View.GONE);
        }
        holder.desc.setText(list.get(position).getBuildingTyp());
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
                    if(Shared.keyList!=null)
                    Shared.offerID=Shared.keyList.get(Shared.keyList.size()-1-position);
                    Shared.offerNeed=list.get(position);
                    Intent intent=new Intent(_ctx,OfferResult.class);
                    intent.putExtra("type",list.get(position).getType());
                    intent.putExtra("build_type",list.get(position).getBuildingTyp());
                    intent.putExtra("price",list.get(position).getPrice());
                    intent.putExtra("city",list.get(position).getCity());
                    intent.putExtra("userName",list.get(position).getUserName());
                    intent.putExtra("uid",list.get(position).getUID());
                    intent.putExtra("offer_id",list.get(position).getOfferID());
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
                    Shared.offerNeed=list.get(position);
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
                    intent.putExtra("userName",list.get(position).getUserName());
                    Shared.putOfferOnMap=list.get(position);
                    _ctx.startActivity(intent);
                    Shared.MyOffer=list.get(position);
                    GradientDrawable gd=(GradientDrawable)holder.logo_typ.getBackground();
                    gd.setColor(Color.parseColor("#d3d3d3"));
                    FirebaseDatabase.getInstance().getReference().child("NeedsSeen").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(Calendar.getInstance().getTime().toString()).setValue(list.get(position).getOfferID());
                }
            });
        }
    }

    private void deleteItem(int position) {
        FirebaseDatabase.getInstance().getReference().child("OfferNeeded").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(list.get(position).getOfferID()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(_ctx, "تم الحذف ", Toast.LENGTH_SHORT).show();
                list.remove(list.get(position));
                notifyDataSetChanged();
                if(list.size()==0)
                    iMyOfferNeed.onListEmpty();
                else
                    iMyOfferNeed.onListHasData();
            }
        });
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
        RelativeLayout logo_typ,close_ignore_relative;
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
            logo_typ=itemView.findViewById(R.id.logo_typ);
            close_ignore_relative=itemView.findViewById(R.id.close_ignore_relative);
        }
    }
}
