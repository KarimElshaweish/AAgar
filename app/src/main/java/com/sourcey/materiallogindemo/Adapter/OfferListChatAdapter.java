package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourcey.materiallogindemo.ChatList;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;

public class OfferListChatAdapter  extends RecyclerView.Adapter<OfferListChatAdapter.ViewHolder> {
    Context _ctx;
    List<Offer>offersList;

    public OfferListChatAdapter(Context _ctx, List<Offer> offersList) {
        this._ctx = _ctx;
        this.offersList = offersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.offer_chat_item_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.offerPrice.setText(offersList.get(i).getPrice());
        viewHolder.offerPlace.setText(offersList.get(i).getCity());
        viewHolder.offerType.setText(offersList.get(i).getType());
        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared.offerID=offersList.get(i).getOfferID();
                Intent intent=new Intent(_ctx,ChatList.class);
                intent.putExtra("id",offersList.get(i).getOfferID());
                _ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView offerType,offerPlace,offerPrice;
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offerType=itemView.findViewById(R.id.offerType);
            offerPlace=itemView.findViewById(R.id.offerplace);
            offerPrice=itemView.findViewById(R.id.offerPrice);
            cv=itemView.findViewById(R.id.cv);
        }
    }
}
