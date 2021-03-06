package com.sourcey.materiallogindemo.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.materiallogindemo.Model.Chat;
import com.sourcey.materiallogindemo.Model.User;
import com.sourcey.materiallogindemo.R;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewholder> {
    public static final  int MSG_TYPE_LEFT=0;
    public static final  int MSG_TYPE_RIGHT=1;
    private Context _ctx;
    List<Chat>mChat;
    private String imageUrl;
    FirebaseUser firebaseUser;
    public MessageAdapter(Context _ctx, List<Chat> mChat,String imageUrl) {
        this._ctx = _ctx;
        this.mChat = mChat;

        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view=LayoutInflater.from(_ctx).inflate(R.layout.chat_right,parent,false);
            return new MessageAdapter.viewholder(view);
        }else{
            View view=LayoutInflater.from(_ctx).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        Chat chat=mChat.get(position);
        holder.showMessage.setText(chat.getMessage());
        FirebaseDatabase.getInstance().getReference("user")
                .child(chat.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())?chat.getReciver():
                        chat.getSender())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.toString().equals("admin")){
                            holder.profileImage.setImageDrawable(_ctx.getResources().getDrawable(R.mipmap.ic_launcher));
                        }else {
                            User user = dataSnapshot.getValue(User.class);
                            if (user!=null&&user.getProfilePic() != null)
                                Glide.with(_ctx).load(user.getProfilePic())
                                        .placeholder(R.mipmap.ic_launcher)
                                        .into(holder.profileImage);
                            else
                                holder.profileImage.setImageDrawable(_ctx.getResources().getDrawable(R.mipmap.ic_launcher));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
       if(position==mChat.size()-1) {
           if (chat.isIsseen()) {
                holder.txt_seen.setText("Seen");
           } else {
                holder.txt_seen.setText("Delivered");
           }
       }else{
           holder.txt_seen.setVisibility(View.GONE);
       }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView showMessage,txt_seen;
        ImageView profileImage;

        public viewholder(View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profile);
            showMessage=itemView.findViewById(R.id.show_message);
            txt_seen=itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
