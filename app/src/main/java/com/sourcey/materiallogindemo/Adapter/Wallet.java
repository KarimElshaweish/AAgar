package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.materiallogindemo.Model.Deals;
import com.sourcey.materiallogindemo.R;

import java.util.List;

public class Wallet extends RecyclerView.Adapter<Wallet.ViewHolder> {
    List<Deals>list;
    Context _ctx;

    public Wallet(List<Deals> list, Context _ctx) {
        this.list = list;
        this._ctx = _ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(_ctx).inflate(R.layout.deal_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if(list.get(i).isAgree())
            viewHolder.buttons.setVisibility(View.GONE);
        viewHolder.city.setText(list.get(i).getCity());
        viewHolder.price.setText(String.valueOf(list.get(i).getPrice()));
        viewHolder.type.setText(list.get(i).getOfferName());
        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deals deals=list.get(i);
                deals.setAgree(true);
                FirebaseDatabase.getInstance().getReference("Deals")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(list.get(i).getKey())
                        .setValue(deals);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView city,type,price;
        Button confirm;
        LinearLayout buttons;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.type);
            city=itemView.findViewById(R.id.city);
            price=itemView.findViewById(R.id.price);
            confirm=itemView.findViewById(R.id.confirm);
            buttons=itemView.findViewById(R.id.buttons);
        }
    }
}
