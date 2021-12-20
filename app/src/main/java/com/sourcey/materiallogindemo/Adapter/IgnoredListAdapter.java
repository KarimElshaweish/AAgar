package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.materiallogindemo.model.Offer;
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;
import com.sourcey.materiallogindemo.RealTimeServices.IIgnoredServices;

import java.util.List;

public class IgnoredListAdapter extends RecyclerView.Adapter<IgnoredListAdapter.ViewHolder> {
    List<Offer>ignoredOfferList;
    Context _ctx;

    Boolean sold;

    IIgnoredServices ignoredServices;
    public IgnoredListAdapter(List<Offer> ignoredOfferList, Context _ctx, Boolean sold) {
        this.ignoredOfferList = ignoredOfferList;
        this._ctx = _ctx;
        this.sold = sold;
        ignoredServices= (IIgnoredServices) _ctx;
    }
    String home="";
    public IgnoredListAdapter(List<Offer> ignoredOfferList, Context _ctx, String home) {
        this.ignoredOfferList = ignoredOfferList;
        this._ctx = _ctx;
        this.home=home;
        ignoredServices= (IIgnoredServices) _ctx;
    }

    public IgnoredListAdapter(List<Offer> ignoredOfferList, Context _ctx) {
        this.ignoredOfferList = ignoredOfferList;
        this._ctx = _ctx;
        ignoredServices= (IIgnoredServices) _ctx;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        holder.saleDoneText.setVisibility(View.VISIBLE);
        if(home!="")
            holder.saleDoneText.setVisibility(View.GONE);
        holder.logo_typ.setVisibility(View.GONE);
        holder.desc.setText(ignoredOfferList.get(position).getBuildingTyp());
        holder.cityText.setText(ignoredOfferList.get(position).getCity());
        holder.TypeText.setText(ignoredOfferList.get(position).getType());
        holder.priceText.setText(ignoredOfferList.get(position).getPrice() + " ريال  ");
        holder.street.setText(ignoredOfferList.get(position).getStreet());
        Shared.MyOffer=ignoredOfferList.get(position);
        if(Shared.customer){
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Shared.keyList!=null)
                        Shared.offerID=Shared.keyList.get(position);
                    Shared.offerNeed=ignoredOfferList.get(position);
                    Intent intent=new Intent(_ctx, OfferResult.class);
                    intent.putExtra("type",ignoredOfferList.get(position).getType());
                    intent.putExtra("build_type",ignoredOfferList.get(position).getBuildingTyp());
                    intent.putExtra("price",ignoredOfferList.get(position).getPrice());
                    intent.putExtra("city",ignoredOfferList.get(position).getCity());
                    intent.putExtra("sold",sold);
                    intent.putExtra("offer_id",ignoredOfferList.get(position).getOfferID());
                    Shared.putOfferOnMap=ignoredOfferList.get(position);
                    _ctx.startActivity(intent);
                    Shared.MyOffer=ignoredOfferList.get(position);
                }
            });

        }
        else{
            holder.close_ignore_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseDatabase.getInstance().getReference().child("Ignored").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(ignoredOfferList.get(position).getOfferID()).setValue(null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(_ctx,"تم استرجاع الطلب",Toast.LENGTH_SHORT).show();
                                    ignoredOfferList.remove(position);
                                    notifyDataSetChanged();
                                    ignoredServices.onListEmpty();
                                }
                            });
                }
            });
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Shared.offerNeed=ignoredOfferList.get(position);
                    Shared.toID=ignoredOfferList.get(position).getUID();
                    Shared.offer=ignoredOfferList.get(position);
                    Shared.offerID=ignoredOfferList.get(position).getOfferID();
                    Intent intent=new Intent(_ctx,OfferResult.class);
                    intent.putExtra("type", ignoredOfferList.get(position).getType()==null ?" ":ignoredOfferList.get(position).getType());
                    intent.putExtra("build_type",ignoredOfferList.get(position).getBuildingTyp());
                    intent.putExtra("price",ignoredOfferList.get(position).getPrice());
                    intent.putExtra("city",ignoredOfferList.get(position).getCity());
                    intent.putExtra("userID",ignoredOfferList.get(position).getUID());
                    intent.putExtra("offerID",ignoredOfferList.get(position).getOfferID());
                    intent.putExtra("userName",ignoredOfferList.get(position).getUserName());
                    intent.putExtra("ignore",true);
                    Shared.putOfferOnMap=ignoredOfferList.get(position);
                    _ctx.startActivity(intent);
                    Shared.MyOffer=ignoredOfferList.get(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ignoredOfferList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout close_ignore_relative,logo_typ;
        TextView priceText,TypeText,cityText,street,desc,seeMore;
        View bottom;
        CardView cv;
        LinearLayout lindesc,linst;
        TextView saleDoneText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            close_ignore_relative=itemView.findViewById(R.id.close_ignore_relative);
            logo_typ=itemView.findViewById(R.id.logo_typ);


            priceText=(TextView)itemView.findViewById(R.id.price);
            TypeText=itemView.findViewById(R.id.type);
            cityText=itemView.findViewById(R.id.city);
            cv=itemView.findViewById(R.id.cv);
            bottom=itemView.findViewById(R.id.bottom);
            street=itemView.findViewById(R.id.street);
            bottom.setVisibility(View.GONE);
            desc=itemView.findViewById(R.id.desc);
            seeMore=itemView.findViewById(R.id.seeMore);
            lindesc=itemView.findViewById(R.id.lindesc);
            linst=itemView.findViewById(R.id.linst);
            saleDoneText=itemView.findViewById(R.id.saleDoneText);
        }
    }
}
