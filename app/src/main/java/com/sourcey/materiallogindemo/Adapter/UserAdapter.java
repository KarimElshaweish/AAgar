package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.ChatAct;
import com.sourcey.materiallogindemo.Model.Chat;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.User;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {

    private Context _ctx;
    private List<User>mList;
    private OfferResult offerID;
    String theLastMessage;
    String user;

    public UserAdapter(Context _ctx, List<User> mList) {
        this._ctx = _ctx;
        this.mList = mList;
    }

    public UserAdapter(Context _ctx, List<User> mList, OfferResult offerID) {
        this._ctx = _ctx;
        this.mList = mList;
        this.offerID = offerID;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(_ctx).inflate(R.layout.user_item,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final User user=mList.get(position);
        lastMessage(user.getUserID(),holder.lastMsg,holder.card);
        holder.usernameText.setText(user.getName()+"( "+user.getType()+" )");
        Glide.with(_ctx).load(user.getProfilePic()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.AllMesage=true;
                Shared.sent_id=user.getUserID();
                if(offerID!=null){
                    Shared.chatOfferId=offerID.getOfferID();
                }
                Intent intent=new Intent(_ctx,ChatAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Shared.offerKnow=offerID;
                _ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView usernameText,lastMsg;
        CircleImageView profile;
        RelativeLayout rl;
        CardView card;
       public viewholder(View itemView) {
           super(itemView);
           usernameText=itemView.findViewById(R.id.usernameText);
           profile=itemView.findViewById(R.id.profile);
           rl=itemView.findViewById(R.id.rl);
           lastMsg=itemView.findViewById(R.id.last_msg);
           card=itemView.findViewById(R.id.card);
       }
   }
   private void lastMessage(final String userId, final TextView last_msg, final CardView cardView){
        theLastMessage="default";
        user="default";
       DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                       Chat chat = snapshot.getValue(Chat.class);
                       if (chat.getReciver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                               && chat.getSender().equals(userId) ||
                               chat.getReciver().equals(userId) &&
                                       chat.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                           if(
                                   chat.getId().equals(offerID.getOfferID())
                           )
                                    theLastMessage = chat.getMessage();
                                    user=chat.getSender();

                       }

               }
               String temp = theLastMessage;
               switch (theLastMessage){
                   case "default":
                       last_msg.setText("No Message");
                       temp="No Message";
                       cardView.setVisibility(View.GONE);
                       break;
                       default:
                           last_msg.setText(theLastMessage);
                           break;
               }
               theLastMessage="default";
               switch (user){
                   case "default":
                        temp=last_msg.getText().toString();
                       last_msg.setText(""+temp);
                       cardView.setVisibility(View.GONE);
                       break;
                       default:
                           if(user.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                               last_msg.setText(temp + " : أنت ");
                           }
                           else{
                               if(Shared.customer){
                                   last_msg.setText(temp+" : وسيط ");
                               }else{
                                   last_msg.setText(temp+" : عميل ");

                               }
                           }
                           break;
               }
               theLastMessage="default";
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
   }
}
