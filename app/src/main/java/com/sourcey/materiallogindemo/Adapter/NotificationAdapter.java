package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Model.Notification;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.OfferResult;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;
    private Context _ctx;

    public NotificationAdapter(List<Notification> notificationList, Context _ctx) {
        this.notificationList = notificationList;
        this._ctx = _ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Notification notification=notificationList.get(i);
        viewHolder.contentTextView.setText(notification.getContent());
        viewHolder.titleTextView.setText(notification.getTitle());
        if(notification.isSeen()){
            viewHolder.relative_bk.setBackgroundColor(_ctx.getResources().getColor(R.color.white));
        }else{
            viewHolder.relative_bk.setBackgroundColor(_ctx.getResources().getColor(R.color.low_opacity_primary));
        }
        viewHolder.relative_bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notification.setSeen(true);
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Notification");
                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(notification.getNotificaitonID()).setValue(notification);
                viewHolder.relative_bk.setBackgroundColor(_ctx.getResources().getColor(R.color.white));
                getOffer(FirebaseAuth.getInstance().getCurrentUser().getUid(),notification.getOfferID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    private void getOffer(String uid,String offerID){
        FirebaseDatabase.getInstance().getReference("OfferNeeded").child(uid)
                .child(offerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shared.offerNeed=dataSnapshot.getValue(Offer.class);
                Shared.MyOffer=Shared.offerNeed;
                Shared.putOfferOnMap=Shared.offerNeed;
                Intent intent=new Intent(_ctx,OfferResult.class);
                if(Shared.offerNeed!=null) {
                    Shared.offerID = Shared.offerNeed.getOfferID();
                    intent.putExtra("type", Shared.offerNeed.getType());
                    intent.putExtra("price", Shared.offerNeed.getPrice());
                    intent.putExtra("build_type", Shared.offerNeed.getBuildingTyp());
                    intent.putExtra("city", Shared.offerNeed.getPrice());
                    _ctx.startActivity(intent);
                }else{
                    Toast.makeText(_ctx,"لم يعد متاح",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView;
        RelativeLayout relative_bk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.title);
            contentTextView=itemView.findViewById(R.id.content);
            relative_bk= itemView.findViewById(R.id.relative_bk);
        }
    }
}
