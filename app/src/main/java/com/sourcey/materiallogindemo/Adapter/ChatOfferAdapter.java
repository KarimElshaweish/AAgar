package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.model.OfferResult;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.sourcey.materiallogindemo.Shared.useList;

public class ChatOfferAdapter extends RecyclerView.Adapter<ChatOfferAdapter.ViewHolder> {
    List<OfferResult> offerResultList=new ArrayList<>();
    Context _ctx;
    public ChatOfferAdapter(Set<OfferResult> offerResultList, Context _ctx) {
        this.offerResultList.addAll(offerResultList);
        this._ctx = _ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(_ctx).inflate(R.layout.chat_offer_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final OfferResult offerResult=offerResultList.get(i);
        viewHolder.city.setText(offerResult.getCity());
        viewHolder.price.setText(offerResult.getPrice());
        viewHolder.type.setText(offerResult.getType());
        viewHolder.buildType.setText(offerResult.getBuildingType());
        Shared.allUsersChat= true;
        getUsers(offerResult, viewHolder);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsers(offerResult, viewHolder);
                //   readCaht();
            }
        });
    }

    private void getUsers(final OfferResult offerResult, @NonNull final ViewHolder viewHolder) {
        Shared.chatOfferId=offerResult.getOfferID();
        viewHolder.pb.setVisibility(View.VISIBLE);
        reference= FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               List<User> musers22=new ArrayList<>();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    User user=dt.getValue(User.class);
                    for(String id:useList){
                        if(user.getUserID().equals(id)){
                            if(!musers22.contains(user))
                                musers22.add(user);
//                            if(musers.size()!=0){
//                                for(int i=0;i<musers.size();i++){
//                                    User user1=musers.get(i);
//                                    if(!user.getUserID().equals(user1.getUserID()))
//                                        musers.add(user1);
//
//                                }
//                            }else {
//                                musers.add(user);
//                            }
                        }
                    }
                }
                userAdapter=new UserAdapter(_ctx,musers22,offerResult);
                viewHolder.rv.setHasFixedSize(true);
                viewHolder.rv.setLayoutManager(new LinearLayoutManager(_ctx));
                viewHolder.rv.setAdapter(userAdapter);
                viewHolder.pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    List<User>musers;
    DatabaseReference reference;
    UserAdapter userAdapter;
    private void readCaht(){
        //pb.setVisibility(View.VISIBLE);
        musers=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                musers.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    User user=dt.getValue(User.class);
                    for(String id:useList){
                        if(user.getUserID().equals(id)){
                            if(musers.size()!=0){
                                for (User user1:musers){
                                    if(!user.getUserID().equals(user1.getUserID())){
                                        musers.add(user);
                                    }
                                }
                            }else {
                                musers.add(user);
                            }
                        }
                    }
                }
                userAdapter=new UserAdapter(_ctx,musers);
//                rv.setHasFixedSize(true);
//                rv.setLayoutManager(new LinearLayoutManager(_ctx));
//                rv.setAdapter(userAdapter);
             //   pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return offerResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView buildType,type,price,city;
        CardView view;
        RecyclerView rv;
        ProgressBar pb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buildType=itemView.findViewById(R.id.buildType);
            type=itemView.findViewById(R.id.type);
            price=itemView.findViewById(R.id.price);
            city=itemView.findViewById(R.id.city);
            view=itemView.findViewById(R.id.view);
            rv=itemView.findViewById(R.id.rv);
            pb=itemView.findViewById(R.id.pb);
        }
    }
}
